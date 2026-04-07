package realcolin.whmod.entity.combat;

public final class Attack {
    private final String animation;
    private final AttackShape shape;
    private final double windup;
    private final double damageBonus;
    private final double reachBonus;

    public Attack(String animation, AttackShape shape, double windup, double damageBonus, double reachBonus) {
        this.animation = animation;
        this.shape = shape;
        this.windup = windup;
        this.damageBonus = damageBonus;
        this.reachBonus = reachBonus;
    }

    public String animation() {
        return animation;
    }

    public double windup() {
        return windup;
    }
}
