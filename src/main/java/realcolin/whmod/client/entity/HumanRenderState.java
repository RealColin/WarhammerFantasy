package realcolin.whmod.client.entity;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import realcolin.whmod.WHMod;

public class HumanRenderState extends HumanoidRenderState {
    public ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "textures/entity/npc/human/fallback.png");
    public boolean slim = false;
}
