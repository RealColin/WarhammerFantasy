package realcolin.whmod.entity.combat;

import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface AttackShape {

    List<LivingEntity> getTargets(LivingEntity attacker);
    double reach();

    default List<LivingEntity> getCandidates(LivingEntity attacker) {
        var searchBox = attacker.getBoundingBox().inflate(reach());

        return attacker.level().getEntitiesOfClass(LivingEntity.class, searchBox, target -> target != attacker);
    }
}
