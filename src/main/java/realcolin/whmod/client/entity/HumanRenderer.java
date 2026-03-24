package realcolin.whmod.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.entity.npc.Gender;
import realcolin.whmod.entity.npc.NPC;

public class HumanRenderer<T extends NPC> extends HumanoidMobRenderer<T, HumanRenderState, HumanModel<T>> {
    private final HumanModel<T> normalModel;
    private final HumanModel<T> slimModel;

    public HumanRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanModel<>(context.bakeLayer(WHModelLayers.HUMAN), false), 0.5F);
        this.normalModel = getModel();
        this.slimModel = new HumanModel<>(context.bakeLayer(WHModelLayers.HUMAN), true);
    }

    @Override
    public void render(HumanRenderState renderState, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.model = renderState.slim ? this.slimModel : this.normalModel;
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(HumanRenderState humanoidRenderState) {
        return humanoidRenderState.textureLocation;
    }

    @Override
    public @NotNull HumanRenderState createRenderState() {
        return new HumanRenderState();
    }

    @Override
    public void extractRenderState(@NotNull T entity, @NotNull HumanRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.textureLocation = entity.getTextureLocation();
        state.slim = entity.getGender() != Gender.MALE;
    }
}
