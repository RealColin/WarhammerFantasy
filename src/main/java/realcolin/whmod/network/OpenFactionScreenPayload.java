package realcolin.whmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;

public record OpenFactionScreenPayload() implements CustomPacketPayload {

    public static final Type<OpenFactionScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "open_faction_screen"));

    public static final StreamCodec<ByteBuf, OpenFactionScreenPayload> STREAM_CODEC =
            StreamCodec.unit(new OpenFactionScreenPayload());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
