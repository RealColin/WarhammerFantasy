package realcolin.whmod.client.renderer;

import com.geckolib.constant.DefaultAnimations;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.ItemArmorGeoLayer;
import com.geckolib.renderer.layer.builtin.ItemInHandGeoLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.model.NPCModel;
import realcolin.whmod.entity.npc.NPC;


import java.util.List;

public class NPCRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<NPC, @NonNull R> {
    public NPCRenderer(EntityRendererProvider.Context context, String path) {
        super(context, new NPCModel(Identifier.fromNamespaceAndPath(WHMod.MOD_ID, path)));

        withRenderLayer(new ItemInHandGeoLayer<>(context, this));
        withRenderLayer(new ItemArmorGeoLayer<>(this, context) {
            @Override
            protected @NonNull List<RenderData> getRelevantBones(@NonNull RenderPassInfo<@NonNull R> renderPassInfo) {
                return BONES;
            }

            private final List<RenderData> BONES = List.of(
                    RenderData.head("armor_head"),
                    RenderData.body("armor_body"),
                    new RenderData("armor_waist", GeoArmorRenderer.ArmorSegment.CHEST),
                    RenderData.leftArm("armor_left_arm"),
                    RenderData.rightArm("armor_right_arm"),
                    RenderData.leftLeg("armor_left_leg"),
                    RenderData.rightLeg("armor_right_leg"),
                    RenderData.leftFoot("armor_left_boot"),
                    RenderData.rightFoot("armor_right_boot")
            );
        });
    }

    @Override
    public void adjustModelBonesForRender(@NonNull RenderPassInfo<@NonNull R> renderPassInfo, @NonNull BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);
        DefaultAnimations.hardcodedHeadRotation(renderPassInfo, snapshots, "head");
    }
}
