package realcolin.whmod.item.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;

public interface FactionCraftingRecipe extends Recipe<CraftingInput> {
    @NonNull RecipeSerializer<? extends FactionCraftingRecipe> getSerializer();
    Faction faction();

    default NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return defaultCraftingReminder(input);
    }

    static NonNullList<ItemStack> defaultCraftingReminder(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for(int slot = 0; slot < result.size(); ++slot) {
            ItemStack item = input.getItem(slot);
            ItemStackTemplate remainder = item.getCraftingRemainder();
            result.set(slot, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }

        return result;
    }
}
