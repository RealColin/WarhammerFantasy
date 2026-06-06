package realcolin.whmod;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import realcolin.whmod.worldgen.map.Region;
import realcolin.whmod.worldgen.map.Terrain;
import realcolin.whmod.worldgen.map.WorldMap;

public class WHRegistries {
    public static final ResourceKey<Registry<Terrain>> TERRAIN = ResourceKey.createRegistryKey(Identifier.parse("worldgen/terrain"));
    public static final ResourceKey<Registry<Region>> REGION = ResourceKey.createRegistryKey(Identifier.parse("worldgen/region"));
    public static final ResourceKey<Registry<WorldMap>> MAP = ResourceKey.createRegistryKey(Identifier.parse("worldgen/map"));
}
