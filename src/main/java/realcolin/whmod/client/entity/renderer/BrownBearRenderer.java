package realcolin.whmod.client.entity.renderer;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.entity.renderstate.BrownBearRenderState;
import realcolin.whmod.client.entity.WHModelLayers;
import realcolin.whmod.client.entity.model.BrownBearModel;
import realcolin.whmod.entity.animal.BrownBear;

@SuppressWarnings("deprecation")
public class BrownBearRenderer extends AgeableMobRenderer<BrownBear, BrownBearRenderState, BrownBearModel> {
    public BrownBearRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BrownBearModel(ctx.bakeLayer(WHModelLayers.BROWN_BEAR)), new BrownBearModel(ctx.bakeLayer(WHModelLayers.BROWN_BEAR_BABY)), 0.9F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BrownBearRenderState brownBearRenderState) {
        return ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "textures/entity/bear/brown_bear.png");
    }

    @Override
    public @NotNull BrownBearRenderState createRenderState() {
        return new BrownBearRenderState();
    }
}
