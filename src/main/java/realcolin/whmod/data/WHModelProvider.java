package realcolin.whmod.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.FoliageColor;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;

public class WHModelProvider extends ModelProvider {
    public WHModelProvider(PackOutput output) {
        super(output, WHMod.MOD_ID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        for (var woodSet : WHBlocks.woodSets) {
            var log = woodSet.log();
            blockModels.woodProvider(log.get()).log(log.get());

            var wood = woodSet.wood();
            blockModels.woodProvider(log.get()).wood(wood.get());

            var strippedLog = woodSet.strippedLog();
            blockModels.woodProvider(strippedLog.get()).log(strippedLog.get());

            var stippedWood = woodSet.strippedWood();
            blockModels.woodProvider(strippedLog.get()).wood(stippedWood.get());

            blockModels.family(woodSet.planks().get())
                    .slab(woodSet.slab().get())
                    .stairs(woodSet.stairs().get())
                    .fence(woodSet.fence().get())
                    .fenceGate(woodSet.fenceGate().get())
                    .button(woodSet.button().get())
                    .pressurePlate(woodSet.pressurePlate().get())

                    .door(woodSet.door().get())
                    .trapdoor(woodSet.trapdoor().get());

            var leaves = woodSet.leaves();
            blockModels.createTintedLeaves(leaves.get(), TexturedModel.LEAVES, FoliageColor.FOLIAGE_DEFAULT);

            var sapling = woodSet.sapling();
            blockModels.createCrossBlock(sapling.get(), BlockModelGenerators.PlantType.NOT_TINTED);
            blockModels.registerSimpleFlatItemModel(sapling.get());
        }
    }
}
