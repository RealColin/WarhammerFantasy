package realcolin.whmod;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import realcolin.whmod.block.WHBlocks;

@SuppressWarnings("deprecation")
@Mod(value = WHMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = WHMod.MOD_ID, value = Dist.CLIENT)
public class WHClient {
    public WHClient(ModContainer container) {

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        for (var woodSet : WHBlocks.woodSets) {
            ItemBlockRenderTypes.setRenderLayer(woodSet.sapling().get(), ChunkSectionLayer.CUTOUT);
        }
    }

    @SubscribeEvent
    static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, tintIndex) ->
                        level != null && pos != null
                                ? BiomeColors.getAverageFoliageColor(level, pos)
                                : FoliageColor.FOLIAGE_DEFAULT,
                WHBlocks.PINE.leaves().get(),
                WHBlocks.BEECH.leaves().get(),
                WHBlocks.ELM.leaves().get()
        );
    }
}
