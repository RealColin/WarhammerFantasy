package realcolin.whmod.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.client.screen.menu.CharacterSubScreen;
import realcolin.whmod.client.screen.menu.FactionSubScreen;
import realcolin.whmod.client.screen.menu.MapSubScreen;
import realcolin.whmod.client.screen.menu.MenuSubScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("PointlessArithmeticExpression")
public class InGameMenuScreen extends Screen {

    private static final int BUFFER = 10;
    private static final int INSET = 8;
    private static final int BORDER = 2;
    private static final int TEXT_SPACING = 3;
    private static final int SCROLLBAR_W = 6;

    private static final int SIDEBAR_X = 0 + BUFFER;
    private static final int SIDEBAR_Y = 0 + BUFFER;
    private static final int SIDEBAR_W = 142;
    private static final int ROW_HEIGHT = 24;
    private static final int CONTENT_Y = 0 + BUFFER;

    private static final int PANEL_BG = 0x88000000;
    private static final int PANEL_BORDER = 0x66AC3400;
    private static final int SELECTED_ROW = 0xAA9F7622;
    private static final int HOVERED_ROW = 0x66666666;
    private static final int ROW = 0x44444444;
    private static final int TEXT = 0xFFFFFFFF;
    private static final int SHADED_TEXT = 0xFFAAAAAA;

    private int sidebarH;
    private int contentX;
    private int contentW;
    private int contentH;

    private ArrayList<MenuSubScreen> subScreens;
    private int selectedIndex = 0;

    public InGameMenuScreen() {
        super(Component.literal("Menu"));
    }

    @Override
    protected void init() {
        super.init();

        subScreens = new ArrayList<>();
        subScreens.add(new MapSubScreen());
        subScreens.add(new FactionSubScreen());
        subScreens.add(new CharacterSubScreen(this.minecraft.player));

        sidebarH = (this.height - BUFFER) - SIDEBAR_Y;
        contentX = SIDEBAR_X + SIDEBAR_W + BUFFER;
        contentW = (this.width - BUFFER) - contentX;
        contentH = (this.height - BUFFER) - CONTENT_Y;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // TODO render background

        drawSidebar(guiGraphics, mouseX, mouseY);
        drawContentPanel(guiGraphics, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        sidebarH = (this.height - 30) - SIDEBAR_Y;
        contentX = SIDEBAR_X + SIDEBAR_W + BUFFER;
        contentW = (this.width - BUFFER) - contentX;
        contentH = (this.height - BUFFER) - CONTENT_Y;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isMouseOverSidebar(mouseX, mouseY)) {

            int cy = SIDEBAR_Y + INSET;

            int y = (int)mouseY - cy;
            int index = y / ROW_HEIGHT;

            if (index >= 0 && index < subScreens.size()) {
                selectedIndex = index;

                return true;
            }
        } else if (button == 0 && isMouseOverContent(mouseX, mouseY)) {
            var sub = subScreens.get(selectedIndex);
            return sub.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        var sub = subScreens.get(selectedIndex);
        return sub.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        var sub = subScreens.get(selectedIndex);
        return sub.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private boolean isMouseOverSidebar(double mouseX, double mouseY) {
        return mouseX >= SIDEBAR_X && mouseX < SIDEBAR_X + SIDEBAR_W
                && mouseY >= SIDEBAR_Y && mouseY < SIDEBAR_Y + sidebarH;
    }

    private boolean isMouseOverContent(double mouseX, double mouseY) {
        return mouseX >= contentX && mouseX < contentX + contentW
                && mouseY >= CONTENT_Y && mouseY < CONTENT_Y + contentH;
    }

    private void drawSidebar(GuiGraphics g, int mouseX, int mouseY) {
        g.fill(SIDEBAR_X, SIDEBAR_Y, SIDEBAR_X + SIDEBAR_W, this.height - BUFFER, PANEL_BG);

        int b = BORDER;
        g.fill(SIDEBAR_X, SIDEBAR_Y, SIDEBAR_X + b, this.height - BUFFER, PANEL_BORDER); // left
        g.fill(SIDEBAR_X + b, SIDEBAR_Y, SIDEBAR_W + SIDEBAR_X - b, SIDEBAR_Y + b, PANEL_BORDER); // top
        g.fill((SIDEBAR_W + SIDEBAR_X) - b, SIDEBAR_Y, SIDEBAR_W + SIDEBAR_X, this.height - BUFFER, PANEL_BORDER); // right
        g.fill(SIDEBAR_X + b, (this.height - BUFFER) - b, SIDEBAR_W + SIDEBAR_X - b, this.height - BUFFER, PANEL_BORDER); // bottom

        int cx = SIDEBAR_X + INSET;
        int cy = SIDEBAR_Y + INSET;
        int cw = SIDEBAR_W - INSET * 2;
        int ch = sidebarH - INSET * 2;

        g.enableScissor(cx, cy, cx + cw, cy + ch);

        for (int i = 0; i < subScreens.size(); i++) {
            int rowY = cy + i * ROW_HEIGHT;
            int rowBottom = rowY + ROW_HEIGHT - 2;

            if (rowBottom < cy || rowY > cy + ch) {
                continue;
            }

            boolean selected = i == selectedIndex;
            boolean hovered = mouseX >= cx && mouseX < cx + cw && mouseY >= rowY && mouseY < rowY + ROW_HEIGHT;

            int rowColor = selected ? SELECTED_ROW : hovered ? HOVERED_ROW : ROW;
            g.fill(cx, rowY, cx + cw, rowY + ROW_HEIGHT - 2, rowColor);

            var name = subScreens.get(i).name();
            int textWidth = this.font.width(name);
            int textX = cx + (cw - textWidth) / 2;

            g.drawString(
                    this.font,
                    name,
                    textX,
                    rowY + 7,
                    selected ? TEXT : SHADED_TEXT
            );
        }

        g.disableScissor();
    }

    private void drawContentPanel(GuiGraphics g, int mouseX, int mouseY) {
        g.fill(contentX, CONTENT_Y, contentX + contentW, this.height - BUFFER, PANEL_BG);

        int b = BORDER;
        g.fill(contentX, CONTENT_Y, contentX + b, this.height - BUFFER, PANEL_BORDER); // left
        g.fill(contentX + b, CONTENT_Y, contentW + contentX - b, CONTENT_Y + b, PANEL_BORDER); // top
        g.fill((contentW + contentX) - b, CONTENT_Y, contentW + contentX, this.height - BUFFER, PANEL_BORDER); // right
        g.fill(contentX + b, (this.height - BUFFER) - b, contentW + contentX - b, this.height - BUFFER, PANEL_BORDER); // bottom

        var content = subScreens.get(selectedIndex);

        content.render(g, contentX, CONTENT_Y, contentW, contentH, mouseX, mouseY, this.font);
    }
}
