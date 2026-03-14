package realcolin.whmod.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.SelectFactionPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NewFactionSelectionScreen extends Screen {
    private final List<Faction> factions = Stream.of(Faction.values()).filter(f -> f != Faction.NONE).toList();
    private int selectedFactionIndex = 0;

    private static final int LIST_X = 10;
    private static final int LIST_Y = 10;
    private static final int LIST_W = 142;
    private static final int DESC_Y = 10;
    private static final int LIST_ROW_HEIGHT = 24;
    private static final int LIST_INSET = 8;
    private static final int DESC_INSET = 8;
    private static final int SCROLLBAR_W = 6;
    private static final int TEXT_SPACING = 3;
    private static final int BORDER_WIDTH = 2;

    private static final int PANEL_BG = 0x88000000;
    private static final int PANEL_BORDER = 0x66AC3400;
    private static final int SELECTED_ROW = 0xAA9F7622;
    private static final int HOVERED_ROW = 0x66666666;
    private static final int ROW = 0x44444444;
    private static final int TEXT = 0xFFFFFFFF;
    private static final int SHADED_TEXT = 0xFFAAAAAA;

    private int listW;
    private int listH;
    private int descX;
    private int descW;
    private int descH;

    private int listScroll = 0;
    private int maxListScroll = 0;

    private List<FormattedCharSequence> descText = List.of();
    private int infoScroll = 0;
    private int maxInfoScroll = 0;

    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "fac_bg");

    public NewFactionSelectionScreen() {
        super(Component.literal("hi"));
    }

    @Override
    protected void init() {
        super.init();
        listW = LIST_W;
        listH = (this.height - 30) - LIST_Y;
        descX = LIST_X + listW + 10;
        descW = (this.width - 10) - descX;
        descH = (this.height - 30) - DESC_Y;

        rebuildFactListScrollBounds();
        rebuildFactionDesc();

        Button confirmSelectionButton = Button.builder(Component.literal("Select Faction"), b ->
                        ClientPacketDistributor.sendToServer(new SelectFactionPayload(factions.get(selectedFactionIndex))))
                .bounds(this.width / 2 - 60, this.height - 25, 120, 20).build();

        this.addRenderableWidget(confirmSelectionButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // screen background
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BG, 0, 0, this.width, this.height);
        guiGraphics.fill(0, 0, this.width, this.height, 0x55000000); // darken the background image a bit

        drawFactionList(guiGraphics, mouseX, mouseY);
        drawDescription(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isMouseOverList(mouseX, mouseY)) {
            int contentX = LIST_X + LIST_INSET;
            int contentY = LIST_Y + LIST_INSET;

            int localY = (int) mouseY - contentY + listScroll;
            int index = localY / LIST_ROW_HEIGHT;

            if (index >= 0 && index < factions.size()) {
                selectedFactionIndex = index;
                infoScroll = 0;
                rebuildFactionDesc();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int step = 18;

        if (isMouseOverList(mouseX, mouseY)) {
            listScroll -= (int) (scrollY * step);
            listScroll = Math.max(0, Math.min(listScroll, maxListScroll));
            return true;
        }

        if (isMouseOverDetails(mouseX, mouseY)) {
            infoScroll -= (int) (scrollY * step);
            infoScroll = Math.max(0, Math.min(infoScroll, maxInfoScroll));
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        listW = LIST_W;
        listH = (height - 30) - LIST_Y;
        descX = LIST_X + listW + 10;
        descW = (this.width - 10) - descX;
        descH = (this.height - 30) - DESC_Y;
    }

    private boolean isMouseOverList(double mouseX, double mouseY) {
        return mouseX >= LIST_X && mouseX < LIST_X + listW
                && mouseY >= LIST_Y && mouseY < LIST_Y + listH;
    }

    private boolean isMouseOverDetails(double mouseX, double mouseY) {
        return mouseX >= descX && mouseX < descX + descW
                && mouseY >= DESC_Y && mouseY < DESC_Y + descH;
    }

    private void drawFactionList(GuiGraphics g, int mouseX, int mouseY) {
        g.fill(LIST_X, LIST_Y, listW + LIST_X, this.height - 30, PANEL_BG); // faction list panel background

        int b = BORDER_WIDTH;
        g.fill(LIST_X, LIST_Y, LIST_X + b, this.height - 30, PANEL_BORDER); // left
        g.fill(LIST_X + b, LIST_Y, listW + LIST_X - b, LIST_Y + b, PANEL_BORDER); // top
        g.fill((listW + LIST_X) - b, LIST_Y, listW + LIST_X, this.height - 30, PANEL_BORDER); // right
        g.fill(LIST_X + b, (this.height - 30) - b, listW + LIST_X - b, this.height - 30, PANEL_BORDER); // bottom

        int contentX = LIST_X + LIST_INSET;
        int contentY = LIST_Y + LIST_INSET;
        int contentW = listW - LIST_INSET * 2 - SCROLLBAR_W - 4;
        int contentH = listH - LIST_INSET * 2;

        g.enableScissor(contentX, contentY, contentX + contentW, contentY + contentH);

        for (int i = 0; i < factions.size(); i++) {
            int rowY = contentY + i * LIST_ROW_HEIGHT - listScroll;
            int rowBottom = rowY + LIST_ROW_HEIGHT - 2;

            if (rowBottom < contentY || rowY > contentY + contentH) {
                continue;
            }

            boolean selected = i == selectedFactionIndex;
            boolean hovered = mouseX >= contentX && mouseX < contentX + contentW
                    && mouseY >= rowY && mouseY < rowY + LIST_ROW_HEIGHT;

            int rowColor = selected ? SELECTED_ROW : hovered ? HOVERED_ROW : ROW;
            g.fill(contentX, rowY, contentX + contentW, rowY + LIST_ROW_HEIGHT - 2, rowColor);

            var name = factions.get(i).name();
            int textWidth = this.font.width(name);
            int textX = contentX + (contentW - textWidth) / 2;

            g.drawString(
                    this.font,
                    name,
                    textX,
                    rowY + 7,
                    selected ? TEXT : SHADED_TEXT
            );
        }

        g.disableScissor();
        drawListScrollbar(g, contentX + contentW + 4, contentY, SCROLLBAR_W, contentH);
    }

    private void drawListScrollbar(GuiGraphics g, int x, int y, int w, int h) {
        int contentHeight = factions.size() * LIST_ROW_HEIGHT;
        if (contentHeight <= 0 || contentHeight <= h) return;

        g.fill(x, y, x + w, y + h, ROW);

        int thumbH = Math.max(16, (int) ((h / (double) contentHeight) * h));
        int thumbTravel = h - thumbH;
        int thumbY = y + (maxListScroll == 0 ? 0 : (int) ((listScroll / (double) maxListScroll) * thumbTravel));

        g.fill(x, thumbY, x + w, thumbY + thumbH, SELECTED_ROW);
    }

    private void drawDescription(GuiGraphics g) {
        g.fill(descX, DESC_Y, this.width - 10, this.height - 30, PANEL_BG); // faction desc panel
        int b = BORDER_WIDTH;
        g.fill(descX, DESC_Y, descX + b, this.height - 30, PANEL_BORDER); // left
        g.fill(descX + b, DESC_Y, descX + descW - b, DESC_Y + b, PANEL_BORDER); // top
        g.fill(descX + descW - b, DESC_Y, descX + descW, this.height - 30, PANEL_BORDER); // right
        g.fill(descX + b, this.height - 30 - b, descX + descW - b, this.height - 30, PANEL_BORDER); // bottom


        int contentX = descX + DESC_INSET;
        int contentY = DESC_Y + DESC_INSET;
        int contentW = descW - DESC_INSET * 2 - SCROLLBAR_W - 4;
        int contentH = descH - DESC_INSET * 2;

        g.enableScissor(contentX, contentY, contentX + contentW, contentY + contentH);

        int y = contentY - infoScroll;
        for (var line : descText) {
            g.drawString(this.font, line, contentX, y, TEXT);
            y += this.font.lineHeight + TEXT_SPACING;
        }

        g.disableScissor();
        drawDescriptionScrollbar(g, contentX + contentW + 4, contentY, SCROLLBAR_W, contentH);
    }

    private void drawDescriptionScrollbar(GuiGraphics g, int x, int y, int w, int h) {
        int contentHeight = descText.size() * (this.font.lineHeight + TEXT_SPACING);
        if (contentHeight <= 0 || contentHeight <= h) return;

        g.fill(x, y, x + w, y + h, ROW);

        int thumbH = Math.max(16, (int) ((h / (double) contentHeight) * h));
        int thumbTravel = h - thumbH;
        int thumbY = y + (maxInfoScroll == 0 ? 0 : (int) ((infoScroll / (double) maxInfoScroll) * thumbTravel));

        g.fill(x, thumbY, x + w, thumbY + thumbH, SELECTED_ROW);
    }

    private void rebuildFactListScrollBounds() {
        int contentHeight = factions.size() * LIST_ROW_HEIGHT;
        int visibleHeight = listH - LIST_INSET * 2;
        maxListScroll = Math.max(0, contentHeight - visibleHeight);
        listScroll = Math.min(listScroll, maxListScroll);
    }

    private void rebuildFactionDesc() {
        var desc = factions.get(selectedFactionIndex).description();

        int contentW = descW - DESC_INSET * 2 - SCROLLBAR_W - 4;

        var lines = new ArrayList<FormattedCharSequence>();
        lines.addAll(this.font.split(desc, contentW));
        this.descText = lines;

        int totalTextHeight = lines.size() * (this.font.lineHeight + TEXT_SPACING);
        int visibleHeight = descH - DESC_INSET * 2;

        this.maxInfoScroll = Math.max(0, totalTextHeight - visibleHeight);
        this.infoScroll = Math.min(this.infoScroll, this.maxInfoScroll);
    }
}
