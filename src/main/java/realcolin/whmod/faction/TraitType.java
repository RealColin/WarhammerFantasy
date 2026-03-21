package realcolin.whmod.faction;

public enum TraitType {
    POSITIVE(0xFF55FF55),
    NEGATIVE(0xFFFF5555);

    private final int color;

    TraitType(int color) {
        this.color = color;
    }

    public int color() {
        return color;
    }
}
