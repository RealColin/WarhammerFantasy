package realcolin.whmod.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.SelectFactionPayload;

import java.util.List;
import java.util.stream.Stream;

public class NewFactionSelectionScreen extends Screen {
    private final List<Faction> factions = Stream.of(Faction.values()).filter(f -> f != Faction.NONE).toList();
    private int selectedFactionIndex = 0;

    private Button confirmSelectionButton;

    private static final int LIST_X = 10;
    private static final int LIST_Y = 10;
    private static final int DESC_Y = 10;
    private static final int LIST_ROW_HEIGHT = 24;
    private static final int LIST_INSET = 8;
    private static final int SCROLLBAR_W = 6;

    private int listW;
    private int listH;
    private int descX;
    private int descW;
    private int descH;

    private int listScroll = 0;
    private int maxListScroll = 0;

    public NewFactionSelectionScreen() {
        super(Component.literal("hi"));
    }

    @Override
    protected void init() {
        super.init();
        listW = this.width / 3;
        listH = (this.height - 30) - LIST_Y;
        descX = LIST_X + listW + 10;
        descW = (this.width - 10) - descX;
        descH = (this.height - 30) - DESC_Y;

        rebuildFactListScrollBounds();


        this.confirmSelectionButton = Button.builder(Component.literal("Select Faction"), b ->
                ClientPacketDistributor.sendToServer(new SelectFactionPayload(factions.get(selectedFactionIndex))))
                .bounds(this.width / 2 - 60, this.height - 25, 120, 20).build();

        this.addRenderableWidget(confirmSelectionButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0xFF000000); // screen-wide background
        drawFactionList(guiGraphics, mouseX, mouseY);
        guiGraphics.fill(descX, DESC_Y, this.width - 10, this.height - 30, 0xFF00FF00); // faction desc panel

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
//                infoScroll = 0;
//                rebuildWrappedText();
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

//        if (isMouseOverDetails(mouseX, mouseY)) {
//            infoScroll -= (int) (scrollY * step);
//            infoScroll = Math.max(0, Math.min(infoScroll, maxInfoScroll));
//            return true;
//        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        listW = width / 3;
        listH = (height - 30) - LIST_Y;
        descX = LIST_X + listW + 10;
        descW = (this.width - 10) - descX;
        descH = (this.height - 30) - DESC_Y;
    }

    private boolean isMouseOverList(double mouseX, double mouseY) {
        return mouseX >= LIST_X && mouseX < LIST_X + listW
                && mouseY >= LIST_Y && mouseY < LIST_Y + listH;
    }

    private void drawFactionList(GuiGraphics g, int mouseX, int mouseY) {
        g.fill(LIST_X, LIST_Y, listW + LIST_X, this.height - 30, 0xFFFF0000); // faction list panel background

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

            int rowColor = selected ? 0xAA7A5A2A : hovered ? 0x66444444 : 0x44222222;
            g.fill(contentX, rowY, contentX + contentW, rowY + LIST_ROW_HEIGHT - 2, rowColor);

            var name = factions.get(i).name();
            int textWidth = this.font.width(name);
            int textX = contentX + (contentW - textWidth) / 2;

            g.drawString(
                    this.font,
                    name,
                    textX,
                    rowY + 7,
                    selected ? 0xFFFFFFFF : 0xFFE0D2B8
            );
        }

        g.disableScissor();
        drawListScrollbar(g, contentX + contentW + 4, contentY, SCROLLBAR_W, contentH);
    }

    private void drawListScrollbar(GuiGraphics g, int x, int y, int w, int h) {
        int contentHeight = factions.size() * LIST_ROW_HEIGHT;
        if (contentHeight <= 0 || contentHeight <= h) return;

        g.fill(x, y, x + w, y + h, 0x66303030);

        int thumbH = Math.max(16, (int) ((h / (double) contentHeight) * h));
        int thumbTravel = h - thumbH;
        int thumbY = y + (maxListScroll == 0 ? 0 : (int) ((listScroll / (double) maxListScroll) * thumbTravel));

        g.fill(x, thumbY, x + w, thumbY + thumbH, 0xCCB08A54);
    }

    private void rebuildFactListScrollBounds() {
        int contentHeight = factions.size() * LIST_ROW_HEIGHT;
        int visibleHeight = listH - LIST_INSET * 2;
        maxListScroll = Math.max(0, contentHeight - visibleHeight);
        listScroll = Math.min(listScroll, maxListScroll);
    }
}
