package realcolin.whmod.client.screen.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CharacterSubScreen implements MenuSubScreen {

    private static final int BUFFER = 10;
    private static final int TITLE_HEIGHT = 20;
    private static final int BOTTOM_HEIGHT = 40;

    private static final int BG = 0x44444444;

    private static final ResourceLocation HEART = ResourceLocation.fromNamespaceAndPath("minecraft", "hud/heart/full");
    private static final ResourceLocation WINDS = ResourceLocation.fromNamespaceAndPath("minecraft", "hud/air");
    private static final ResourceLocation ARMOR = ResourceLocation.fromNamespaceAndPath("minecraft", "hud/armor_full");

    private Player player;

    private float modelYaw = 180.0F;
    private float modelPitch = 0.0F;
    private boolean draggingModel = false;
    private int modelBoxX1, modelBoxY1, modelBoxX2, modelBoxY2;

    public CharacterSubScreen(Player player) {
        this.player = player;
    }

    @Override
    public void render(GuiGraphics g, int px, int py, int pw, int ph, int mouseX, int mouseY, Font font) {
        renderBackground(g, px, py, pw, ph);
        renderTitlePanel(g, px, py, pw, ph, font);
        renderMiddle(g, px, py, pw, ph, font, mouseX, mouseY);
        renderBottomPanel(g, px, py, pw, ph, font);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isInsideModelBox(mouseX, mouseY)) {
            draggingModel = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingModel) {
            draggingModel = false;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && draggingModel) {
            modelYaw += (float) dragX * 2.0F;
            modelPitch -= (float) dragY * 1.5F;

            modelPitch = Mth.clamp(modelPitch, -60.0F, 60.0F);
            return true;
        }

        return false;
    }

    @Override
    public String name() {
        return "Character";
    }

    private boolean isInsideModelBox(double mouseX, double mouseY) {
        return mouseX >= modelBoxX1 && mouseX < modelBoxX2
                && mouseY >= modelBoxY1 && mouseY < modelBoxY2;
    }

    private void renderBackground(GuiGraphics g, int px, int py, int pw, int ph) {
        int x = px + BUFFER;
        int y = py + BUFFER;
        int w = pw - 2 * BUFFER;
        int h = ph - 2 * BUFFER;

        g.fill(x, y, x + w, y + h, BG);
    }

    private void renderTitlePanel(GuiGraphics g, int px, int py, int pw, int ph, Font font) {
        int x = px + BUFFER;
        int y = py + BUFFER;
        int w = pw - 2 * BUFFER;
        int h = TITLE_HEIGHT;

        var name = player.getName();
        int textWidth = font.width(name);
        int textX = x + (w - textWidth) / 2;

        g.fill(x, y, x + w, y + h, 0xAA9F7622);
        g.drawString(font, player.getName(), textX, y + 6, 0xFFFFFFFF);
    }

    private void renderMiddle(GuiGraphics g, int px, int py, int pw, int ph, Font font, int mouseX, int mouseY) {
        int x = px + BUFFER;
        int y = py + BUFFER + TITLE_HEIGHT;
        int w = pw - 2 * BUFFER;
        int bottom = (py + ph) - (BOTTOM_HEIGHT + BUFFER);
        int h = bottom - y;

        g.fill(x, y, x + w, y + h, 0x66000000);

        int ex = x;
        int ey = y;
        int eh = h;
        int ew = eh / 2;

        modelBoxX1 = ex;
        modelBoxY1 = ey;
        modelBoxX2 = ex + ew;
        modelBoxY2 = ey + eh;

        int scaleFromHeight = (int) (eh * 0.45f);
        int scaleFromWidth = (int) (ew * 0.80f);
        int scale = Math.min(scaleFromHeight, scaleFromWidth);

//        InventoryScreen.renderEntityInInventoryFollowsMouse(g, ex, ey, ex + ew, ey + eh, scale, 0.0625F, mouseX, mouseY, player);

        Quaternionf rotation = new Quaternionf()
                .rotateZ((float) Math.PI)
                .rotateX((float) Math.toRadians(modelPitch))
                .rotateY((float) Math.toRadians(modelYaw));

        InventoryScreen.renderEntityInInventory(
                g,
                ex,
                ey,
                ex + ew,
                ey + eh,
                (float)scale / player.getScale(),
                new Vector3f(0.0F, player.getBbHeight() / 2.0F + 0.0625F * player.getScale(), 0.0F),
                rotation,
                null,
                player
        );

        int dx = ex + ew + BUFFER;
        int dy = y;
        int dw = (x + w) - dx;
        int dh = h;

        int tx = dx + 4;
        int ty = dy + 8;

        String temp = "Hi this is a temporary String";
        g.drawString(font, temp, tx, ty, 0xFFFFFFFF);

    }

    private void renderBottomPanel(GuiGraphics g, int px, int py, int pw, int ph, Font font) {
        int w = pw - 2 * BUFFER;
        int h = BOTTOM_HEIGHT;
        int x = px + BUFFER;
        int y = (py + ph) - (h + BUFFER);

        g.fill(x, y, x + w, y + h, 0x66666666); // background color

        // health info
        var health = player.getHealth();
        var maxHealth = player.getMaxHealth();
        var healthText = Mth.floor(health) + "/" + Mth.floor(maxHealth);

        int hx = x + 8;
        int heartIconX = hx + font.width(healthText) + 2;
        int heartIconY = y + 14;

        g.drawString(font, healthText, hx, y + 16, 0xFFFFFFFF);
        g.blitSprite(RenderPipelines.GUI_TEXTURED, HEART, heartIconX, heartIconY, 12, 12);

        // winds of magic info
        int winds = 0;
        int maxWinds = 0;
        var windsText = winds + "/" + maxWinds;

        int wx = hx + font.width(healthText) + 2 + 12 + 20;
        int windsIconX = wx + font.width(windsText) + 2;
        int windsIconY = y + 14;

        g.drawString(font, windsText, wx, y + 16, 0xFFFFFFFF);
        g.blitSprite(RenderPipelines.GUI_TEXTURED, WINDS, windsIconX, windsIconY, 12, 12);


        // armor info
        var armor = player.getArmorValue();
        var armorText = String.valueOf(armor);

        int ax = wx + font.width(windsText) + 2 + 12 + 20;
        int armorIconX = ax + font.width(armorText) + 2;
        int armorIconY = y + 14;

        g.drawString(font, armorText, ax, y + 16, 0xFFFFFFFF);
        g.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR, armorIconX, armorIconY, 12, 12);
    }


}
