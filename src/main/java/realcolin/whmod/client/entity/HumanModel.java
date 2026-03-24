package realcolin.whmod.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.entity.npc.NPC;

@SuppressWarnings("unused")
public class HumanModel<T extends NPC> extends HumanoidModel<HumanRenderState> {
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;
    private final boolean slim;

    public HumanModel(ModelPart root, boolean slim) {
        super(root);
        this.slim = slim;
        this.leftSleeve = this.leftArm.getChild("left_sleeve");
        this.rightSleeve = this.rightArm.getChild("right_sleeve");
        this.leftPants = this.leftLeg.getChild("left_pants");
        this.rightPants = this.rightLeg.getChild("right_pants");
        this.jacket = this.body.getChild("jacket");
    }

    public static LayerDefinition createLayer(CubeDeformation cubeDeformation) {
        return LayerDefinition.create(createMesh(cubeDeformation, false), 64, 64);
    }

    public static LayerDefinition createSlimLayer(CubeDeformation cubeDeformation) {
        return LayerDefinition.create(createMesh(cubeDeformation, true), 64, 64);
    }

    public static MeshDefinition createMesh(CubeDeformation deformation, boolean slim) {
        var meshDef = HumanoidModel.createMesh(deformation, 0.0F);
        var partDef = meshDef.getRoot();

        if (slim) {
            var leftArm = partDef.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F, 0.0F));
            var rightArm = partDef.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation), PartPose.offset(-5.0F, 2.0F, 0.0F));
            leftArm.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
            rightArm.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        } else {
            var leftArm = partDef.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F, 0.0F));
            var rightArm = partDef.getChild("right_arm");
            leftArm.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
            rightArm.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        }

        var leftLeg = partDef.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F, 0.0F));
        var rightLeg = partDef.getChild("right_leg");
        leftLeg.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        rightLeg.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        var body = partDef.getChild("body");
        body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation.extend(0.25F)), PartPose.ZERO);
        return meshDef;
    }

    @Override
    public void setupAnim(@NotNull HumanRenderState renderState) {
        super.setupAnim(renderState);
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm side, @NotNull PoseStack poseStack) {
        this.root().translateAndRotate(poseStack);
        ModelPart modelpart = this.getArm(side);
        if (this.slim) {
            float f = 0.5F * (float)(side == HumanoidArm.RIGHT ? 1 : -1);
            modelpart.x += f;
            modelpart.translateAndRotate(poseStack);
            modelpart.x -= f;
        } else {
            modelpart.translateAndRotate(poseStack);
        }

    }
}
