package realcolin.whmod.client.screen.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class MapSubScreen implements MenuSubScreen{
    @Override
    public void render(GuiGraphicsExtractor g, int px, int py, int pw, int ph, int mouseX, int mouseY, Font font) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    @Override
    public String name() {
        return "Map";
    }
}
