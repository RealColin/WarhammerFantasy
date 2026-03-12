package realcolin.whmod.faction;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import realcolin.whmod.util.Pair;

public enum Faction {
    NONE("none", new Pair(0, 0), Component.translatable("faction.whmod.none.desc"), Component.translatable("faction.whmod.none.traits")),
    EMPIRE("empire", new Pair(46526, 30134), Component.translatable("faction.whmod.empire.desc"), Component.translatable("faction.whmod.empire.traits")),
    DWARFS("dwarfs", new Pair(46526, 30134), Component.translatable("faction.whmod.dwarfs.desc"), Component.translatable("faction.whmod.dwarfs.traits")),
    BEASTMEN("beastmen", new Pair(46526, 30134), Component.translatable("faction.whmod.beastmen.desc"), Component.translatable("faction.whmod.beastmen.traits"));

    private final String id;
    private final Pair spawnPos;
    private final Component description;
    private final Component traits;

    Faction(String id, Pair spawnPos, Component description, Component traits) {
        this.id = id;
        this.spawnPos = spawnPos;
        this.description = description;
        this.traits = traits;
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

    public Component traits() {
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
