package realcolin.whmod.faction;

public class FactionRelationships {
    private static final FactionRelationship[][] RELATIONS =
            new FactionRelationship[Faction.values().length][Faction.values().length];

    static {
        for (Faction a : Faction.values()) {
            for (Faction b : Faction.values()) {
                RELATIONS[a.ordinal()][b.ordinal()] =
                        a == b ? FactionRelationship.ALLIED : FactionRelationship.NEUTRAL;
            }
        }

        // EMPIRE relationships
        setSymmetric(Faction.EMPIRE, Faction.DWARFS, FactionRelationship.ALLIED);
        setSymmetric(Faction.EMPIRE, Faction.BEASTMEN, FactionRelationship.HOSTILE);
        setSymmetric(Faction.EMPIRE, Faction.GREENSKINS, FactionRelationship.HOSTILE);
        set(Faction.EMPIRE, Faction.WOOD_ELVES, FactionRelationship.NEUTRAL);

        // DWARFS
        setSymmetric(Faction.DWARFS, Faction.BEASTMEN, FactionRelationship.HOSTILE);
        setSymmetric(Faction.DWARFS, Faction.GREENSKINS, FactionRelationship.HOSTILE);
        setSymmetric(Faction.DWARFS, Faction.WOOD_ELVES, FactionRelationship.UNFRIENDLY);

        // BEASTMEN
        setSymmetric(Faction.BEASTMEN, Faction.GREENSKINS, FactionRelationship.HOSTILE);
        setSymmetric(Faction.BEASTMEN, Faction.WOOD_ELVES, FactionRelationship.HOSTILE);

        // GREENSKINS
        setSymmetric(Faction.GREENSKINS, Faction.WOOD_ELVES, FactionRelationship.HOSTILE);

        // WOOD ELVES
        set(Faction.WOOD_ELVES, Faction.EMPIRE, FactionRelationship.UNFRIENDLY);

    }


    private static void setSymmetric(Faction a, Faction b, FactionRelationship relationship) {
        RELATIONS[a.ordinal()][b.ordinal()] = relationship;
        RELATIONS[b.ordinal()][a.ordinal()] = relationship;
    }

    private static void set(Faction a, Faction b, FactionRelationship relationship) {
        RELATIONS[a.ordinal()][b.ordinal()] = relationship;
    }

    public static FactionRelationship get(Faction a, Faction b) {
        return RELATIONS[a.ordinal()][b.ordinal()];
    }
}
