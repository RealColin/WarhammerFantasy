package realcolin.whmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;

public record CloseScreenPayload() implements CustomPacketPayload {

    public static final Type<CloseScreenPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "close_screen"));

    public static final StreamCodec<ByteBuf, CloseScreenPayload> STREAM_CODEC =
            StreamCodec.unit(new CloseScreenPayload());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
