package realcolin.whmod.client.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import realcolin.whmod.WHMod;

public record StartAttackPayload() implements CustomPacketPayload {

    public static final Type<StartAttackPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "start_attack")
    );

    public static final StreamCodec<ByteBuf, StartAttackPayload> STREAM_CODEC =
            StreamCodec.unit(new StartAttackPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
