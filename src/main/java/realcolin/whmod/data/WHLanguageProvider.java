package realcolin.whmod.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;
import realcolin.whmod.entity.WHEntities;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.WHItems;

public class WHLanguageProvider extends LanguageProvider {
    public WHLanguageProvider(PackOutput output) {
        super(output, WHMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        WHBlocks.BLOCKS.getEntries().forEach(h -> addBlock(h, toTitleCase(h.getId().getPath())));
        WHItems.ITEMS.getEntries().forEach(h -> addItem(h, toTitleCase(h.getId().getPath())));
        WHEntities.ENTITY_TYPES.getEntries().forEach(h -> addEntityType(h, toTitleCase(h.getId().getPath())));
        addFactions();
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

    private void addFactions() {
        add("faction.whmod.empire.desc", """
                The Empire is the greatest realm of mankind in the Old World, a vast and fractious nation held together by faith, steel, and the will of its Elector Counts. From the bustling cities along the Reik to the haunted forests of the Drakwald, the Empire stands as the bulwark against the countless horrors that threaten civilization\
                
                
                Traits:
                +Strong
                -Whatever""");
        add("faction.whmod.dwarfs.desc", "The Dawi");
        add("faction.whmod.beastmen.desc", "Beast Men");
    }
}
