package realcolin.whmod.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;

import java.util.concurrent.CompletableFuture;

public class WHItemTagsProvider extends ItemTagsProvider {
    public WHItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WHMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        for (var woodSet : WHBlocks.woodSets) {
            this.tag(ItemTags.PLANKS).add(woodSet.planksItem().get());
        }
    }
}
