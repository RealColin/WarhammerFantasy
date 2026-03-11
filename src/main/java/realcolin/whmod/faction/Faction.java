package realcolin.whmod.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum Faction {
    NONE("none"),
    EMPIRE("empire"),
    DWARFS("dwarfs"),
    BEASTMEN("beastmen");

    private final String id;

    Faction(String id) {
        this.id = id;
    }

    public String id() {
        return id;
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
