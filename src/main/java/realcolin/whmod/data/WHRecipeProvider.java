package realcolin.whmod.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;

import java.util.concurrent.CompletableFuture;

public class WHRecipeProvider extends RecipeProvider {
    private final HolderGetter<Item> items;

    protected WHRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    protected void buildRecipes() {
        for (var woodSet : WHBlocks.woodSets) {
            // logs to planks
            ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, woodSet.planksItem(), 4)
                    .requires(woodSet.logItem())
                    .unlockedBy("has_log", has(woodSet.logItem()))
                    .save(this.output, recipeKey(woodSet.name() + "_planks_from_log"));
            ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, woodSet.planksItem(), 4)
                    .requires(woodSet.strippedLogItem())
                    .unlockedBy("has_stripped_log", has(woodSet.strippedLogItem()))
                    .save(this.output, recipeKey(woodSet.name() + "_planks_from_stripped_log"));
            ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, woodSet.planksItem(), 4)
                    .requires(woodSet.woodItem())
                    .unlockedBy("has_wood", has(woodSet.woodItem()))
                    .save(this.output, recipeKey(woodSet.name() + "_planks_from_wood"));
            ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, woodSet.planksItem(), 4)
                    .requires(woodSet.strippedWoodItem())
                    .unlockedBy("has_stripped_wood", has(woodSet.strippedWoodItem()))
                    .save(this.output, recipeKey(woodSet.name() + "_planks_from_stripped_wood"));


            // stairs
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, woodSet.stairsItem(), 4)
                    .pattern("P  ")
                    .pattern("PP ")
                    .pattern("PPP")
                    .define('P', woodSet.planksItem())
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);

            // slabs
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, woodSet.slabItem(), 6)
                    .pattern("PPP")
                    .define('P', woodSet.planksItem())
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);

            // door
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, woodSet.doorItem(), 3)
                    .pattern("PP")
                    .pattern("PP")
                    .pattern("PP")
                    .define('P', woodSet.planksItem())
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);

            // trapdoor
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, woodSet.trapdoorItem(), 2)
                    .pattern("PPP")
                    .pattern("PPP")
                    .define('P', woodSet.planksItem())
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);

            // fence
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, woodSet.fenceItem(), 3)
                    .pattern("PSP")
                    .pattern("PSP")
                    .define('P', woodSet.planksItem())
                    .define('S', Items.STICK)
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);

            // fence gate
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.BUILDING_BLOCKS, woodSet.fenceGateItem(), 3)
                    .pattern("SPS")
                    .pattern("SPS")
                    .define('P', woodSet.planksItem())
                    .define('S', Items.STICK)
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);

            // button
            ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.REDSTONE, woodSet.buttonItem())
                    .requires(woodSet.planksItem())
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);

            // pressure plate
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.REDSTONE, woodSet.pressurePlateItem())
                    .pattern("PP")
                    .define('P', woodSet.planksItem())
                    .unlockedBy("has_planks", has(woodSet.planksItem()))
                    .save(this.output);
        }
    }

    private ResourceKey<Recipe<?>> recipeKey(String path) {
        return ResourceKey.create(
                Registries.RECIPE,
                ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, path)
        );
    }

    public static class Runner extends RecipeProvider.Runner {

        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new WHRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return "";
        }
    }
}
