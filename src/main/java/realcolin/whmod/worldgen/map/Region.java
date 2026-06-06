package realcolin.whmod.worldgen.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import realcolin.whmod.WHRegistries;

public record Region(String region, Integer color, Holder<Terrain> terrain, Climate.ParameterList<Holder<Biome>> biomes) {

    public static final MapCodec<Climate.ParameterList<Holder<Biome>>> BIOME_CODEC =
            Climate.ParameterList.codec(Biome.CODEC.fieldOf("biome")).fieldOf("biomes");

    public static final Codec<Region> DIRECT_CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Region::region),
                    Codec.INT.fieldOf("color").forGetter(Region::color),
                    Terrain.CODEC.fieldOf("terrain").forGetter(Region::terrain),
                    BIOME_CODEC.forGetter(Region::biomes)
            ).apply(instance, Region::new));

    public static final Codec<Region> NETWORK_CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Region::region),
                    Codec.INT.fieldOf("color").forGetter(Region::color)
            ).apply(instance, Region::clientOnly));


    public static final Codec<Holder<Region>> CODEC = RegistryFileCodec.create(WHRegistries.REGION, DIRECT_CODEC);

    private static Region clientOnly(String name, Integer color) {
        return new Region(name, color, null, null);
    }
}
