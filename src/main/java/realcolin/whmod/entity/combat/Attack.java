package realcolin.whmod.entity.combat;

public record Attack(
        String id,
        double windupRatio,
        double damageMultiplier,
        double reach,
        double horizontalAngle,
        double verticalAngle,
        double rollAngle
) { }
