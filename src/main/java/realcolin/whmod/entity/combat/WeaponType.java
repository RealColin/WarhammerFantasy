package realcolin.whmod.entity.combat;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;

public enum WeaponType {
    NONE,
    SWORD,
    AXE,
    MACE;

    static WeaponType getType(ItemStack stack) {
        if (stack.getItem() instanceof AxeItem)
            return AXE;
        else if (stack.getItem() instanceof MaceItem)
            return MACE;
        else if (stack.is(ItemTags.SWORDS))
            return SWORD;
        else
            return NONE;
    }
}
