package realcolin.whmod.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import realcolin.whmod.WHMod;

public class WHModelLayers {
    public static final ModelLayerLocation BOAR = make("boar");
    public static final ModelLayerLocation BOAR_BABY = make ("boar_baby");

    private static ModelLayerLocation make(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, name), "main");
    }

}
