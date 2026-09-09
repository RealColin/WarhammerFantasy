package realcolin.whmod.entity.combat;

import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public record ThrustShape(double reach, double radius) implements AttackShape{
    @Override
    public List<LivingEntity> getTargets(LivingEntity attacker) {
        return List.of();
    }
}
