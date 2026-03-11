package realcolin.whmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;

public record SelectFactionPayload(Faction faction) implements CustomPacketPayload {

    public static final Type<SelectFactionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "select_faction"));

    public static final StreamCodec<ByteBuf, SelectFactionPayload> STREAM_CODEC =
            StreamCodec.composite(
                    Faction.STREAM_CODEC,
                    SelectFactionPayload::faction,
                    SelectFactionPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
