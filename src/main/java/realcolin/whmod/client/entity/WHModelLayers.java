package realcolin.whmod.client.entity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import realcolin.whmod.WHMod;

public class WHModelLayers {
    public static final ModelLayerLocation BOAR = make("boar");
    public static final ModelLayerLocation BOAR_BABY = make ("boar_baby");

    public static final ModelLayerLocation BROWN_BEAR = make("brown_bear");
    public static final ModelLayerLocation BROWN_BEAR_BABY = make("brown_bear_baby");

    public static final ModelLayerLocation HUMAN = make("human_npc");
    public static final ModelLayerLocation HUMAN_SLIM = make("human_npc_slim");

    private static ModelLayerLocation make(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, name), "main");
    }

}
