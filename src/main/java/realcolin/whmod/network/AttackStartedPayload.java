package realcolin.whmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.WHMod;

public record AttackStartedPayload(int entityId, String attackId, double attackSpeed) implements CustomPacketPayload {
    public static final Type<AttackStartedPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(
                    WHMod.MOD_ID,
                    "attack_started"
            ));

    public static final StreamCodec<ByteBuf, AttackStartedPayload> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    AttackStartedPayload::entityId,
                    ByteBufCodecs.STRING_UTF8,
                    AttackStartedPayload::attackId,
                    ByteBufCodecs.DOUBLE,
                    AttackStartedPayload::attackSpeed,
                    AttackStartedPayload::new
            );


    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
