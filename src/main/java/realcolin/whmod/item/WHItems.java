package realcolin.whmod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

public class WHItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WHMod.MOD_ID);

    public static final DeferredItem<Item> IMPERIAL_SWORD = ITEMS.registerItem(
            "imperial_sword",
            props -> new Item(
                    props.sword(
                            ToolMaterial.IRON,
                            3.0F,
                            -2.4F)
            )
    );
}
