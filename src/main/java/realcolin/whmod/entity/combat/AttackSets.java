package realcolin.whmod.entity.combat;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public final class AttackSets {

    public static AttackSet resolve(LivingEntity entity) {
        if (entity.getMainHandItem().is(ItemTags.SWORDS)) {
            return AttackSets.SWORD;
        }

        return AttackSets.PUNCH;
    }

    public static final AttackSet SWORD = new AttackSet(
            "sword",
            List.of(
                    new Attack(
                            "slash_1",
                            0.4,
                            1.0,
                            new SweepShape(3.0, 130.0, 0.6, 0.0)
                    ),
                    new Attack(
                            "slash_2",
                            0.4,
                            1.0,
                            new SweepShape(3.0, 130.0, 0.6, 0.0)
                    ),
                    new Attack(
                            "slash_3",
                            0.4,
                            1.0,
                            new SweepShape(3.0, 130.0, 0.6, 45.0)
                    ),
                    new Attack(
                            "slash_4",
                            0.4,
                            1.0,
                            new SweepShape(3.0, 130.0, 0.6, -45.0)
                    )
    ));

    public static final AttackSet PUNCH = new AttackSet(
            "punch",
            List.of(
                    new Attack(
                            "punch",
                            0.4,
                            1.0,
                            new ThrustShape(2.0, 0.5)
                            )
            )
    );

    private AttackSets() {}
}
