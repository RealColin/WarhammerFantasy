package realcolin.whmod.item.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;

public record FactionCraftingRecipe(Faction faction, ShapedRecipePattern pattern,
                                    ItemStackTemplate result) implements Recipe<CraftingInput> {
    public static final MapCodec<FactionCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Faction.CODEC.fieldOf("faction").forGetter(FactionCraftingRecipe::faction),
                    ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result)
            ).apply(instance, FactionCraftingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FactionCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Faction.STREAM_CODEC, FactionCraftingRecipe::faction,
                    ShapedRecipePattern.STREAM_CODEC, FactionCraftingRecipe::pattern,
                    ItemStackTemplate.STREAM_CODEC, FactionCraftingRecipe::result,
                    FactionCraftingRecipe::new
            );

    @Override
    public boolean matches(CraftingInput craftingInput, @NonNull Level level) {
        return this.pattern.matches(craftingInput);
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
    public @NonNull RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
        return WHRecipes.FACTION_CRAFTING_S.get();
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
        return RecipeBookCategories.CRAFTING_MISC;
    }


}
