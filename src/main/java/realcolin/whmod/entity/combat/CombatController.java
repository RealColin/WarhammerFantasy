package realcolin.whmod.entity.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import realcolin.whmod.WHMod;

public class CombatController {

    public static void startAttack(LivingEntity entity) {
        var state = entity.getData(WHMod.COMBAT_STATE_ATTACHMENT);
        var attackSpeed = entity.getAttributeValue(Attributes.ATTACK_SPEED);
        var attackSet = AttackSets.resolve(entity);

        var started = state.startAttack(attackSpeed, attackSet);
        if (!started)
            return;

//        System.out.println("Started attack for " + entity + " at game time: " + entity.level().getGameTime());
    }

    public static void tickCombat(LivingEntity entity) {
        var state = entity.getData(WHMod.COMBAT_STATE_ATTACHMENT);
//        state.debugPrint(entity);

        var attack = state.tickAttack();
        if (attack == null)
            return;

//        System.out.println("Should apply attack for attack " + attack.id() + " by " + entity);
        applyAttack(entity, attack);
    }

    public static void applyAttack(LivingEntity attacker, Attack attack) {
        var targets = attack.shape().getTargets(attacker);

        for (var target : targets) {
//            System.out.println(attacker + " hit " + target +  " with " + attack.id());

            applyDamage(attacker, target, attack);
        }

    }

    private static void applyDamage(LivingEntity attacker, LivingEntity target, Attack attack) {

    }

}
