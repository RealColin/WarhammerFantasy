package realcolin.whmod.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import realcolin.whmod.WHMod;

public class WHDimensions {
    public static final ResourceKey<Level> MALLUS =
            ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "mallus"));
}
