package realcolin.whmod.faction;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import realcolin.whmod.util.Pair;

import java.util.List;

public enum Faction {
    NONE(
            "none",
            "",
            "none",
            new Pair(0, 0),
            Component.translatable("faction.whmod.none.desc"),
            List.of()),
    EMPIRE(
            "empire",
            "The Empire",
            "Imperial",
            new Pair(46526, 30134),
            Component.translatable("faction.whmod.empire.desc"),
            List.of(
                    new FactionTrait(
                            Component.translatable("faction.whmod.empire.trait.versatile"),
                            Component.translatable("faction.whmod.empire.trait.versatile.desc"),
                            TraitType.POSITIVE
                    ),
                    new FactionTrait(
                            Component.translatable("faction.whmod.empire.trait.gunpowder"),
                            Component.translatable("faction.whmod.empire.trait.gunpowder.desc"),
                            TraitType.POSITIVE
                    )
            )
    ),
    DWARFS(
            "dwarfs",
            "Dwarfs",
            "Dwarven",
            new Pair(46526, 30134),
            Component.translatable("faction.whmod.dwarfs.desc"),
            List.of(
                    new FactionTrait(
                            Component.translatable("faction.whmod.dwarfs.trait.strong"),
                            Component.translatable("faction.whmod.dwarfs.trait.strong.desc"),
                            TraitType.POSITIVE
                    ),
                    new FactionTrait(
                            Component.translatable("faction.whmod.dwarfs.trait.slow"),
                            Component.translatable("faction.whmod.dwarfs.trait.slow.desc"),
                            TraitType.NEGATIVE
                    )
            )
    ),
    BEASTMEN("beastmen", "Beastmen", "Beastish", new Pair(46526, 30134), Component.translatable("faction.whmod.beastmen.desc"), List.of(

    )),
    GREENSKINS("greenskins", "Greenskins", "Orcish", new Pair(46526, 30134), Component.translatable("faction.whmod.greenskins.desc"), List.of(

    )),
    WOOD_ELVES("wood_elves", "Wood Elves", "Wood_Elven", new Pair(46526, 30134), Component.translatable("faction.whmod.wood_elves.desc"), List.of(

    ));

    private final String id;
    private final String name;
    private final String adjective;
    private final Pair spawnPos;
    private final Component description;
    private final List<FactionTrait> traits;

    Faction(String id, String name, String adjective, Pair spawnPos, Component description, List<FactionTrait> traits) {
        this.id = id;
        this.name = name;
        this.adjective = adjective;
        this.spawnPos = spawnPos;
        this.description = description;
        this.traits = traits;
    }

    public String id() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String adjective() {
        return adjective;
    }

    public Pair spawnPos() {
        return spawnPos;
    }

    public Component description() {
        return description;
    }

    public List<FactionTrait> traits() {
        return traits;
    }

    public static final Codec<Faction> CODEC = Codec.STRING.xmap(
            Faction::fromId,
            Faction::id
    );

    public static final StreamCodec<ByteBuf, Faction> STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(Faction::fromId, Faction::id);

    private static Faction fromId(String id) {
        for (var fac : values()) {
            if (fac.id.equals(id)) return fac;
        }
        throw new IllegalArgumentException("Unknown faction id: " + id);
    }
}
