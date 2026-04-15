package realcolin.whmod.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import realcolin.whmod.client.screen.FactionSelectionScreen;
import realcolin.whmod.network.CloseScreenPayload;
import realcolin.whmod.network.OpenFactionScreenPayload;

@SuppressWarnings("unused")
public class ClientPayloadHandler {

    public static void handleOpenFactionScreen(OpenFactionScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(new FactionSelectionScreen());
        });
    }

    public static void handleCloseScreen(CloseScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(null);
        });
    }
}
