package realcolin.whmod.client.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import realcolin.whmod.entity.npc.NPC;

public class BeastmenModel<T extends NPC> extends HumanoidModel<BeastmenRenderState> {
    public BeastmenModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer(CubeDeformation deformation) {
        return LayerDefinition.create(createMesh(deformation), 64, 64);
    }

    public static MeshDefinition createMesh(CubeDeformation deformation) {
        var meshDef = HumanoidModel.createMesh(deformation, 0.0F);
        var partDef = meshDef.getRoot();

        var head = partDef.getChild("head");

        var left_ear = head.addOrReplaceChild("right_ear_r2", CubeListBuilder.create().texOffs(8, 61).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, -1.0F, 0.0F, 0.4363F, 0.0F));
        var right_ear = head.addOrReplaceChild("right_ear_r1", CubeListBuilder.create().texOffs(8, 61).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, -1.0F, 0.0F, -0.4363F, 0.0F));
        var left_horn = head.addOrReplaceChild("left_horn_r1", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 6.0F, 2.0F, 0.3491F, 0.0F, 0.3491F));
        var right_horn = head.addOrReplaceChild("right_horn_r1", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 6.0F, 2.0F, 0.3491F, 0.0F, -0.3491F));


        return meshDef;
    }
}
