package realcolin.whmod.item.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

public class WHRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, WHMod.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, WHMod.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<FactionCraftingRecipe>> FACTION_CRAFTING =
            RECIPE_TYPES.register("faction_crafting", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return WHMod.MOD_ID + ":faction_crafting";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FactionShapedCraftingRecipe>> SHAPED_FACTION_CRAFTING =
            RECIPE_SERIALIZERS.register(
                    "shaped_faction_crafting",
                    () -> new RecipeSerializer<>(FactionShapedCraftingRecipe.CODEC, FactionShapedCraftingRecipe.STREAM_CODEC)
            );

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FactionShapelessCraftingRecipe>> SHAPELESS_FACTION_CRAFTING =
            RECIPE_SERIALIZERS.register(
                    "shapeless_faction_crafting",
                    () -> new RecipeSerializer<>(FactionShapelessCraftingRecipe.CODEC, FactionShapelessCraftingRecipe.STREAM_CODEC)
            );
}
