package realcolin.whmod.entity.npc.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import realcolin.whmod.entity.npc.NPC;

public class TargetEnemyGoal extends TargetGoal {
    private LivingEntity target;

    public TargetEnemyGoal(NPC npc, boolean mustSee) {
        super(npc, mustSee);
    }

    @Override
    public boolean canUse() {
        findTarget();
        return target != null;
    }

    @Override
    public void start() {
        this.mob.setTarget(target);
        super.start();
    }

    private void findTarget() {
        var level = getServerLevel(this.mob);
        var npc = (NPC) mob;

        var nearby = level.getEntitiesOfClass(
                LivingEntity.class,
                npc.getBoundingBox().inflate(this.getFollowDistance()),
                entity -> entity != npc && npc.isEnemy(entity)
        );

        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;

        for (var entity : nearby) {
            if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) continue;

            var dist = npc.distanceToSqr(entity);
            if (dist < bestDist) {
                bestDist = dist;
                best = entity;
            }
        }

        this.target = best;
    }
}
