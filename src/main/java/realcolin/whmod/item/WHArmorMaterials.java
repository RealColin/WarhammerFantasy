package realcolin.whmod.item;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import realcolin.whmod.WHMod;

import java.util.Map;

public class WHArmorMaterials {
    private static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    public static final ResourceKey<EquipmentAsset> STATE_TROOP_ASSETS = createId("state_troop");

    public static ArmorMaterial STATE_TROOP = new ArmorMaterial(
            15,
            makeDefense(2, 5, 6, 2, 5),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            ItemTags.REPAIRS_IRON_ARMOR,
            STATE_TROOP_ASSETS);

    @SuppressWarnings("SameParameterValue")
    private static Map<ArmorType, Integer> makeDefense(int boots, int leggings, int chestplate, int helmet, int body) {
        return Maps.newEnumMap(Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, leggings, ArmorType.CHESTPLATE, chestplate, ArmorType.HELMET, helmet, ArmorType.BODY, body));
    }

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<EquipmentAsset> createId(String name) {
        return ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(WHMod.MOD_ID, name));
    }
}
