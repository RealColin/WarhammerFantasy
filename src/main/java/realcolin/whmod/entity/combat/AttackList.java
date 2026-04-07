package realcolin.whmod.entity.combat;

import java.util.List;

public class AttackList {
    private final List<Attack> attacks;

    public AttackList(Attack... attacks) {
        this.attacks = List.of(attacks);
    }
}
