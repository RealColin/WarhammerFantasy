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

public record FactionShapedCraftingRecipe(Faction faction, ShapedRecipePattern pattern,
                                          ItemStackTemplate result) implements FactionCraftingRecipe {
    public static final MapCodec<FactionShapedCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Faction.CODEC.fieldOf("faction").forGetter(FactionShapedCraftingRecipe::faction),
                    ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result)
            ).apply(instance, FactionShapedCraftingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FactionShapedCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Faction.STREAM_CODEC, FactionShapedCraftingRecipe::faction,
                    ShapedRecipePattern.STREAM_CODEC, FactionShapedCraftingRecipe::pattern,
                    ItemStackTemplate.STREAM_CODEC, FactionShapedCraftingRecipe::result,
                    FactionShapedCraftingRecipe::new
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
    public @NonNull RecipeSerializer<? extends FactionCraftingRecipe> getSerializer() {
        return WHRecipes.SHAPED_FACTION_CRAFTING.get();
    }
}
