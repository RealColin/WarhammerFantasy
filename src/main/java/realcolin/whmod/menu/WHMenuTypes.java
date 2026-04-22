package realcolin.whmod.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

public class WHMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, WHMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<FactionCraftingMenu>> FACTION_CRAFTING =
            MENU_TYPES.register("faction_crafting", () -> IMenuTypeExtension.create(FactionCraftingMenu::new));
//    public static final DeferredHolder<MenuType<?>, MenuType<FactionCraftingMenu>> FACTION_CRAFTING = registerMenu("faction_crafting", FactionCraftingMenu::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenu(String name, MenuType.MenuSupplier<T> constructor) {
        return MENU_TYPES.register(name, () -> new MenuType<>(constructor, FeatureFlags.VANILLA_SET));
    }
}
