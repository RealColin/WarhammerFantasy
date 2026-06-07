package realcolin.whmod.client.screen.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

@SuppressWarnings("unused")
public class CharacterSubScreen implements MenuSubScreen {

    private static final int BUFFER = 10;
    private static final int TITLE_HEIGHT = 22;
    private static final int BOTTOM_HEIGHT = 42;

    private static final int BG = 0x44444444;
    private static final int TITLE_BG = 0xAA9F7622;
    private static final int MIDDLE_BG = 0x66000000;
    private static final int BOTTOM_BG = 0x66666666;

    private static final int COLUMN_HEADER_BG = 0x66332211;
    private static final int COLUMN_BG = 0x33000000;
    private static final int COLUMN_BORDER = 0x669F7622;

    private static final int TEXT = 0xFFFFFFFF;
    private static final int MUTED_TEXT = 0xFFCCCCCC;

    private static final Identifier HEART = Identifier.fromNamespaceAndPath("minecraft", "hud/heart/full");
    private static final Identifier WINDS = Identifier.fromNamespaceAndPath("minecraft", "hud/air");
    private static final Identifier ARMOR = Identifier.fromNamespaceAndPath("minecraft", "hud/armor_full");

    private final Player player;

    public CharacterSubScreen(Player player) {
        this.player = player;
    }

    @Override
    public void render(GuiGraphicsExtractor g, int px, int py, int pw, int ph, int mouseX, int mouseY, Font font) {
        renderBackground(g, px, py, pw, ph);
        renderTitlePanel(g, px, py, pw, ph, font);
        renderMiddle(g, px, py, pw, ph, font);
        renderBottomPanel(g, px, py, pw, ph, font);
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
        return "Character";
    }

    private void renderBackground(GuiGraphicsExtractor g, int px, int py, int pw, int ph) {
        int x = px + BUFFER;
        int y = py + BUFFER;
        int w = pw - 2 * BUFFER;
        int h = ph - 2 * BUFFER;

        g.fill(x, y, x + w, y + h, BG);
    }

    private void renderTitlePanel(GuiGraphicsExtractor g, int px, int py, int pw, int ph, Font font) {
        int x = px + BUFFER;
        int y = py + BUFFER;
        int w = pw - 2 * BUFFER;

        var name = player.getName();
        int textWidth = font.width(name);
        int textX = x + (w - textWidth) / 2;

        g.fill(x, y, x + w, y + TITLE_HEIGHT, TITLE_BG);
        g.text(font, name, textX, y + 7, TEXT);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private void renderMiddle(GuiGraphicsExtractor g, int px, int py, int pw, int ph, Font font) {
        int x = px + BUFFER;
        int y = py + BUFFER + TITLE_HEIGHT;
        int w = pw - 2 * BUFFER;
        int bottom = (py + ph) - (BOTTOM_HEIGHT + BUFFER);
        int h = bottom - y;

        g.fill(x, y, x + w, y + h, MIDDLE_BG);

        int innerPadding = 8;
        int gap = 8;

        int contentX = x + innerPadding;
        int contentY = y + innerPadding;
        int contentW = w - innerPadding * 2;
        int contentH = h - innerPadding * 2;

        int columnW = (contentW - gap * 2) / 3;

        int abilitiesX = contentX;
        int passivesX = abilitiesX + columnW + gap;
        int titlesX = passivesX + columnW + gap;

        List<String> abilities = getAbilityNames();
        List<String> passives = getPassiveNames();
        List<String> titles = getTitleNames();

        renderColumn(g, font, "Abilities", abilities, abilitiesX, contentY, columnW, contentH);
        renderColumn(g, font, "Passives", passives, passivesX, contentY, columnW, contentH);
        renderColumn(g, font, "Titles", titles, titlesX, contentY, columnW, contentH);
    }

    private void renderColumn(
            GuiGraphicsExtractor g,
            Font font,
            String header,
            List<String> entries,
            int x,
            int y,
            int w,
            int h
    ) {
        int headerH = 20;

        g.fill(x, y, x + w, y + h, COLUMN_BG);

        // Border
        g.fill(x, y, x + w, y + 1, COLUMN_BORDER);
        g.fill(x, y + h - 1, x + w, y + h, COLUMN_BORDER);
        g.fill(x, y, x + 1, y + h, COLUMN_BORDER);
        g.fill(x + w - 1, y, x + w, y + h, COLUMN_BORDER);

        // Header
        g.fill(x, y, x + w, y + headerH, COLUMN_HEADER_BG);

        int headerTextX = x + (w - font.width(header)) / 2;
        g.text(font, header, headerTextX, y + 6, TEXT);

        int entryX = x + 6;
        int entryY = y + headerH + 8;
        int lineHeight = 12;

        if (entries.isEmpty()) {
            g.text(font, "None", entryX, entryY, MUTED_TEXT);
            return;
        }

        int maxVisibleEntries = Math.max(0, (h - headerH - 12) / lineHeight);

        for (int i = 0; i < entries.size() && i < maxVisibleEntries; i++) {
            String entry = entries.get(i);

            // Trim text so it does not overflow the column.
            entry = trimToWidth(font, entry, w - 12);

            int color = i == 0 ? TEXT : MUTED_TEXT;
            g.text(font, entry, entryX, entryY + i * lineHeight, color);
        }

        if (entries.size() > maxVisibleEntries) {
            String moreText = "+" + (entries.size() - maxVisibleEntries) + " more";
            g.text(font, moreText, entryX, y + h - 12, 0xFFAAAAAA);
        }
    }

    private String trimToWidth(Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text;
        }

        String suffix = "...";
        int suffixWidth = font.width(suffix);

        while (!text.isEmpty() && font.width(text) + suffixWidth > maxWidth) {
            text = text.substring(0, text.length() - 1);
        }

        return text + suffix;
    }

    private void renderBottomPanel(GuiGraphicsExtractor g, int px, int py, int pw, int ph, Font font) {
        int w = pw - 2 * BUFFER;
        int h = BOTTOM_HEIGHT;
        int x = px + BUFFER;
        int y = (py + ph) - (h + BUFFER);

        g.fill(x, y, x + w, y + h, BOTTOM_BG);

        int statX = x + 10;
        int statY = y + 16;
        int maxX = x + w - 10;
        int gap = 14;

        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        String healthText = Mth.floor(health) + "/" + Mth.floor(maxHealth);

        statX = renderIconStatClipped(g, font, statX, statY, maxX, gap, healthText, HEART);
        if (statX == -1) return;

        int armor = player.getArmorValue();
        String armorText = String.valueOf(armor);

        statX = renderIconStatClipped(g, font, statX, statY, maxX, gap, armorText, ARMOR);
        if (statX == -1) return;

        int winds = getCurrentWinds();
        int maxWinds = getMaxWinds();
        String windsText = winds + "/" + maxWinds;

        statX = renderIconStatClipped(g, font, statX, statY, maxX, gap, windsText, WINDS);
        if (statX == -1) return;

        statX = renderTextStatClipped(g, font, statX, statY, maxX, gap, "Lvl", String.valueOf(getCharacterLevel()));
        if (statX == -1) return;

        renderTextStatClipped(g, font, statX, statY, maxX, gap, "XP", getXpText());
    }

    private int renderIconStatClipped(
            GuiGraphicsExtractor g,
            Font font,
            int x,
            int y,
            int maxX,
            int gap,
            String text,
            Identifier icon
    ) {
        int textWidth = font.width(text);
        int width = textWidth + 3 + 12;

        if (x + width > maxX) {
            return -1;
        }

        g.text(font, text, x, y, TEXT);

        int iconX = x + textWidth + 3;
        int iconY = y - 2;

        g.blitSprite(RenderPipelines.GUI_TEXTURED, icon, iconX, iconY, 12, 12);

        return x + width + gap;
    }

    private int renderTextStatClipped(
            GuiGraphicsExtractor g,
            Font font,
            int x,
            int y,
            int maxX,
            int gap,
            String label,
            String value
    ) {
        String text = label + ": " + value;
        int width = font.width(text);

        if (x + width > maxX) {
            return -1;
        }

        g.text(font, text, x, y, TEXT);

        return x + width + gap;
    }

    private int renderIconStat(
            GuiGraphicsExtractor g,
            Font font,
            int x,
            int y,
            String text,
            Identifier icon
    ) {
        g.text(font, text, x, y, TEXT);

        int iconX = x + font.width(text) + 3;
        int iconY = y - 2;

        g.blitSprite(RenderPipelines.GUI_TEXTURED, icon, iconX, iconY, 12, 12);

        return iconX + 12;
    }

    private int renderTextStat(
            GuiGraphicsExtractor g,
            Font font,
            int x,
            int y,
            String label,
            String value
    ) {
        String text = label + ": " + value;
        g.text(font, text, x, y, TEXT);
        return x + font.width(text);
    }

    // ---------------------------------------------------------------------
    // Temporary data hooks.
    // Replace these later with my actual character/player capability data.
    // ---------------------------------------------------------------------

    private List<String> getAbilityNames() {
        return List.of(
                "To be added..."
        );
    }

    private List<String> getPassiveNames() {
        return List.of(
                "To be added..."
        );
    }

    private List<String> getTitleNames() {
        return List.of(
                "To be added..."
        );
    }

    private int getCurrentWinds() {
        return 0;
    }

    private int getMaxWinds() {
        return 0;
    }

    private int getCharacterLevel() {
        return 1;
    }

    private String getXpText() {
        return "0/100";
    }
}