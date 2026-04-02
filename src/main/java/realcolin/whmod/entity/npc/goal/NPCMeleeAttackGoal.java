package realcolin.whmod.entity.npc.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import realcolin.whmod.entity.npc.NPC;

public class NPCMeleeAttackGoal extends Goal {
    private final NPC npc;
    private final double speedModifier;

    private Path path;
    private int ticksUntilPathCalc;

    public NPCMeleeAttackGoal(NPC npc, double speedModifier) {
        this.npc = npc;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean canUse() {
        var target = npc.getTarget();

        if (target == null)
            return false;
        else if (!target.isAlive())
            return false;
        else {
            path = npc.getNavigation().createPath(target, 0);
            return path != null || withinAttackRange(target);
        }
    }

    @Override
    public boolean canContinueToUse() {
        var target = npc.getTarget();

        if (target == null)
            return false;
        else if (!target.isAlive())
            return false;
        else {
            if (target instanceof Player player)
                return !player.isCreative() && !player.isSpectator();

            return true;
        }
    }

    @Override
    public void start() {
        npc.getNavigation().moveTo(path, speedModifier);
        npc.setAggressive(true);
        ticksUntilPathCalc = 0;
    }

    @Override
    public void stop() {
        npc.setTarget(null);
        npc.setAggressive(false);
        npc.getNavigation().stop();
    }

    @Override
    public void tick() {
        var target = npc.getTarget();

        if (target == null) return;

        npc.getLookControl().setLookAt(target, 30.0F, 30.0F);

        ticksUntilPathCalc = Math.max(ticksUntilPathCalc - 1, 0);
        if (ticksUntilPathCalc == 0 && !withinAttackRange(target)) {
            ticksUntilPathCalc = 5;
            var distSqr = npc.distanceToSqr(target);

            if (distSqr > 1024.0)
                ticksUntilPathCalc += 10;
            else if (distSqr > 512.0)
                ticksUntilPathCalc += 5;

            npc.getNavigation().moveTo(target, speedModifier);
        }

        tryAttack(target);
    }

    private void tryAttack(LivingEntity target) {
        if (withinAttackRange(target) && npc.hasLineOfSight(target)) {
            npc.beginAttack();
        }
    }

    private boolean withinAttackRange(LivingEntity target) {
        if (target == null) return false;

        var reach = npc.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);

        var eyePos = npc.getEyePosition();
        var look = npc.getViewVector(1.0F);
        var reachVec = eyePos.add(look.scale(reach));

        var hitbox = target.getBoundingBox().inflate(target.getPickRadius());

        var hit = hitbox.clip(eyePos, reachVec);

        return hit.isPresent();
    }
}
