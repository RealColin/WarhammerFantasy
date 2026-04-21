package realcolin.whmod.item.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

public class WHRecipeBookCategories {
    public static final DeferredRegister<RecipeBookCategory> CATEGORIES = DeferredRegister.create(BuiltInRegistries.RECIPE_BOOK_CATEGORY, WHMod.MOD_ID);

    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> FACTION_CRAFTING =
            CATEGORIES.register("faction_crafting", RecipeBookCategory::new);
}
