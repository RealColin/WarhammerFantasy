package realcolin.whmod.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import realcolin.whmod.WHMod;

public class WHDimensions {
    public static final ResourceKey<Level> MALLUS =
            ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "mallus"));
}
