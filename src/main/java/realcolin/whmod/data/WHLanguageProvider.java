package realcolin.whmod.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;
import realcolin.whmod.item.WHItems;

public class WHLanguageProvider extends LanguageProvider {
    public WHLanguageProvider(PackOutput output) {
        super(output, WHMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        WHBlocks.BLOCKS.getEntries().forEach(h -> addBlock(h, toTitleCase(h.getId().getPath())));
        WHItems.ITEMS.getEntries().forEach(h -> addItem(h, toTitleCase(h.getId().getPath())));
    }

    private static String toTitleCase(String path) {
        // "pine_log" -> "Pine Log"
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            sb.append(Character.toUpperCase(p.charAt(0)))
                    .append(p.substring(1))
                    .append(' ');
        }
        return sb.toString().trim();
    }
}
