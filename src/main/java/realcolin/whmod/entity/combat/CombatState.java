package realcolin.whmod.entity.combat;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Map;

public class CombatState {
    private final LivingEntity attacker;
    private final Map<WeaponType, AttackList> attacks;

    // mutable state
    private boolean attacking = false;
    private Attack currentAttack;
    private int nextAttackIndex = 0;
    private int attackTick = 0;
    private int attackDurationTicks = 0;
    private int damageTick = 0;
    private boolean damageApplied = false;

    public CombatState(LivingEntity attacker, Map<WeaponType, AttackList> attacks) {
        this.attacker = attacker;
        this.attacks = attacks;
    }

    // TODO implement this please
    public void startAttack() {
        // first, get the current Attack
        nextAttack();
        var windup = currentAttack.windup();

        // next, get the entity's attack speed
        var attackSpeed = attacker.getAttributeValue(Attributes.ATTACK_SPEED);

        // then, calculate how long the attack will last and which tick damage will be applied on
        this.attackDurationTicks = Math.max(1, Mth.floor((20.0 / attackSpeed)));
        this.damageTick = Math.max(1, Mth.floor(this.attackDurationTicks * windup));

        // last, finalize the rest of the state
        this.attackTick = 0;
        this.damageApplied = false;
        this.attacking = true;
    }

    // TODO implement this please
    public void tickAttack() {

    }

    // TODO implement this please
    public String getCurrentAnimation() {
        return currentAttack.animation();
    }

    // TODO implement this please
    private void nextAttack() {
        // this needs to properly set currentAttack to the next Attack in the AttackList
        // for the weapon this entity is using
        // if it is dual wielding idk wtf to do i have to come up with that logic - maybe I need to keep track of multiple combos or something?

        // maybe I have a "main hand combo index" and a "off hand combo index" and if the mob is dual wielding then i alternate which one i update?
        // or maybe I need a combo state per item stack or something, so that weapon changes won't cause a new weapon to start swinging mid combo
        // so I have two Tuple<ItemStack, Integer>, one for the mainhand and one for the offhand, and then if both hands have weapons I'll alternate combos
        // this is probably the most complicated piece of logic I have, everything else about this seems easy
    }
}
