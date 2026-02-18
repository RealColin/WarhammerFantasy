package realcolin.whmod.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.block.WHBlocks;

import java.util.Set;

public class BlockLoot extends BlockLootSubProvider {
    protected BlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        for (var woodSet : WHBlocks.woodSets) {
            dropSelf(woodSet.log().get());
            dropSelf(woodSet.wood().get());
            dropSelf(woodSet.strippedLog().get());
            dropSelf(woodSet.strippedWood().get());
            dropSelf(woodSet.planks().get());
            dropSelf(woodSet.slab().get());
            dropSelf(woodSet.stairs().get());
            dropSelf(woodSet.fence().get());
            dropSelf(woodSet.fenceGate().get());
            dropSelf(woodSet.button().get());
            dropSelf(woodSet.pressurePlate().get());
            dropSelf(woodSet.sapling().get());
            dropSelf(woodSet.door().get());
            dropSelf(woodSet.trapdoor().get());

            add(woodSet.leaves().get(), createLeavesDrops(
                    woodSet.leaves().get(),
                    woodSet.sapling().get(),
                    NORMAL_LEAVES_SAPLING_CHANCES
            ));
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return WHBlocks.BLOCKS.getEntries()
                .stream()
                .map(h -> (Block) h.get())
                .toList();
    }
}
