package realcolin.whmod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

public class WHCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WHMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WH_COMBAT = CREATIVE_MODE_TABS.register(
            "wh_combat",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + WHMod.MOD_ID + ".combat"))
                    .icon(() -> new ItemStack(WHItems.IMPERIAL_SWORD.get()))
                    .displayItems((params, output) -> {
                        output.accept(WHItems.IMPERIAL_SWORD.get());

                        for (var armorSet : WHItems.ARMOR_SETS) {
                            output.accept(armorSet.helmet());
                            output.accept(armorSet.chestplate());
                            output.accept(armorSet.leggings());
                            output.accept(armorSet.boots());
                        }
                    })

                    .build());
}
