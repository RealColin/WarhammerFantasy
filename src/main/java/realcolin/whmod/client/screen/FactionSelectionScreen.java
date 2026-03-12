package realcolin.whmod.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.SelectFactionPayload;

import java.util.List;
import java.util.stream.Stream;

public class FactionSelectionScreen extends Screen {
    private final List<Faction> factions = Stream.of(Faction.values()).filter(f -> f != Faction.NONE).toList();
    private int selectedFactionIndex = 0;
    private Component factionDesc = factions.getFirst().description();
    private Component factionTraits = factions.getFirst().traits();

    private MultiLineTextWidget descWidget;
    private MultiLineTextWidget traitsWidget;

    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "fac_bg");

    public FactionSelectionScreen() {
        super(Component.literal("Faction Selection"));
    }

    @Override
    protected void init() {
        super.init();
        descWidget = new MultiLineTextWidget(this.width / 2, 80, factionDesc, this.font);
        traitsWidget = new MultiLineTextWidget(this.width / 2, 120, factionTraits, this.font);

        int centerX = this.width / 2;

        this.addRenderableWidget(
                Button.builder(getSelectedFactionName(), b -> {
                    selectedFactionIndex = (selectedFactionIndex + 1) % factions.size();
                    var selectedFac = factions.get(selectedFactionIndex);
                    b.setMessage(getSelectedFactionName());
                    factionDesc = selectedFac.description();
                    factionTraits = selectedFac.traits();
                    descWidget.setMessage(factionDesc);
                    traitsWidget.setMessage(factionTraits);
                }).bounds(centerX - 70, 55, 140, 20).build()
        );

        this.addRenderableOnly(descWidget);
        this.addRenderableOnly(traitsWidget);

        this.addRenderableWidget(
                Button.builder(Component.literal("Confirm"), b ->
                        ClientPacketDistributor.sendToServer(new SelectFactionPayload(factions.get(selectedFactionIndex))))
                        .bounds(centerX - 70, this.height - 55, 140, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BG, 0, 0, this.width, this.height);
        guiGraphics.fill(0, 0, this.width, this.height, 0x66000000); // darken the background image

        guiGraphics.fill(30, 30, this.width - 30, this.height - 30, 0x88000000);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 40, 0xFFFFFFFF);
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

    private void drawWrapped(GuiGraphics guiGraphics, Component text, int x, int y, int maxWidth, int color) {
        int yy = y;
        for (FormattedCharSequence line : this.font.split(text, maxWidth)) {
            guiGraphics.drawString(this.font, line, x, yy, color);
            yy += this.font.lineHeight + 2;
        }
    }
}
