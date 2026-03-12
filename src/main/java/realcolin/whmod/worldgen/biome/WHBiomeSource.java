package realcolin.whmod.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.Constants;
import realcolin.whmod.WHMod;
import realcolin.whmod.worldgen.map.WorldMap;

import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class WHBiomeSource extends BiomeSource {

    public static final MapCodec<WHBiomeSource> CODEC =
            RecordCodecBuilder.mapCodec(inst -> inst.group(
                    WorldMap.CODEC.fieldOf("map").forGetter(src -> src.map)
            ).apply(inst, inst.stable(WHBiomeSource::new)));

    private final Holder<WorldMap> map;

    public WHBiomeSource(Holder<WorldMap> map) {
        this.map = map;
    }

    @Override
    protected @NotNull MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull Stream<Holder<Biome>> collectPossibleBiomes() {
        return map.value().getAllBiomes().stream();
    }

    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int i, int i1, int i2, Climate.@NotNull Sampler sampler) {
        var entry = map.value().getEntryAt(i * 4, i2 * 4);

        if (entry == null)
            return map.value().getDefaultBiome();

        var params = entry.biomes();
        return params.findValue(sampler.sample(i, i1, i2));
    }

    public static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES =
            DeferredRegister.create(BuiltInRegistries.BIOME_SOURCE, WHMod.MOD_ID);

    public static final Supplier<MapCodec<? extends BiomeSource>> MAP_SOURCE =
            BIOME_SOURCES.register(Constants.MAP_BIOME_SOURCE_ID, () -> WHBiomeSource.CODEC);

}
