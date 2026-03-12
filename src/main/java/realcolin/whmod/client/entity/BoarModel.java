package realcolin.whmod.client.entity;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.Set;

public class BoarModel extends QuadrupedModel<BoarRenderState> {
	public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false, 4.0F, 4.0F, Set.of("head"));

	public BoarModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
		return LayerDefinition.create(createBaseBoarModel(cubeDeformation), 64, 64);
	}

	protected static MeshDefinition createBaseBoarModel(CubeDeformation cubeDeformation) {
		MeshDefinition meshdefinition = QuadrupedModel.createBodyMesh(6, true, false, cubeDeformation);
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 0).addBox(-3.0F, 2.0F, -10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(-3.0F, 1.0F, -11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 0).addBox(2.0F, 2.0F, -10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(2.0F, 1.0F, -11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, -6.0F));
		return meshdefinition;
	}
}