package realcolin.whmod.faction;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import realcolin.whmod.util.Pair;

public enum Faction {
    NONE("none", new Pair(0, 0), Component.translatable("faction.whmod.none.desc")),
    EMPIRE("empire", new Pair(46526, 30134), Component.translatable("faction.whmod.empire.desc")),
    DWARFS("dwarfs", new Pair(46526, 30134), Component.translatable("faction.whmod.dwarfs.desc")),
    BEASTMEN("beastmen", new Pair(46526, 30134), Component.translatable("faction.whmod.beastmen.desc")),
    DUMMY1("dummy1", new Pair(0, 0), Component.literal("hi")),
    DUMMY2("dummy2", new Pair(0, 0), Component.literal("hi")),
    DUMMY3("dummy3", new Pair(0, 0), Component.literal("hi")),
    DUMMY4("dummy4", new Pair(0, 0), Component.literal("hi")),
    DUMMY5("dummy5", new Pair(0, 0), Component.literal("hi")),
    DUMMY6("dummy6", new Pair(0, 0), Component.literal("hi")),
    DUMMY7("dummy7", new Pair(0, 0), Component.literal("hi"));

    private final String id;
    private final Pair spawnPos;
    private final Component description;


    Faction(String id, Pair spawnPos, Component description) {
        this.id = id;
        this.spawnPos = spawnPos;
        this.description = description;
    }

    public String id() {
        return id;
    }

    public Pair spawnPos() {
        return spawnPos;
    }

    public Component description() {
        return description;
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
