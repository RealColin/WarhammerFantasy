package realcolin.whmod.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import realcolin.whmod.client.screen.FactionSelectionScreen;
import realcolin.whmod.client.screen.NewFactionSelectionScreen;
import realcolin.whmod.network.CloseScreenPayload;
import realcolin.whmod.network.OpenFactionScreenPayload;

public class ClientPayloadHandler {

    public static void handleOpenFactionScreen(OpenFactionScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
//            mc.setScreen(new FactionSelectionScreen());
            mc.setScreen(new NewFactionSelectionScreen());
        });
    }

    public static void handleCloseScreen(CloseScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(null);
        });
    }
}
