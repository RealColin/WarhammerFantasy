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
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.SelectFactionPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FactionSelectionScreen extends Screen {
    private final List<Faction> factions = Stream.of(Faction.values()).filter(f -> f != Faction.NONE).toList();
    private int selectedFactionIndex = 0;

    private Component factionDesc = factions.getFirst().description();
    private MultiLineTextWidget descWidget;
    private List<FormattedCharSequence> wrappedText = List.of();
    private int textScroll = 0;
    private int maxTextScroll = 0;

    private static final int SELECTION_PANEL_X = 30;
    private static final int SELECTION_PANEL_Y = 30;
    private static final int LEFT_PANEL_X = SELECTION_PANEL_X + 10;
    private static final int RIGHT_PANEL_X = SELECTION_PANEL_Y + 50;


    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "fac_bg");

    public FactionSelectionScreen() {
        super(Component.literal("Faction Selection"));
    }

    @Override
    protected void init() {
        super.init();
        descWidget = new MultiLineTextWidget(35, 80, factionDesc, this.font);
        descWidget.setMaxWidth(160);
        rebuildDescription();


        int centerX = this.width / 2;

        this.addRenderableWidget(
                Button.builder(getSelectedFactionName(), b -> {
                    selectedFactionIndex = (selectedFactionIndex + 1) % factions.size();
                    var selectedFac = factions.get(selectedFactionIndex);
                    b.setMessage(getSelectedFactionName());
                    factionDesc = selectedFac.description();
                    rebuildDescription();

                    descWidget.setMessage(factionDesc);

                }).bounds(centerX - 70, 55, 140, 20).build()
        );

//        this.addRenderableOnly(descWidget);


        this.addRenderableWidget(
                Button.builder(Component.literal("Confirm"), b ->
                        ClientPacketDistributor.sendToServer(new SelectFactionPayload(factions.get(selectedFactionIndex))))
                        .bounds(centerX - 70, this.height - 55, 140, 20).build()
        );
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        drawBackground(guiGraphics);
        drawSelectionBackground(guiGraphics);
        drawPanelBackgrounds(guiGraphics);
        drawScrollableText(guiGraphics);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 40, 0xFFFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isMouseOverDescPanel(mouseX, mouseY)) {
            int step = 12;
            textScroll -= (int)(scrollY * step);
            textScroll = Math.max(0, Math.min(textScroll, maxTextScroll));
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private Component getSelectedFactionName() {
        var selectedFaction = factions.get(selectedFactionIndex);
        return Component.literal(selectedFaction.name());
    }

    private boolean isMouseOverDescPanel(double mouseX, double mouseY) {
        int x = 40;
        int y = 80;
        int w = (this.width / 2 - 10) - 40;
        int h = (this.height - 65) - 80;

        return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
    }


    private void drawBackground(GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BG, 0, 0, this.width, this.height);
        guiGraphics.fill(0, 0, this.width, this.height, 0x66000000); // darken the background image
    }

    private void drawSelectionBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(
                SELECTION_PANEL_X,
                SELECTION_PANEL_Y,
                this.width - SELECTION_PANEL_X,
                this.height - SELECTION_PANEL_Y,
                0x88000000);
    }

    private void drawPanelBackgrounds(GuiGraphics guiGraphics) {
        guiGraphics.fill(40, 80, (this.width / 2) - 10, this.height - 65, 0xBB000000);
        guiGraphics.fill((this.width / 2) + 10, 80, this.width - 40, this.height - 65, 0xBB000000);
    }

    private void drawScrollableText(GuiGraphics guiGraphics) {
        int textX = 40 + 5;
        int textY = 80 + 5;
        int textW = ((this.width / 2 - 10) - (40)) - 10;
        int textH = ((this.height - 65) - (80)) - 10;

        guiGraphics.enableScissor(textX, textY, textX + textW, textY + textH);

        int y = textY - textScroll;
        for (var line : wrappedText) {
            guiGraphics.drawString(this.font, line, textX, y, 0xFFFFFFFF);
            y += this.font.lineHeight + 2;
        }

        guiGraphics.disableScissor();
    }

    private void rebuildDescription() {
        var lines = new ArrayList<FormattedCharSequence>();

        int panelWidth = (this.width / 2 - 10) - 40;
        int panelHeight = (this.height - 65) - 80;
        int textWidth = panelWidth - 10;

        lines.addAll(this.font.split(factionDesc, textWidth));
        this.wrappedText = lines;

        int totalTextHeight = lines.size() * (this.font.lineHeight + 2);
        int visibleHeight = panelHeight - 10;

        this.maxTextScroll = Math.max(0, totalTextHeight - visibleHeight);
        this.textScroll = Math.min(this.textScroll, this.maxTextScroll);
    }
}
