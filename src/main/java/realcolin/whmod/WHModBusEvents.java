package realcolin.whmod;

import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import realcolin.whmod.client.WHModelLayers;
import realcolin.whmod.client.model.BoarModel;
import realcolin.whmod.client.model.BrownBearModel;
import realcolin.whmod.entity.WHEntities;
import realcolin.whmod.entity.animal.Boar;
import realcolin.whmod.entity.animal.BrownBear;
import realcolin.whmod.entity.npc.NPCStats;
import realcolin.whmod.network.CloseScreenPayload;
import realcolin.whmod.network.OpenFactionScreenPayload;
import realcolin.whmod.network.SelectFactionPayload;
import realcolin.whmod.network.ServerPayloadHandler;
import realcolin.whmod.worldgen.map.Terrain;
import realcolin.whmod.worldgen.map.WorldMap;

@EventBusSubscriber(modid = WHMod.MOD_ID)
public class WHModBusEvents {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(OpenFactionScreenPayload.TYPE, OpenFactionScreenPayload.STREAM_CODEC);
        registrar.playToClient(CloseScreenPayload.TYPE, CloseScreenPayload.STREAM_CODEC);
        registrar.playToServer(SelectFactionPayload.TYPE, SelectFactionPayload.STREAM_CODEC, ServerPayloadHandler::handleSelectFactionPayload);
    }

    @SubscribeEvent
    public static void newDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(WHRegistries.TERRAIN, Terrain.DIRECT_CODEC);
        event.dataPackRegistry(WHRegistries.MAP, WorldMap.DIRECT_CODEC);
    }

    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(WHEntities.BOAR.get(), Boar.createAttributes().build());
        event.put(WHEntities.BROWN_BEAR.get(), BrownBear.createAttributes().build());
        event.put(WHEntities.IMPERIAL_SWORDSMAN.get(), NPCStats.defaultHumanAttributes().build());
        event.put(WHEntities.UNGOR.get(), NPCStats.defaultBeastmenAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(WHEntities.BOAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(WHEntities.BROWN_BEAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        var boarLayerDef = BoarModel.createBodyLayer(CubeDeformation.NONE);
        event.registerLayerDefinition(WHModelLayers.BOAR, () -> boarLayerDef);
        event.registerLayerDefinition(WHModelLayers.BOAR_BABY, () -> boarLayerDef.apply(BoarModel.BABY_TRANSFORMER));
        var brownBearLayerDef = BrownBearModel.createBodyLayer(false);
        var brownBearBabyLayerDef = BrownBearModel.createBodyLayer(true);
        event.registerLayerDefinition(WHModelLayers.BROWN_BEAR, () -> brownBearLayerDef);
        event.registerLayerDefinition(WHModelLayers.BROWN_BEAR_BABY, () -> brownBearBabyLayerDef);
    }

    @SubscribeEvent
    public static void addCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {

    }
}
