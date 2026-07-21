package realcolin.whmod.entity.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import realcolin.whmod.WHMod;

public class CombatController {

    public static void startAttack(LivingEntity entity) {
        var state = entity.getData(WHMod.COMBAT_STATE_ATTACHMENT);
        var attackSpeed = entity.getAttributeValue(Attributes.ATTACK_SPEED);

        var started = state.startAttack(attackSpeed);
        if (!started)
            return;

        System.out.println("Started attack for " + entity);
    }

    public static void tickCombat(LivingEntity entity) {
        var state = entity.getData(WHMod.COMBAT_STATE_ATTACHMENT);
        state.debugPrint();

        var ticked = state.tickAttack();
        if (!ticked)
            return;

        System.out.println("Should apply damage for attack by " + entity);
    }
}
