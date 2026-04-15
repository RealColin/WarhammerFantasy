package realcolin.whmod.worldgen.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.List;

public record MapEntry(String region, Integer color, Holder<Terrain> terrain, Climate.ParameterList<Holder<Biome>> biomes) {

    public static final MapCodec<Climate.ParameterList<Holder<Biome>>> BIOME_CODEC =
            Climate.ParameterList.codec(Biome.CODEC.fieldOf("biome")).fieldOf("biomes");

    public static final Codec<List<MapEntry>> ENTRIES_CODEC =
            RecordCodecBuilder.<MapEntry>create(instance -> instance.group(
                    Codec.STRING.fieldOf("region").forGetter(MapEntry::region),
                    Codec.INT.fieldOf("color").forGetter(MapEntry::color),
                    Terrain.CODEC.fieldOf("terrain").forGetter(MapEntry::terrain),
                    BIOME_CODEC.forGetter(MapEntry::biomes)
            ).apply(instance, MapEntry::new)).listOf();
}
