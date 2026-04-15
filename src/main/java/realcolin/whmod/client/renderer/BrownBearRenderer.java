package realcolin.whmod.client.renderer;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.renderstate.BrownBearRenderState;
import realcolin.whmod.client.WHModelLayers;
import realcolin.whmod.client.model.BrownBearModel;
import realcolin.whmod.entity.animal.BrownBear;

@SuppressWarnings("deprecation")
public class BrownBearRenderer extends AgeableMobRenderer<BrownBear, BrownBearRenderState, BrownBearModel> {
    public BrownBearRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BrownBearModel(ctx.bakeLayer(WHModelLayers.BROWN_BEAR)), new BrownBearModel(ctx.bakeLayer(WHModelLayers.BROWN_BEAR_BABY)), 0.9F);
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull BrownBearRenderState brownBearRenderState) {
        return Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "textures/entity/bear/brown_bear.png");
    }

    @Override
    public @NotNull BrownBearRenderState createRenderState() {
        return new BrownBearRenderState();
    }
}
