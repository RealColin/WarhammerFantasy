package realcolin.whmod.faction;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import realcolin.whmod.util.Pair;

public enum Faction {
    NONE("none", new Pair(0, 0)),
    EMPIRE("empire", new Pair(46526, 30134)),
    DWARFS("dwarfs", new Pair(46526, 30134)),
    BEASTMEN("beastmen", new Pair(46526, 30134));

    private final String id;
    private final Pair spawnPos;

    Faction(String id, Pair spawnPos) {
        this.id = id;
        this.spawnPos = spawnPos;
    }

    public String id() {
        return id;
    }

    public Pair spawnPos() {
        return spawnPos;
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
