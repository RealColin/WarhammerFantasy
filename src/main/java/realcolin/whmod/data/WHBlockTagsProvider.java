package realcolin.whmod.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockItemTags;
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
        var logsThatBurn = tag(BlockItemTags.LOGS_THAT_BURN.block());
        var goatHorn = tag(BlockTags.SNAPS_GOAT_HORN);
        var planks = tag(BlockTags.PLANKS);
        var stairs = tag(BlockTags.STAIRS);
        var slabs = tag(BlockTags.SLABS);
        var leaves = tag(BlockTags.LEAVES);
        var woodenStairs = tag(BlockTags.WOODEN_STAIRS);
        var woodenSlabs = tag(BlockTags.WOODEN_SLABS);


        var mineableHoe = tag(BlockTags.MINEABLE_WITH_HOE);

        for (var woodSet : WHBlocks.woodSets) {
            logs.add(woodSet.log().getKey());
            logsThatBurn.add(woodSet.log().getKey());
            goatHorn.add(woodSet.log().getKey());

            logs.add(woodSet.strippedLog().getKey());
            logsThatBurn.add(woodSet.strippedLog().getKey());

            logs.add(woodSet.wood().getKey());
            logsThatBurn.add(woodSet.wood().getKey());

            logs.add(woodSet.strippedWood().getKey());
            logsThatBurn.add(woodSet.strippedWood().getKey());

            planks.add(woodSet.planks().getKey());
            stairs.add(woodSet.stairs().getKey());
            woodenStairs.add(woodSet.stairs().getKey());
            slabs.add(woodSet.slab().getKey());
            woodenSlabs.add(woodSet.slab().getKey());

            leaves.add(woodSet.leaves().getKey());
            mineableHoe.add(woodSet.leaves().getKey());
        }
    }
}
