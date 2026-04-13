package realcolin.whmod.client.entity.renderer;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import realcolin.whmod.item.WHArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class WHArmorRenderer<R extends HumanoidRenderState & GeoRenderState> extends GeoArmorRenderer<WHArmorItem, R> {
    public WHArmorRenderer(ResourceLocation modelId) {
        super(new DefaultedItemGeoModel<>(modelId));
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot slot) {
        super.applyBoneVisibilityBySlot(slot);

        if (this.lastModel == null) {
            return;
        }

        this.lastModel.getBone("armorWaist").ifPresent(bone -> bone.setHidden(slot != EquipmentSlot.LEGS));
    }
}
