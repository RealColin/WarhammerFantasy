package realcolin.whmod.entity.combat;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record ThrustShape(double reach, double radius) implements AttackShape{
    @Override
    public List<LivingEntity> getTargets(LivingEntity attacker) {
        spawnDebugParticles(attacker);

        return getCandidates(attacker)
                .stream()
                .filter(target -> intersects(attacker, target))
                .toList();
    }

    @Override
    public Vec3 support(LivingEntity attacker, Vec3 direction) {
        Vec3 dir = direction.normalize();
        Vec3 forward = attacker.getLookAngle().normalize();

        Vec3 origin = attacker.position()
                .add(0.0, attacker.getBbHeight() * 0.5, 0.0);

        double centerlineLength = Math.max(0.0, reach - radius);

        Vec3 point = dir.dot(forward) >= 0.0
                ? origin.add(forward.scale(centerlineLength))
                : origin;

        return point.add(dir.scale(radius));
    }

    private void spawnDebugParticles(LivingEntity attacker) {
        Vec3 forward = attacker.getLookAngle().normalize();

        Vec3 origin = attacker.position()
                .add(0.0, attacker.getBbHeight() * 0.5, 0.0);

        double centerlineLength = Math.max(0.0, reach - radius);
        Vec3 end = origin.add(forward.scale(centerlineLength));

        // Create two vectors perpendicular to forward.
        Vec3 reference = Math.abs(forward.y) > 0.99
                ? new Vec3(1.0, 0.0, 0.0)
                : new Vec3(0.0, 1.0, 0.0);

        Vec3 right = forward.cross(reference).normalize();
        Vec3 up = right.cross(forward).normalize();

        int ringPoints = 12;
        int lengthSteps = Math.max(1, (int) Math.ceil(centerlineLength * 4.0));

        // Cylinder portion of the capsule.
        for (int i = 0; i <= lengthSteps; i++) {
            double t = (double) i / lengthSteps;

            Vec3 center = origin.add(
                    forward.scale(centerlineLength * t)
            );

            spawnRing(attacker, center, right, up, radius, ringPoints);
        }

        // Rounded base and tip.
        int capSteps = 4;

        for (int i = 1; i < capSteps; i++) {
            double angle = (Math.PI / 2.0) * i / capSteps;

            double axialOffset = Math.cos(angle) * radius;
            double ringRadius = Math.sin(angle) * radius;

            // Back cap
            Vec3 backCenter = origin.subtract(
                    forward.scale(axialOffset)
            );

            spawnRing(
                    attacker,
                    backCenter,
                    right,
                    up,
                    ringRadius,
                    ringPoints
            );

            // Front cap
            Vec3 frontCenter = end.add(
                    forward.scale(axialOffset)
            );

            spawnRing(
                    attacker,
                    frontCenter,
                    right,
                    up,
                    ringRadius,
                    ringPoints
            );
        }

        // Extreme ends of the capsule.
        spawnParticle(attacker, origin.subtract(forward.scale(radius)));
        spawnParticle(attacker, end.add(forward.scale(radius)));
    }

    private void spawnRing(
            LivingEntity attacker,
            Vec3 center,
            Vec3 right,
            Vec3 up,
            double ringRadius,
            int points
    ) {
        for (int i = 0; i < points; i++) {
            double angle = 2.0 * Math.PI * i / points;

            Vec3 offset = right.scale(Math.cos(angle) * ringRadius)
                    .add(up.scale(Math.sin(angle) * ringRadius));

            spawnParticle(attacker, center.add(offset));
        }
    }

    private void spawnParticle(LivingEntity attacker, Vec3 position) {
        if (attacker.level() instanceof ServerLevel level) {
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
}
