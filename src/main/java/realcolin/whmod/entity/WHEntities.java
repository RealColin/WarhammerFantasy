package realcolin.whmod.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;
import realcolin.whmod.entity.animal.Boar;

import java.util.function.Supplier;

public class WHEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(WHMod.MOD_ID);

    public static final Supplier<EntityType<Boar>> BOAR = ENTITY_TYPES.register("boar", () ->
            EntityType.Builder.of(Boar::new, MobCategory.CREATURE)
                    .sized(0.9F, 0.9F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "boar"))));
}
