package realcolin.whmod.entity.npc.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;
import realcolin.whmod.entity.npc.NPC;

public class NPCLookGoal extends Goal {
    private final NPC npc;
    private final TargetingConditions lookAtConditions;
    private final double lookDistance;

    private Entity lookTarget = null;
    private Vec3 lookPos = null;

    private int lookTime = 0;

    public NPCLookGoal(NPC npc, double lookDistance) {
        this.npc = npc;
        this.lookDistance = lookDistance;
        this.lookAtConditions = TargetingConditions.forNonCombat().range(lookDistance);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    // if the npc has a target, look at that and do not look away
    // else, either look at a nearby player, look at another nearby NPC, or look at a random location

    @Override
    public void tick() {
        if (npc.getTarget() != null) {
            var target = npc.getTarget();
            npc.getLookControl().setLookAt(target, 50.0F, 50.0F);
            lookTime = 0;
        } else {
            if (lookPos == null && lookTarget == null) {
                findNewLookTarget();
            }

            if (lookTime > 0) {
                if (lookTarget != null)
                    lookPos = new Vec3(lookTarget.getX(), lookTarget.getEyeY(), lookTarget.getZ());

                npc.getLookControl().setLookAt(lookPos.x, lookPos.y, lookPos.z, 30.0F, 30.0F);
                lookTime--;
            } else {
                lookPos = null;
                lookTarget = null;
            }
        }
    }

    @SuppressWarnings("resource")
    private void findNewLookTarget() {
        if (npc.getRandom().nextFloat() < 0.04F) {
            double d0 = (Math.PI * 2D) * npc.getRandom().nextDouble();
            var relX = Math.cos(d0);
            var relZ = Math.sin(d0);

            lookPos = new Vec3(npc.getX() + relX, npc.getEyeY(), npc.getZ() + relZ);
            lookTime = 20 + npc.getRandom().nextInt(20);
        } else {
            var server = getServerLevel(npc);
            var player = server.getNearestPlayer(this.lookAtConditions, npc, npc.getX(), npc.getEyeY(), npc.getZ());

            if (player != null) {
                lookPos = new Vec3(player.getX(), player.getEyeY(), player.getZ());
                lookTime = 30;
                lookTarget = player;
            } else {
                var otherNPC = server.getNearestEntity(
                        npc.level().getEntitiesOfClass(
                                NPC.class, npc.getBoundingBox().inflate(lookDistance, 3.0, lookDistance), (_) -> true),
                        lookAtConditions, npc, npc.getX(), npc.getEyeY(), npc.getZ());

                if (otherNPC != null) {
                    lookPos = new Vec3(otherNPC.getX(), otherNPC.getEyeY(), otherNPC.getZ());
                    lookTime = 30;
                    lookTarget = otherNPC;
                }
            }
        }
    }
}
