package realcolin.whmod.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;

import java.util.concurrent.CompletableFuture;

public class WHBlockTagsProvider extends BlockTagsProvider {
    public WHBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WHMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var logs = tag(BlockTags.LOGS);
        var logsThatBurn = tag(BlockTags.LOGS_THAT_BURN);
        var goatHorn = tag(BlockTags.SNAPS_GOAT_HORN);
        var planks = tag(BlockTags.PLANKS);
        var stairs = tag(BlockTags.STAIRS);
        var slabs = tag(BlockTags.SLABS);
        var leaves = tag(BlockTags.LEAVES);
        var woodenStairs = tag(BlockTags.WOODEN_STAIRS);
        var woodenSlabs = tag(BlockTags.WOODEN_SLABS);


        var mineableHoe = tag(BlockTags.MINEABLE_WITH_HOE);

        for (var woodSet : WHBlocks.woodSets) {
            logs.add(woodSet.log().get());
            logsThatBurn.add(woodSet.log().get());
            goatHorn.add(woodSet.log().get());

            logs.add(woodSet.strippedLog().get());
            logsThatBurn.add(woodSet.strippedLog().get());

            logs.add(woodSet.wood().get());
            logsThatBurn.add(woodSet.wood().get());

            logs.add(woodSet.strippedWood().get());
            logsThatBurn.add(woodSet.strippedWood().get());

            planks.add(woodSet.planks().get());
            stairs.add(woodSet.stairs().get());
            woodenStairs.add(woodSet.stairs().get());
            slabs.add(woodSet.slab().get());
            woodenSlabs.add(woodSet.slab().get());

            leaves.add(woodSet.leaves().get());
            mineableHoe.add(woodSet.leaves().get());
        }
    }
}
