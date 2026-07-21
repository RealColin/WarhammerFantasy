package realcolin.whmod.entity.combat;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Map;

public class CombatState {


    // mutable state
    private boolean attacking = false;
    private int attackTick = 0;
    private int attackDurationTicks = 0;
    private int damageTick = 0;
    private boolean damageApplied = false;

    public CombatState() {

    }
    
    public boolean startAttack(double attackSpeed) {
        if (attacking) return false;
        if (attackSpeed <= 0.0) return false;

        double windup = 0.5;

        // figure out how many ticks the attack will last, and at which tick damage is applied
        this.attackDurationTicks = Math.max(1, Mth.floor((20.0 / attackSpeed)));
        this.damageTick = Math.max(1, Mth.floor(this.attackDurationTicks * windup));

        // then, finalize the state
        this.attackTick = 0;
        this.damageApplied = false;
        this.attacking = true;

        return true;
    }

    public boolean tickAttack() {
        if (!attacking)
            return false;

        attackTick++;

        var shouldApplyDamage = false;

        if (!damageApplied && attackTick >= damageTick) {
            damageApplied = true;
            shouldApplyDamage = true;
        }

        // attack has finished, reset state
        if (attackTick >= attackDurationTicks)
            reset();

        return shouldApplyDamage;
    }

    public void reset() {
        this.attacking = false;
        this.attackTick = 0;
        this.attackDurationTicks = 0;
        this.damageTick = 0;
        this.damageApplied = false;
    }

    public void debugPrint() {
        if (attacking)
            System.out.println("current tick / total ticks: " + attackTick + "/" + attackDurationTicks);
    }
}
