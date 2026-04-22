package realcolin.whmod.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;

import java.util.ArrayList;
import java.util.List;

public record FactionShapelessCraftingRecipe(Faction faction, List<Ingredient> ingredients,
                                             ItemStackTemplate result) implements FactionCraftingRecipe {

    public static final MapCodec<FactionShapelessCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            (i) -> i.group(
                    Faction.CODEC.fieldOf("faction").forGetter(FactionShapelessCraftingRecipe::faction),
                    Codec.lazyInitialized(
                            () -> Ingredient.CODEC.listOf(1, ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth())
                    ).fieldOf("ingredients").forGetter((o) -> o.ingredients),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter((o) -> o.result)
            ).apply(i, FactionShapelessCraftingRecipe::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, FactionShapelessCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Faction.STREAM_CODEC, FactionShapelessCraftingRecipe::faction,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), FactionShapelessCraftingRecipe::ingredients,
                    ItemStackTemplate.STREAM_CODEC, FactionShapelessCraftingRecipe::result,
                    FactionShapelessCraftingRecipe::new
            );

    @Override
    public boolean matches(CraftingInput craftingInput, @NonNull Level level) {
        if (craftingInput.ingredientCount() != ingredients.size())
            return false;
        else if (!ingredients.stream().allMatch(Ingredient::isSimple)) {
            var nonEmptyItems = new ArrayList<ItemStack>(craftingInput.ingredientCount());

            for (var item : craftingInput.items())
                if (!item.isEmpty())
                    nonEmptyItems.add(item);

            return RecipeMatcher.findMatches(nonEmptyItems, ingredients) != null;
        } else {
            return craftingInput.size() == 1 && this.ingredients.size() == 1 ?
                    (this.ingredients.getFirst()).test(craftingInput.getItem(0)) :
                    craftingInput.stackedContents().canCraft(this, null);
        }
    }

    @Override
    public @NonNull ItemStack assemble(CraftingInput craftingInput) {
        return result.create();
    }

    // TODO what for?
    @Override
    public boolean showNotification() {
        return false;
    }

    // TODO what for?
    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull RecipeSerializer<? extends FactionCraftingRecipe> getSerializer() {
        return WHRecipes.SHAPELESS_FACTION_CRAFTING.get();
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<CraftingInput>> getType() {
        return WHRecipes.FACTION_CRAFTING.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return WHRecipeBookCategories.FACTION_CRAFTING.get();
    }

    @Override
    public @NonNull List<RecipeDisplay> display() {
        // TODO change Items.CRAFTING_TABLE to whatever the faction crafting table is

        return List.of(
                new FactionShapelessCraftingRecipeDisplay(
                        faction,
                        this.ingredients.stream().map(Ingredient::display).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));

    }
}
