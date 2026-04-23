package realcolin.whmod.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.item.recipe.FactionCraftingRecipe;
import realcolin.whmod.item.recipe.WHRecipes;

public class FactionCraftingResultSLot extends Slot {
    private final CraftingContainer craftSlots;
    private final Player player;
    private int removeCount;

    public FactionCraftingResultSLot(Player player, CraftingContainer craftSlots, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.craftSlots = craftSlots;
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack itemStack) {
        return false;
    }

    @Override
    public @NonNull ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(@NonNull ItemStack picked, int count) {
        this.removeCount += count;
        this.checkTakeAchievements(picked);
    }

    @Override
    protected void onSwapCraft(int count) {
        this.removeCount += count;
    }

    @Override
    protected void checkTakeAchievements(@NonNull ItemStack carried) {
        if (this.removeCount > 0) {
            carried.onCraftedBy(this.player, this.removeCount);
            EventHooks.firePlayerCraftingEvent(this.player, carried, this.craftSlots);
        }

        if (this.container instanceof RecipeCraftingHolder recipeCraftingHolder) {
            recipeCraftingHolder.awardUsedRecipes(this.player, this.craftSlots.getItems());
        }

        this.removeCount = 0;
    }

    private static NonNullList<ItemStack> copyAllInputItems(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for(int slot = 0; slot < result.size(); ++slot) {
            result.set(slot, input.getItem(slot));
        }

        return result;
    }

    private NonNullList<ItemStack> getRemainingItems(CraftingInput input, Level level) {
        NonNullList<ItemStack> ret;

        if (level instanceof ServerLevel serverLevel) {
            ret = serverLevel.recipeAccess().getRecipeFor(
                    WHRecipes.FACTION_CRAFTING.get(),
                    input,
                    serverLevel
            ).map((recipe) -> (recipe.value()).getRemainingItems(input)).orElseGet(() -> copyAllInputItems(input));

        } else {
            ret = FactionCraftingRecipe.defaultCraftingReminder(input);
        }

        return ret;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onTake(@NonNull Player player, @NonNull ItemStack carried) {
        this.checkTakeAchievements(carried);
        var positionedRecipe = this.craftSlots.asPositionedCraftInput();
        var input = positionedRecipe.input();
        int recipeLeft = positionedRecipe.left();
        int recipeTop = positionedRecipe.top();
        CommonHooks.setCraftingPlayer(player);
        var remaining = this.getRemainingItems(input, player.level());
        CommonHooks.setCraftingPlayer(null);

        for(int y = 0; y < input.height(); ++y) {
            for(int x = 0; x < input.width(); ++x) {
                int slot = x + recipeLeft + (y + recipeTop) * this.craftSlots.getWidth();
                var itemStack = this.craftSlots.getItem(slot);
                var replacement = remaining.get(x + y * input.width());
                if (!itemStack.isEmpty()) {
                    this.craftSlots.removeItem(slot, 1);
                    itemStack = this.craftSlots.getItem(slot);
                }

                if (!replacement.isEmpty()) {
                    if (itemStack.isEmpty()) {
                        this.craftSlots.setItem(slot, replacement);
                    } else if (ItemStack.isSameItemSameComponents(itemStack, replacement)) {
                        replacement.grow(itemStack.getCount());
                        this.craftSlots.setItem(slot, replacement);
                    } else if (!this.player.getInventory().add(replacement)) {
                        this.player.drop(replacement, false);
                    }
                }
            }
        }
    }

    @Override
    public boolean isFake() {
        return true;
    }
}
