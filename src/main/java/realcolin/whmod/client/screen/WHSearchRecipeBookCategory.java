package realcolin.whmod.client.screen;

import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import realcolin.whmod.item.recipe.WHRecipeBookCategories;

public enum WHSearchRecipeBookCategory implements ExtendedRecipeBookCategory {

    CRAFTING(WHRecipeBookCategories.FACTION_CRAFTING.get());
    private final RecipeBookCategory[] included;

    WHSearchRecipeBookCategory(RecipeBookCategory... includedCategories) {
        this.included = includedCategories;
    }

    public RecipeBookCategory[] included() {
        return included;
    }
}
