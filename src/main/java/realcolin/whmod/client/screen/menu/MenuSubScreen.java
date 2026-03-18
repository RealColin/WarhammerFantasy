package realcolin.whmod.client.screen.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface MenuSubScreen {
    void render(GuiGraphics g, int px, int py, int pw, int ph, int mouseX, int mouseY, Font font);
    boolean mouseClicked(double mouseX, double mouseY, int button);
    boolean mouseReleased(double mouseX, double mouseY, int button);
    boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY);

    String name();
}
