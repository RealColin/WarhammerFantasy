package realcolin.whmod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;

public record ArmorSet(DeferredItem<Item> helmet, DeferredItem<Item> chestplate, DeferredItem<Item> leggings, DeferredItem<Item> boots) {

    public static ArmorSet make(String name, ArmorMaterial material) {
        String helmetName = name + "_helmet";
        DeferredItem<Item> helmet = WHItems.ITEMS.registerItem(helmetName, props -> new WHArmorItem(material, ArmorType.HELMET, props));

        String chestplateName = name + "_chestplate";
        DeferredItem<Item> chestplate = WHItems.ITEMS.registerItem(chestplateName, props -> new WHArmorItem(material, ArmorType.CHESTPLATE, props));

        String leggingsName = name + "_leggings";
        DeferredItem<Item> leggings = WHItems.ITEMS.registerItem(leggingsName, props -> new WHArmorItem(material, ArmorType.LEGGINGS, props));

        String bootsName = name + "_boots";
        DeferredItem<Item> boots = WHItems.ITEMS.registerItem(bootsName, props -> new WHArmorItem(material, ArmorType.BOOTS, props));

        return new ArmorSet(helmet, chestplate, leggings, boots);
    }
}
