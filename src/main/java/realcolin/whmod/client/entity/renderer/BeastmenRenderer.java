package realcolin.whmod.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.client.entity.renderstate.BeastmenRenderState;
import realcolin.whmod.client.entity.WHModelLayers;
import realcolin.whmod.client.entity.model.BeastmenModel;
import realcolin.whmod.entity.npc.NPC;

public class BeastmenRenderer<T extends NPC> extends HumanoidMobRenderer<T, BeastmenRenderState, BeastmenModel<T>> {
    public BeastmenRenderer(EntityRendererProvider.Context context) {
        super(context, new BeastmenModel<>(context.bakeLayer(WHModelLayers.UNGOR)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(BeastmenRenderState state) {
        return state.textureLocation;
    }

    @Override
    public @NotNull BeastmenRenderState createRenderState() {
        return new BeastmenRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T entity, @NotNull BeastmenRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.textureLocation = entity.getTextureLocation();
    }
}
