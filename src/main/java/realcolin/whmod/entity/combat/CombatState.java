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

    public void startAttack(double attackSpeed) {
        if (attacking) return;

        double windup = 0.5;

        // figure out how many ticks the attack will last, and at which tick damage is applied
        this.attackDurationTicks = Math.max(1, Mth.floor((20.0 / attackSpeed)));
        this.damageTick = Math.max(1, Mth.floor(this.attackDurationTicks * windup));

        // then, finalize the state
        this.attackTick = 0;
        this.damageApplied = false;
        this.attacking = true;
    }

    public void tickAttack() {
        attackTick++;

        if (!damageApplied && attackTick >= damageTick) {
            // TODO handle damage
            damageApplied = true;
        }

        // attack has finished, reset state
        if (attackTick >= attackDurationTicks) {
            attacking = false;
            attackTick = 0;
            attackDurationTicks = 0;
            damageTick = 0;
            damageApplied = false;
        }
    }
}
