package realcolin.whmod.client.screen.menu;

import net.minecraft.client.gui.GuiGraphics;

public interface MenuSubScreen {
    void render(GuiGraphics g, int px, int py, int pw, int ph, int mouseX, int mouseY);

    String name();
}
