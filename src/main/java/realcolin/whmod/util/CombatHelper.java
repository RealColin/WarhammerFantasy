package realcolin.whmod.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CombatHelper {

    // TODO make a data structure to represent the shape the attack box takes, and see if target is within
    public static boolean withinAttackRange(LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return false;

        double reach = attacker.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);

        /*
            The logic right now:
            - Get the forward vec (the way the body is facing)
            - Make a list of the points of the bounding box of the target
            - Check whether each point is within an arc in front of the attacker
                - arc is X degrees, with X/2 degrees on each side of forward vec
                - it extends from origin to a vec that is reach distance away
         */

        // Better for a horizontal slash than pure eye position
        Vec3 origin = new Vec3(
                attacker.getX(),
                attacker.getY() + attacker.getBbHeight() * 0.6,
                attacker.getZ()
        );

        Vec3 forward = attacker.getForward();

        double halfAngleDegrees = 60.0; // 120 total arc
        double halfHeight = 0.9;        // tune this

        AABB box = target.getBoundingBox().inflate(target.getPickRadius());

        Vec3[] points = new Vec3[] {
                box.getCenter(),

                new Vec3((box.minX + box.maxX) * 0.5, box.minY, (box.minZ + box.maxZ) * 0.5),
                new Vec3((box.minX + box.maxX) * 0.5, box.maxY, (box.minZ + box.maxZ) * 0.5),

                new Vec3(box.minX, (box.minY + box.maxY) * 0.5, (box.minZ + box.maxZ) * 0.5),
                new Vec3(box.maxX, (box.minY + box.maxY) * 0.5, (box.minZ + box.maxZ) * 0.5),
                new Vec3((box.minX + box.maxX) * 0.5, (box.minY + box.maxY) * 0.5, box.minZ),
                new Vec3((box.minX + box.maxX) * 0.5, (box.minY + box.maxY) * 0.5, box.maxZ),

                new Vec3(box.minX, box.minY, box.minZ),
                new Vec3(box.minX, box.minY, box.maxZ),
                new Vec3(box.maxX, box.minY, box.minZ),
                new Vec3(box.maxX, box.minY, box.maxZ),

                new Vec3(box.minX, box.maxY, box.minZ),
                new Vec3(box.minX, box.maxY, box.maxZ),
                new Vec3(box.maxX, box.maxY, box.minZ),
                new Vec3(box.maxX, box.maxY, box.maxZ)
        };

        for (Vec3 p : points) {
            if (pointInHorizontalArcVolume(origin, forward, p, reach, halfAngleDegrees, halfHeight)) {
                return true;
            }
        }

        return false;
    }

    private static boolean pointInHorizontalArcVolume(
            Vec3 origin,
            Vec3 forward,
            Vec3 point,
            double reach,
            double halfAngleDegrees,
            double halfHeight
    ) {
        Vec3 d = point.subtract(origin);

        // Vertical thickness of the slash
        if (Math.abs(d.y) > halfHeight) {
            return false;
        }

        // Full 3D reach cap
        if (d.lengthSqr() > reach * reach) {
            return false;
        }

        // Horizontal angular check
        Vec3 flatForward = new Vec3(forward.x, 0.0, forward.z);
        Vec3 flatDelta = new Vec3(d.x, 0.0, d.z);

        if (flatForward.lengthSqr() < 1.0E-8 || flatDelta.lengthSqr() < 1.0E-8) {
            return true;
        }

        flatForward = flatForward.normalize();
        flatDelta = flatDelta.normalize();

        double cosThreshold = Math.cos(Math.toRadians(halfAngleDegrees));
        return flatForward.dot(flatDelta) >= cosThreshold;
    }
}
