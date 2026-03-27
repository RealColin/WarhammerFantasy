package realcolin.whmod.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.entity.renderstate.BoarRenderState;
import realcolin.whmod.client.entity.WHModelLayers;
import realcolin.whmod.client.entity.model.BoarModel;
import realcolin.whmod.entity.animal.Boar;

public class BoarRenderer extends MobRenderer<Boar, BoarRenderState, BoarModel> {
    private final AdultAndBabyModelPair<BoarModel> models;

    public BoarRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BoarModel(ctx.bakeLayer(WHModelLayers.BOAR)), 0.25f);
        this.models = bakeModels(ctx);
    }

    private static AdultAndBabyModelPair<BoarModel> bakeModels(EntityRendererProvider.Context ctx) {
        return new AdultAndBabyModelPair<>(new BoarModel(ctx.bakeLayer(WHModelLayers.BOAR)), new BoarModel(ctx.bakeLayer(WHModelLayers.BOAR_BABY)));
    }


    @Override
    public void render(BoarRenderState renderState, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.model = models.getModel(renderState.isBaby);
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BoarRenderState boarRenderState) {
        return ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "textures/entity/boar/boar.png");
    }

    @Override
    public @NotNull BoarRenderState createRenderState() {
        return new BoarRenderState();
    }
}
