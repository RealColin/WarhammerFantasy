package realcolin.whmod.data;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.recipe.FactionShapelessCraftingRecipe;

import java.util.ArrayList;
import java.util.List;

public class FactionShapelessRecipeBuilder implements RecipeBuilder {
    private final Faction faction;
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;

    private FactionShapelessRecipeBuilder(Faction faction, HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        this.faction = faction;
        this.items = items;
        this.category = category;
        this.result = result;
    }

    public static FactionShapelessRecipeBuilder factionRecipe(Faction faction, HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        return new FactionShapelessRecipeBuilder(faction, items, category, result);
    }

    public static FactionShapelessRecipeBuilder factionRecipe(Faction faction, HolderGetter<Item> items, RecipeCategory category, ItemLike item) {
        return new FactionShapelessRecipeBuilder(faction, items, category, new ItemStackTemplate(item.asItem(), 1));
    }

    public FactionShapelessRecipeBuilder requires(ItemLike item, int count) {
        for (int i = 0; i < count; i++) {
            this.requires(Ingredient.of(item));
        }

        return this;
    }

    public FactionShapelessRecipeBuilder requires(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(@NonNull String s, @NonNull Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(s, criterion);
        return this;
    }

    @Override
    public @NonNull RecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }

    @Override
    public @NonNull ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(@NonNull RecipeOutput recipeOutput, @NonNull ResourceKey<Recipe<?>> id) {
        var recipe = new FactionShapelessCraftingRecipe(faction, ingredients, result);
        recipeOutput.accept(id, recipe, this.advancementBuilder.build(recipeOutput, id, this.category));
    }
}
