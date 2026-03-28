package realcolin.whmod.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import realcolin.whmod.entity.npc.NPC;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class NPCModel extends DefaultedEntityGeoModel<NPC> {
    public NPCModel(ResourceLocation assetPath) {
        super(assetPath, true);
    }
}
