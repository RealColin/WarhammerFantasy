package realcolin.whmod.menu;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.block.FactionCraftingTableBlock;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.recipe.FactionCraftingRecipe;
import realcolin.whmod.item.recipe.FactionShapedCraftingRecipe;
import realcolin.whmod.item.recipe.WHRecipeBookTypes;
import realcolin.whmod.item.recipe.WHRecipes;

import java.util.ArrayList;
import java.util.List;

public class FactionCraftingMenu extends AbstractCraftingMenu {
    private final Faction faction;
    private final ContainerLevelAccess access;
    private final Player player;

    private boolean placingRecipe;

    public FactionCraftingMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.create(inventory.player.level(), buf.readBlockPos()), buf.readEnum(Faction.class));
    }

    public FactionCraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access, Faction faction) {
        super(WHMenuTypes.FACTION_CRAFTING.get(), containerId, 3, 3);
        this.faction = faction;
        this.access = access;
        this.player = inventory.player;
        //this.addResultSlot(player, 124, 35);
        this.addSlot(new FactionCraftingResultSLot(player, this.craftSlots, this.resultSlots, 0, 124, 35));
        this.addCraftingGridSlots(30, 17);
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public @NonNull PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, @NonNull RecipeHolder<?> recipe, @NonNull ServerLevel level, @NonNull Inventory inventory) {
        RecipeHolder<FactionCraftingRecipe> facRecipe = (RecipeHolder<FactionCraftingRecipe>) recipe;
        this.beginPlacingRecipe();

        if (facRecipe.value() instanceof FactionShapedCraftingRecipe shaped) {
            var ret = placeShapedRecipe(shaped, inventory, useMaxItems, allowDroppingItemsToClear);
            this.finishRecipePlacement(level);
            return ret;
        }

        RecipeBookMenu.PostPlaceAction ret;

        try{
            var inputSlots = getInputGridSlots();
            ret = ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {

                @Override
                public void fillCraftSlotsStackedContents(@NonNull StackedItemContents stackedItemContents) {
                    FactionCraftingMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
                }

                @Override
                public void clearCraftingContent() {
                    FactionCraftingMenu.this.resultSlots.clearContent();
                    FactionCraftingMenu.this.craftSlots.clearContent();
                }

                @Override
                public boolean recipeMatches(@NonNull RecipeHolder<FactionCraftingRecipe> recipeHolder) {
                    return (recipeHolder.value()).matches(FactionCraftingMenu.this.craftSlots.asCraftInput(), FactionCraftingMenu.this.owner().level());
                }


            }, this.getGridWidth(), this.getGridHeight(), inputSlots, inputSlots, inventory, facRecipe, useMaxItems, allowDroppingItemsToClear);
        } finally {
            this.finishRecipePlacement(level);
        }

        return ret;
    }

    private PostPlaceAction placeShapedRecipe(
            FactionShapedCraftingRecipe recipe,
            Inventory inventory,
            boolean useMaxItems,
            boolean allowDroppingItemsToClear
    ) {

        var menu = new ServerPlaceRecipe.CraftingMenuAccess<FactionShapedCraftingRecipe>() {

            @Override
            public void fillCraftSlotsStackedContents(@NonNull StackedItemContents stackedItemContents) {
                FactionCraftingMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
            }

            @Override
            public void clearCraftingContent() {
                FactionCraftingMenu.this.resultSlots.clearContent();
                FactionCraftingMenu.this.craftSlots.clearContent();
            }

            @Override
            public boolean recipeMatches(RecipeHolder<FactionShapedCraftingRecipe> recipeHolder) {
                return (recipeHolder.value()).matches(FactionCraftingMenu.this.craftSlots.asCraftInput(), FactionCraftingMenu.this.owner().level());
            }
        };

        if (!allowDroppingItemsToClear && !testClearGrid(inventory)) {
            return RecipeBookMenu.PostPlaceAction.NOTHING;
        } else {
            StackedItemContents availableItems = new StackedItemContents();
            inventory.fillStackedContents(availableItems);
            menu.fillCraftSlotsStackedContents(availableItems);

            // try to place the recipe:
            if (availableItems.canCraft(recipe, null)) {
                putRecipeOnGrid(recipe, availableItems, inventory, useMaxItems);
                inventory.setChanged();
                return PostPlaceAction.NOTHING;
            } else {
                clearGrid(inventory);
                inventory.setChanged();
                return PostPlaceAction.PLACE_GHOST_RECIPE;
            }

        }
    }

    private void putRecipeOnGrid(FactionShapedCraftingRecipe recipe, StackedItemContents availableItems, Inventory inventory, boolean useMaxItems) {
        var matches = recipe.matches(this.craftSlots.asCraftInput(), this.owner().level());
        var biggestCraftableStack = availableItems.getBiggestCraftableStack(recipe, null);
        var inputSlots = getInputGridSlots();
        var gridSize = 3;

        if (matches) {
            for (var slot : inputSlots) {
                var stack = slot.getItem();
                if (!stack.isEmpty() && Math.min(biggestCraftableStack, stack.getMaxStackSize()) < stack.getCount() + 1) {
                    return;
                }
            }
        }

        int amountToCraft = this.calculateAmountToCraft(biggestCraftableStack, matches, useMaxItems);

        List<Holder<Item>> itemsUsedPerIngredient = new ArrayList<>();

        if (availableItems.canCraft(recipe, amountToCraft, itemsUsedPerIngredient::add)) {
            int adjustedAmountToCraft = clampToMaxStackSize(amountToCraft, itemsUsedPerIngredient);
            if (adjustedAmountToCraft != amountToCraft) {
                itemsUsedPerIngredient.clear();
                if (!availableItems.canCraft(recipe, adjustedAmountToCraft, itemsUsedPerIngredient::add)) {
                    return;
                }
            }

            this.clearGrid(inventory);
            PlaceRecipeHelper.placeRecipe(
                    gridSize,
                    gridSize,
                    recipe.pattern().width(),
                    recipe.pattern().height(),
                    recipe.placementInfo().slotsToIngredientIndex(),
                    (ingredientIndex, gridIndex, _, _) -> {
                        if (ingredientIndex != -1) {
                            var targetGridSlot = inputSlots.get(gridIndex);
                            var itemUsed = itemsUsedPerIngredient.get(ingredientIndex);
                            int remainingCount = adjustedAmountToCraft;

                            while (remainingCount > 0) {
                                remainingCount = this.moveItemToGrid(targetGridSlot, itemUsed, remainingCount, inventory);
                                if (remainingCount == -1) {
                                    return;
                                }
                            }
                        }
                    }

            );
        }
    }

    private static int clampToMaxStackSize(int value, List<Holder<Item>> items) {
        for (Holder<Item> item : items) {
            value = Math.min(value, item.components().getOrDefault(DataComponents.MAX_STACK_SIZE, 1));
        }

        return value;
    }

    private int calculateAmountToCraft(int biggestCraftableStack, boolean recipeMatchesPlaced, boolean useMaxItems) {
        if (useMaxItems) {
            return biggestCraftableStack;
        } else if (recipeMatchesPlaced) {
            int smallestStackSize = Integer.MAX_VALUE;

            for (Slot inputSlot : getInputGridSlots()) {
                ItemStack itemStack = inputSlot.getItem();
                if (!itemStack.isEmpty() && smallestStackSize > itemStack.getCount()) {
                    smallestStackSize = itemStack.getCount();
                }
            }

            if (smallestStackSize != Integer.MAX_VALUE) {
                smallestStackSize++;
            }

            return smallestStackSize;
        } else {
            return 1;
        }
    }

    private int moveItemToGrid(Slot targetSlot, Holder<Item> itemInInventory, int count, Inventory inventory) {
        ItemStack itemInTargetSlot = targetSlot.getItem();
        int inventorySlotId = inventory.findSlotMatchingCraftingIngredient(itemInInventory, itemInTargetSlot);
        if (inventorySlotId == -1) {
            return -1;
        } else {
            ItemStack inventoryItem = inventory.getItem(inventorySlotId);
            ItemStack takenStack;
            if (count < inventoryItem.getCount()) {
                takenStack = inventory.removeItem(inventorySlotId, count);
            } else {
                takenStack = inventory.removeItemNoUpdate(inventorySlotId);
            }

            int takenCount = takenStack.getCount();
            if (itemInTargetSlot.isEmpty()) {
                targetSlot.set(takenStack);
            } else {
                itemInTargetSlot.grow(takenCount);
            }

            return count - takenCount;
        }
    }


    private void clearGrid(Inventory inventory) {
        var slotsToClear = getInputGridSlots();

        for (var slot : slotsToClear) {
            var stackCopy = slot.getItem().copy();
            inventory.placeItemBackInInventory(stackCopy, false);
            slot.set(stackCopy);
        }
        resultSlots.clearContent();
        craftSlots.clearContent();
    }

    private boolean testClearGrid(Inventory inventory) {
        List<ItemStack> freeSlots = Lists.newArrayList();

        int freeSlotsInInventory = 0;
        for(ItemStack item : inventory.getNonEquipmentItems()) {
            if (item.isEmpty()) {
                ++freeSlotsInInventory;
            }
        }

        for(Slot inputSlot : this.getInputGridSlots()) {
            ItemStack itemStack = inputSlot.getItem().copy();
            if (!itemStack.isEmpty()) {
                int slotId = inventory.getSlotWithRemainingSpace(itemStack);
                if (slotId == -1 && freeSlots.size() <= freeSlotsInInventory) {
                    for(ItemStack itemStackInList : freeSlots) {
                        if (ItemStack.isSameItem(itemStackInList, itemStack) && itemStackInList.getCount() != itemStackInList.getMaxStackSize() && itemStackInList.getCount() + itemStack.getCount() <= itemStackInList.getMaxStackSize()) {
                            itemStackInList.grow(itemStack.getCount());
                            itemStack.setCount(0);
                            break;
                        }
                    }

                    if (!itemStack.isEmpty()) {
                        if (freeSlots.size() >= freeSlotsInInventory) {
                            return false;
                        }

                        freeSlots.add(itemStack);
                    }
                } else if (slotId == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void slotsChanged(@NonNull Container container) {
        if (!this.placingRecipe) {
            this.access.execute((level, _) -> {
                if (level instanceof ServerLevel serverLevel) {
                    slotChangedCraftingGrid(this, serverLevel, this.player, this.craftSlots, this.resultSlots);
                }

            });
        }
    }

    private void slotChangedCraftingGrid(AbstractContainerMenu menu,
                                                ServerLevel level,
                                                Player player,
                                                CraftingContainer container,
                                            ResultContainer resultSlots) {
        var result = ItemStack.EMPTY;
        var serverPlayer = (ServerPlayer)player;
        var input = container.asCraftInput();

        var optional = level.recipeAccess().getRecipeFor(
                WHRecipes.FACTION_CRAFTING.get(),
                input,
                level
        );

        if (optional.isPresent()) {
            var holder = optional.get();
            var recipe = holder.value();

            if (recipe.faction() == this.faction) {
                if (resultSlots.setRecipeUsed(serverPlayer, holder)) {
                    var recipeResult = recipe.assemble(input);
                    if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                        result = recipeResult;
                    }
                }
            }
        }

        this.resultSlots.setItem(0, result);
        this.setRemoteSlot(0, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
    }

    @Override
    protected void beginPlacingRecipe() {
        this.placingRecipe = true;
    }

//    @Override
//    protected void finishPlacingRecipe(@NonNull ServerLevel level, @NonNull RecipeHolder<CraftingRecipe> recipe) {
//        this.placingRecipe = false;
//        slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots);
//    }



    protected void finishRecipePlacement(@NonNull ServerLevel level) {
        this.placingRecipe = false;
        slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots);
    }

    @Override
    public void removed(@NonNull Player player) {
        super.removed(player);
        this.access.execute((_, _) -> this.clearContainer(player, this.craftSlots));
    }

    @Override
    public @NonNull Slot getResultSlot() {
        return this.slots.getFirst();
    }

    @Override
    public @NonNull List<Slot> getInputGridSlots() {
        return this.slots.subList(1, 10);
    }

    @Override
    protected @NonNull Player owner() {
        return this.player;
    }

    @Override
    public @NonNull RecipeBookType getRecipeBookType() {
        return WHRecipeBookTypes.FACTION_CRAFTING;
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        var clicked = ItemStack.EMPTY;
        var slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            var stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex == 0) {
                stack.getItem().onCraftedBy(stack, player);
                if (!this.moveItemStackTo(stack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            } else if (slotIndex >= 10 && slotIndex < 46) {
                if (!this.moveItemStackTo(stack, 1, 10, false)) {
                    if (slotIndex < 37) {
                        if (!this.moveItemStackTo(stack, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(stack, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(stack, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            if (slotIndex == 0) {
                player.drop(stack, false);
            }
        }

        return clicked;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.access.evaluate((level, pos) -> {
            var state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof FactionCraftingTableBlock block)) {
                return false;
            }

            if (block.getFaction() != this.faction) {
                return false;
            }

            return player.isWithinBlockInteractionRange(pos, 4.0);
        }, true);
    }

    public Faction getFaction() {
        return this.faction;
    }
}