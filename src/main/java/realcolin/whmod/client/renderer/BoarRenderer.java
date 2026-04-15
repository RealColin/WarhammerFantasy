package realcolin.whmod.client.renderer;

import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.renderstate.BoarRenderState;
import realcolin.whmod.client.WHModelLayers;
import realcolin.whmod.client.model.BoarModel;
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
    public void extractRenderState(Boar entity, BoarRenderState state, float partialTicks) {
        this.model = models.getModel(state.isBaby);
        super.extractRenderState(entity, state, partialTicks);
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull BoarRenderState boarRenderState) {
        return Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "textures/entity/boar/boar.png");
    }

    @Override
    public @NotNull BoarRenderState createRenderState() {
        return new BoarRenderState();
    }
}
