package realcolin.whmod.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import realcolin.whmod.WHMod;

import java.util.Optional;

public class WHTreeGrowers {
    private static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_PINE_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "small_pine"));

    private static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_PINE_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "giant_pine"));

    private static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_BEECH_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "small_beech"));

    public static final TreeGrower PINE = new TreeGrower(WHMod.MOD_ID + ":pine", Optional.of(GIANT_PINE_TREE),
            Optional.of(SMALL_PINE_TREE), Optional.empty());

    public static final TreeGrower BEECH = new TreeGrower(WHMod.MOD_ID + ":beech", Optional.empty(),
            Optional.of(SMALL_BEECH_TREE), Optional.empty());
}
