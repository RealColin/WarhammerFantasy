package realcolin.whmod.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.entity.renderer.WHArmorRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.function.Consumer;

public class WHArmorItem extends Item implements GeoItem {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WHArmorItem(ArmorMaterial material, ArmorType type, Properties properties) {
        super(properties.humanoidArmor(material, type));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private WHArmorRenderer<?> renderer;

            @Override
            public <S extends HumanoidRenderState> @NotNull GeoArmorRenderer<?, ?> getGeoArmorRenderer(
                    @Nullable S renderState,
                    ItemStack itemStack,
                    EquipmentSlot equipmentSlot,
                    EquipmentClientInfo.LayerType type,
                    @Nullable HumanoidModel<S> original
            ) {

                if (renderer == null) {
                    renderer = new WHArmorRenderer<>(ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "armor/state_troop_armor"));
                }

                return renderer;
            }
        });
    }
}
