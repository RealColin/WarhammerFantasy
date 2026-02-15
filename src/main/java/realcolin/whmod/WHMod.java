package realcolin.whmod;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.block.WHBlocks;
import realcolin.whmod.worldgen.biome.WHBiomeSource;
import realcolin.whmod.worldgen.densityfunction.MapSampler;
import realcolin.whmod.worldgen.densityfunction.MapSamplerWithBlending;
import realcolin.whmod.worldgen.densityfunction.Noise;
import realcolin.whmod.worldgen.densityfunction.ShiftedNoise;
import realcolin.whmod.worldgen.map.Terrain;
import realcolin.whmod.worldgen.map.WorldMap;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(WHMod.MOD_ID)
public class WHMod {
    public static final String MOD_ID = "whmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    private static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(BuiltInRegistries.BIOME_SOURCE, MOD_ID);
    private static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTIONS = DeferredRegister.create(BuiltInRegistries.DENSITY_FUNCTION_TYPE, MOD_ID);


    public WHMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BIOME_SOURCES.register(Constants.MAP_BIOME_SOURCE_ID, () -> WHBiomeSource.CODEC);

        DENSITY_FUNCTIONS.register("noise", () -> Noise.CODEC);
        DENSITY_FUNCTIONS.register("shifted_noise", () -> ShiftedNoise.CODEC);
        DENSITY_FUNCTIONS.register("map_sampler", () -> MapSampler.CODEC);
        DENSITY_FUNCTIONS.register("blended_map_sampler", () -> MapSamplerWithBlending.CODEC);

        WHBlocks.BLOCKS.register(modEventBus);
        WHBlocks.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BIOME_SOURCES.register(modEventBus);
        DENSITY_FUNCTIONS.register(modEventBus);


        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerData);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    public void registerData(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(WHRegistries.TERRAIN, Terrain.DIRECT_CODEC);
        event.dataPackRegistry(WHRegistries.MAP, WorldMap.DIRECT_CODEC);
    }
}
