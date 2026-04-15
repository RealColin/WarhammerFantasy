package realcolin.whmod.item;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.renderer.WHArmorRenderer;
import java.util.function.Consumer;

public class WHArmorItem extends Item implements GeoItem {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WHArmorItem(ArmorMaterial material, ArmorType type, Properties properties) {
        super(properties.humanoidArmor(material, type));
    }

    @Override
    public void registerControllers(AnimatableManager.@NonNull ControllerRegistrar controllers) {

    }

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private WHArmorRenderer<?> renderer;

            @Override
            public @NonNull GeoArmorRenderer<?, ?> getGeoArmorRenderer(@NonNull ItemStack itemStack, @NonNull EquipmentSlot equipmentSlot) {
                if (renderer == null) {
                    renderer = new WHArmorRenderer<>(Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "armor/state_troop_armor"));
                }

                return renderer;
            }
        });
    }
}
