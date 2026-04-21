package realcolin.whmod.item.recipe;

import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;

public interface FactionCraftingRecipe extends Recipe<CraftingInput> {
    @NonNull RecipeSerializer<? extends FactionCraftingRecipe> getSerializer();
    Faction faction();
}
