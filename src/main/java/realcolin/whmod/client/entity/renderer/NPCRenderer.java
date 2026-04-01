package realcolin.whmod.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.entity.model.NPCModel;
import realcolin.whmod.entity.npc.NPC;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.ItemInHandGeoLayer;

public class NPCRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<NPC, R> {
    public NPCRenderer(EntityRendererProvider.Context context, String path) {
        super(context, new NPCModel(ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, path)));

        addRenderLayer(new ItemInHandGeoLayer<>(this));
    }
}
