package realcolin.whmod.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.SelectFactionPayload;

import java.util.List;
import java.util.stream.Stream;

public class FactionSelectionScreen extends Screen {
    private final List<Faction> factions = Stream.of(Faction.values()).filter(f -> f != Faction.NONE).toList();
    private int selectedFactionIndex = 0;

    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "fac_bg");

    public FactionSelectionScreen() {
        super(Component.literal("Choose Your Faction"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int bottomY = this.height - 50;

        this.addRenderableWidget(
                Button.builder(getSelectedFactionName(), b -> {
                    selectedFactionIndex = (selectedFactionIndex + 1) % factions.size();
                    b.setMessage(getSelectedFactionName());
                }).bounds(centerX - 100, this.height / 2 - 20, 200, 20).build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("Confirm"), b ->
                        ClientPacketDistributor.sendToServer(new SelectFactionPayload(factions.get(selectedFactionIndex))))
                        .bounds(centerX - 50, bottomY, 100, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BG, 0, 0, this.width, this.height);
        guiGraphics.fill(0, 0, this.width, this.height, 0x55000000);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 30, 0xFFFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private Component getSelectedFactionName() {
        var selectedFaction = factions.get(selectedFactionIndex);
        return Component.literal(selectedFaction.name());
    }
}
