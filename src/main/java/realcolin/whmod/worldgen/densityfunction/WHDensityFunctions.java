package realcolin.whmod.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class WHDensityFunctions {

    public static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTIONS =
            DeferredRegister.create(BuiltInRegistries.DENSITY_FUNCTION_TYPE, WHMod.MOD_ID);

    public static final Supplier<MapCodec<? extends DensityFunction>> NOISE = DENSITY_FUNCTIONS.register("noise", () -> Noise.CODEC);
    public static final Supplier<MapCodec<? extends DensityFunction>> SHIFTED_NOISE = DENSITY_FUNCTIONS.register("shifted_noise", () -> ShiftedNoise.CODEC);
    public static final Supplier<MapCodec<? extends DensityFunction>> MAP_SAMPLER = DENSITY_FUNCTIONS.register("map_sampler", () -> MapSampler.CODEC);
    public static final Supplier<MapCodec<? extends DensityFunction>> BLENDED_MAP_SAMPLER = DENSITY_FUNCTIONS.register("blended_map_sampler", () -> MapSamplerWithBlending.CODEC);
}
