package realcolin.whmod.client;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BrownBearModel extends QuadrupedModel<BrownBearRenderState> {
    private static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(
            true,
            16.0F,
            4.0F,
            2.25F,
            2.0F,
            24.0F,
            Set.of("head"));

    protected BrownBearModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(boolean isBaby) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 7.0F, 7.0F).texOffs(0, 44).addBox("mouth", -2.5F, 1.0F, -6.0F, 5.0F, 3.0F, 3.0F).texOffs(26, 0).addBox("right_ear", -4.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F).texOffs(26, 0).mirror().addBox("left_ear", 2.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 10.0F, -16.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14.0F, 14.0F, 11.0F).texOffs(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12.0F, 12.0F, 10.0F), PartPose.offsetAndRotation(-2.0F, 9.0F, 12.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
        int i = 10;
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F);
        partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-4.5F, 14.0F, 6.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(4.5F, 14.0F, 6.0F));
        CubeListBuilder cubelistbuilder1 = CubeListBuilder.create().texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F);
        partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder1, PartPose.offset(-3.5F, 14.0F, -8.0F));
        partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder1, PartPose.offset(3.5F, 14.0F, -8.0F));
        return LayerDefinition.create(meshdefinition, 128, 64).apply(isBaby ? BABY_TRANSFORMER : MeshTransformer.IDENTITY).apply(MeshTransformer.scaling(1.2F));
    }

    public void setupAnim(@NotNull BrownBearRenderState renderState) {
        super.setupAnim(renderState);
        float f = renderState.standScale * renderState.standScale;
        float f1 = renderState.ageScale;
        float f2 = renderState.isBaby ? 0.44444445F : 1.0F;
        ModelPart part = this.body;
        part.xRot -= f * (float)Math.PI * 0.35F;
        part = this.body;
        part.y += f * f1 * 2.0F;
        part = this.rightFrontLeg;
        part.y -= f * f1 * 20.0F;
        part = this.rightFrontLeg;
        part.z += f * f1 * 4.0F;
        part = this.rightFrontLeg;
        part.xRot -= f * (float)Math.PI * 0.45F;
        this.leftFrontLeg.y = this.rightFrontLeg.y;
        this.leftFrontLeg.z = this.rightFrontLeg.z;
        part = this.leftFrontLeg;
        part.xRot -= f * (float)Math.PI * 0.45F;
        part = this.head;
        part.y -= f * f2 * 24.0F;
        part = this.head;
        part.z += f * f2 * 13.0F;
        part = this.head;
        part.xRot += f * (float)Math.PI * 0.15F;
    }
}
