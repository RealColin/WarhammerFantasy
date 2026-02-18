package realcolin.whmod.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import realcolin.whmod.WHMod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = WHMod.MOD_ID)
public class WHDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        System.out.println("DATA GENERATED");
        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(true, new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));

        event.createProvider(WHModelProvider::new);
        event.createProvider(WHLanguageProvider::new);
        event.createProvider(WHBlockTagsProvider::new);
    }
}
