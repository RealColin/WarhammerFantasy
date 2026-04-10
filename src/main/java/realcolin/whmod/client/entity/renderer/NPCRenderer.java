package realcolin.whmod.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.entity.model.NPCModel;
import realcolin.whmod.entity.npc.NPC;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemInHandGeoLayer;

import java.util.List;

public class NPCRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<NPC, R> {
    public NPCRenderer(EntityRendererProvider.Context context, String path) {
        super(context, new NPCModel(ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, path)));

        addRenderLayer(new ItemInHandGeoLayer<>(this));
        addRenderLayer(new ItemArmorGeoLayer<>(this, context) {
            private final List<RenderData> BONES = List.of(
                    RenderData.head("armor_head"),
                    RenderData.body("armor_body"),
                    new RenderData("armor_waist", EquipmentSlot.LEGS, model -> model.body),
                    RenderData.leftArm("armor_left_arm"),
                    RenderData.rightArm("armor_right_arm"),
                    RenderData.leftLeg("armor_left_leg"),
                    RenderData.rightLeg("armor_right_leg"),
                    RenderData.leftFoot("armor_left_boot"),
                    RenderData.rightFoot("armor_right_boot")
            );

            @Override
            protected List<RenderData> getRelevantBones(R renderState, BakedGeoModel model) {
                return BONES;
            }
        });
    }
}
