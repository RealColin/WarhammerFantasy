package realcolin.whmod.client.network;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import realcolin.whmod.WHMod;
import realcolin.whmod.client.WHClient;
import realcolin.whmod.client.screen.FactionSelectionScreen;
import realcolin.whmod.network.AttackStartedPayload;
import realcolin.whmod.network.CloseScreenPayload;
import realcolin.whmod.network.OpenFactionScreenPayload;

@SuppressWarnings("unused")
public class ClientPayloadHandler {

    public static void handleOpenFactionScreen(OpenFactionScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.gui.setScreen(new FactionSelectionScreen());
        });
    }

    public static void handleCloseScreen(CloseScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.gui.setScreen(null);
        });
    }

    public static void handleStartedAttack(AttackStartedPayload payload, IPayloadContext context) {
        String animName = payload.attackId();

        var entity = context.player().level().getEntity(payload.entityId());

        if (!(entity instanceof Avatar avatar))
            return;

        var layer = PlayerAnimationAccess.getPlayerAnimationLayer(avatar, WHClient.COMBAT_LAYER);

        if (!(layer instanceof PlayerAnimationController controller))
            return;

        if (!Double.isFinite(payload.attackSpeed()) || payload.attackSpeed() <= 0)
            return;

        controller.removeModifierIf(modifier -> modifier instanceof SpeedModifier);

        controller.addModifierLast(new SpeedModifier((float)payload.attackSpeed()));

        Identifier animationId = Identifier.fromNamespaceAndPath(WHMod.MOD_ID, payload.attackId());
        controller.triggerAnimation(animationId);
    }
}
