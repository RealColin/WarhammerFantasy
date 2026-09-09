package realcolin.whmod.entity.combat;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record SweepShape(double reach, double sweepAngle, double thickness, double rollAngle) implements AttackShape{
    @Override
    public List<LivingEntity> getTargets(LivingEntity attacker) {
        debugDraw(attacker);

        return getCandidates(attacker)
                .stream()
                .filter(target -> isInsideSweep(attacker, target))
                .toList();
    }

    private boolean isInsideSweep(LivingEntity attacker, LivingEntity target) {
        var box = target.getBoundingBox();

        var xs = new double[] {
                box.minX,
                (box.minX + box.maxX) / 2.0,
                box.maxX
        };

        var ys = new double[] {
                box.minY,
                (box.minY + box.maxY) / 2.0,
                box.maxY
        };

        var zs = new double[] {
                box.minZ,
                (box.minZ + box.maxZ) / 2.0,
                box.maxZ
        };

        for (double x : xs) {
            for (double y : ys) {
                for (double z : zs) {
                    if (isPointInsideSweep(
                            attacker,
                            new Vec3(x, y, z)
                    )) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isPointInsideSweep(LivingEntity attacker, Vec3 targetPos) {
        var origin = attacker.getBoundingBox().getCenter();
        var toTarget = targetPos.subtract(origin);

        var forward = attacker.getLookAngle().normalize();
        var up = attacker.getUpVector(1.0F).normalize();
        var right = forward.cross(up).normalize();

        var roll = Math.toRadians(rollAngle);
        var cos = Math.cos(roll);
        var sin = Math.sin(roll);

        var sweepAxis = right.scale(cos)
                .add(up.scale(sin))
                .normalize();

        var thicknessAxis = up.scale(cos)
                .subtract(right.scale(sin))
                .normalize();

        var forwardAmount = toTarget.dot(forward);
        var sweepAmount = toTarget.dot(sweepAxis);
        var thicknessAmount = toTarget.dot(thicknessAxis);

        if (forwardAmount < 0.0)
            return false;

        var planarDistance = Math.sqrt(
                forwardAmount * forwardAmount +
                        sweepAmount * sweepAmount
        );

        if (planarDistance > reach)
            return false;

        var angle = Math.toDegrees(
                Math.atan2(sweepAmount, forwardAmount)
        );

        if (Math.abs(angle) > sweepAngle / 2.0)
            return false;

        return Math.abs(thicknessAmount) <= thickness / 2.0;
    }

    private boolean isInsideSweepOld(LivingEntity attacker, LivingEntity target) {
        var origin = attacker.getBoundingBox().getCenter();
        var targetPos = target.getBoundingBox().getCenter();
        var toTarget = targetPos.subtract(origin);

        var forward = attacker.getLookAngle().normalize();
        var up = attacker.getUpVector(1.0F).normalize();
        var right = forward.cross(up).normalize();

        var roll = Math.toRadians(rollAngle);
        var cos = Math.cos(roll);
        var sin = Math.sin(roll);

        var sweepAxis = right.scale(cos).add(up.scale(sin)).normalize();
        var thicknessAxis = up.scale(cos).subtract(right.scale(sin)).normalize();

        var forwardAmount = toTarget.dot(forward);
        var sweepAmount = toTarget.dot(sweepAxis);
        var thicknessAmount = toTarget.dot(thicknessAxis);

        if (forwardAmount < 0.0)
            return false;

        var planarDistance = Math.sqrt(forwardAmount * forwardAmount + sweepAmount * sweepAmount);

        if (planarDistance > reach)
            return false;

        var angle = Math.toDegrees(Math.atan2(sweepAmount, forwardAmount));

        if (Math.abs(angle) > sweepAngle / 2.0)
            return false;

        return Math.abs(thicknessAmount) <= thickness / 2.0;
    }

    private void debugDraw(LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel level))
            return;

        var origin = attacker.getBoundingBox().getCenter();

        var forward = attacker.getLookAngle().normalize();
        var up = attacker.getUpVector(1.0F).normalize();
        var right = forward.cross(up).normalize();

        var roll = Math.toRadians(rollAngle);
        var cosRoll = Math.cos(roll);
        var sinRoll = Math.sin(roll);

        var sweepAxis = right.scale(cosRoll)
                .add(up.scale(sinRoll))
                .normalize();

        var thicknessAxis = up.scale(cosRoll)
                .subtract(right.scale(sinRoll))
                .normalize();

        var halfSweep = Math.toRadians(sweepAngle / 2.0);
        var halfThickness = thickness / 2.0;

        int arcSteps = 32;

        // Draw curved outer edge of the sweep
        for (int i = 0; i <= arcSteps; i++) {
            double t = (double) i / arcSteps;

            double angle = -halfSweep + t * (halfSweep * 2.0);

            var planarDirection =
                    forward.scale(Math.cos(angle))
                            .add(sweepAxis.scale(Math.sin(angle)));

            var centerPoint = origin.add(
                    planarDirection.scale(reach)
            );

            var topPoint = centerPoint.add(
                    thicknessAxis.scale(halfThickness)
            );

            var bottomPoint = centerPoint.subtract(
                    thicknessAxis.scale(halfThickness)
            );

            spawnDebugParticle(level, centerPoint);
            spawnDebugParticle(level, topPoint);
            spawnDebugParticle(level, bottomPoint);
        }

        // Draw the two radial sides of the pie slice
        int radialSteps = 16;

        for (double angle : new double[]{-halfSweep, halfSweep}) {
            var direction =
                    forward.scale(Math.cos(angle))
                            .add(sweepAxis.scale(Math.sin(angle)));

            for (int i = 0; i <= radialSteps; i++) {
                double distance =
                        reach * ((double) i / radialSteps);

                var centerPoint = origin.add(
                        direction.scale(distance)
                );

                spawnDebugParticle(level, centerPoint);

                spawnDebugParticle(
                        level,
                        centerPoint.add(
                                thicknessAxis.scale(halfThickness)
                        )
                );

                spawnDebugParticle(
                        level,
                        centerPoint.subtract(
                                thicknessAxis.scale(halfThickness)
                        )
                );
            }
        }
    }

    private void spawnDebugParticle(ServerLevel level, Vec3 position) {
        level.sendParticles(
                ParticleTypes.END_ROD,
                position.x,
                position.y,
                position.z,
                1,
                0.0,
                0.0,
                0.0,
                0.0
        );
    }
}
