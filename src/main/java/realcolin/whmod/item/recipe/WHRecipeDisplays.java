package realcolin.whmod.item.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

public class WHRecipeDisplays {
    public static final DeferredRegister<RecipeDisplay.Type<?>> RECIPE_DISPLAYS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_DISPLAY, WHMod.MOD_ID);

    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<FactionShapedCraftingRecipeDisplay>> FACTION_SHAPED =
            RECIPE_DISPLAYS.register(
                    "faction_shaped",
                    () -> new RecipeDisplay.Type<>(
                            FactionShapedCraftingRecipeDisplay.MAP_CODEC,
                            FactionShapedCraftingRecipeDisplay.STREAM_CODEC
                    ));
}
