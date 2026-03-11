package realcolin.whmod;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import realcolin.whmod.block.WHBlocks;
import realcolin.whmod.client.BoarModel;
import realcolin.whmod.client.BoarRenderer;
import realcolin.whmod.client.BrownBearRenderer;
import realcolin.whmod.client.ClientPayloadHandler;
import realcolin.whmod.entity.WHEntities;
import realcolin.whmod.entity.animal.Boar;
import realcolin.whmod.network.CloseScreenPayload;
import realcolin.whmod.network.OpenFactionScreenPayload;

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

        EntityRenderers.register(WHEntities.BOAR.get(), BoarRenderer::new);
        EntityRenderers.register(WHEntities.BROWN_BEAR.get(), BrownBearRenderer::new);
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

    @SubscribeEvent
    static void registerClientPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(OpenFactionScreenPayload.TYPE, HandlerThread.NETWORK, ClientPayloadHandler::handleOpenFactionScreen);
        event.register(CloseScreenPayload.TYPE, HandlerThread.NETWORK, ClientPayloadHandler::handleCloseScreen);
    }

//    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.<Boar>registerEntityRenderer(WHEntities.BOAR.get(), BoarRenderer::new);
//    }

//    @SubscribeEvent
//    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
//        event.registerLayerDefinition(BoarModel.LAYER_LOCATION, BoarModel::createBodyLayer);
//    }



}
