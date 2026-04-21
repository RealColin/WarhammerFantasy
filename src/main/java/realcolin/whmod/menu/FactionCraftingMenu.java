package realcolin.whmod.menu;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.block.FactionCraftingTableBlock;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.recipe.WHRecipeBookTypes;
import realcolin.whmod.item.recipe.WHRecipes;

import java.util.List;

public class FactionCraftingMenu extends AbstractCraftingMenu {
    private final Faction faction;
    private final ContainerLevelAccess access;
    private final Player player;

    private boolean placingRecipe;

    public FactionCraftingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL, Faction.NONE);
    }

    public FactionCraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access, Faction faction) {
        super(WHMenuTypes.FACTION_CRAFTING.get(), containerId, 3, 3);
        this.faction = faction;
        this.access = access;
        this.player = inventory.player;
        this.addResultSlot(player, 124, 35);
        this.addCraftingGridSlots(30, 17);
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public void slotsChanged(@NonNull Container container) {
        if (!this.placingRecipe) {
            this.access.execute((level, _) -> {
                if (level instanceof ServerLevel serverLevel) {
                    slotChangedCraftingGrid(this, serverLevel, this.player, this.craftSlots);
                }

            });
        }
    }

    private void slotChangedCraftingGrid(AbstractContainerMenu menu,
                                                ServerLevel level,
                                                Player player,
                                                CraftingContainer container) {
        var result = ItemStack.EMPTY;
        var serverPlayer = (ServerPlayer)player;
        var input = container.asCraftInput();

        var optional = level.recipeAccess().getRecipeFor(
                WHRecipes.FACTION_CRAFTING.get(),
                input,
                level
        );

        if (optional.isPresent()) {
            var recipe = optional.get().value();

            if (recipe.faction() == this.faction) {
                result = recipe.assemble(input);
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

    @Override
    protected void finishPlacingRecipe(@NonNull ServerLevel level, @NonNull RecipeHolder<CraftingRecipe> recipe) {
        this.placingRecipe = false;
        slotChangedCraftingGrid(this, level, this.player, this.craftSlots);
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