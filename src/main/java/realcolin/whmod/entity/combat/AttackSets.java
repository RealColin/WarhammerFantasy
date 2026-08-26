package realcolin.whmod.entity.combat;

import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AttackSets {

    public static AttackSet resolve(LivingEntity entity) {
        return AttackSets.SWORD;
    }

    public static final AttackSet SWORD = new AttackSet(
            "sword",
            List.of(
                    new Attack(
                            "slash_1",
                            0.4,
                            1.0,
                            3.0,
                            90.0,
                            0.0,
                            0.0
                    ),
                    new Attack(
                            "slash_2",
                            0.4,
                            1.0,
                            3.0,
                            -90.0,
                            0.0,
                            0.0
                    )
            )
    );
}
