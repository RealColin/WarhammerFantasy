package realcolin.whmod.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.recipe.FactionCraftingRecipe;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class FactionRecipeBuilder implements RecipeBuilder {
    private final Faction faction;
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final List<String> rows;
    private final Map<Character, Ingredient> key;
    private final RecipeUnlockAdvancementBuilder advancementBuilder;
    private @Nullable String group;
    private final boolean showNotification;

    private FactionRecipeBuilder(Faction faction, HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        this.faction = faction;
        this.rows = Lists.newArrayList();
        this.key = Maps.newLinkedHashMap();
        this.advancementBuilder = new RecipeUnlockAdvancementBuilder();
        this.showNotification = true;
        this.items = items;
        this.category = category;
        this.result = result;
    }

    private FactionRecipeBuilder(Faction faction, HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        this(faction, items, category, new ItemStackTemplate(result.asItem(), count));
    }

    public static FactionRecipeBuilder factionRecipe(Faction faction, HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) {
        return new FactionRecipeBuilder(faction, items, category, item, count);
    }

    public FactionRecipeBuilder pattern(String row) {
        if (!this.rows.isEmpty() && row.length() != (this.rows.getFirst()).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(row);
            return this;
        }
    }

    public FactionRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(item));
    }

    public FactionRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
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
    public void save(RecipeOutput output, @NonNull ResourceKey<Recipe<?>> id) {
        var pattern = ShapedRecipePattern.of(this.key, this.rows);
        var recipe = new FactionCraftingRecipe(faction, pattern, result);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}
