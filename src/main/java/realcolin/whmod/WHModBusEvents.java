package realcolin.whmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.CloseScreenPayload;
import realcolin.whmod.network.OpenFactionScreenPayload;
import realcolin.whmod.network.SelectFactionPayload;

@EventBusSubscriber(modid = WHMod.MOD_ID)
public class WHModBusEvents {

    // TODO move this somewhere else
    private static final ResourceKey<Level> MALLUS = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "mallus"));

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(OpenFactionScreenPayload.TYPE, OpenFactionScreenPayload.STREAM_CODEC);
        registrar.playToClient(CloseScreenPayload.TYPE, CloseScreenPayload.STREAM_CODEC);
        registrar.playToServer(SelectFactionPayload.TYPE, SelectFactionPayload.STREAM_CODEC, WHModBusEvents::handleSelectFactionPayload);
    }

    // TODO probably move this somewhere else
    private static void handleSelectFactionPayload(SelectFactionPayload payload, final IPayloadContext context) {
        var player = context.player();

        if (!(player instanceof ServerPlayer serverPlayer)) return;

        var fac = payload.faction();
        if (fac == Faction.NONE) fac = Faction.EMPIRE; // this is only temporary until I think of a better fix

        if (serverPlayer.getData(WHMod.FACTION_ATTACHMENT) != Faction.NONE) return;

        serverPlayer.setData(WHMod.FACTION_ATTACHMENT, fac);

        var mallus = serverPlayer.getServer().getLevel(MALLUS);
        var spawn = findSafeSpawn(mallus, 46526, 30134);
        PacketDistributor.sendToPlayer(serverPlayer, new CloseScreenPayload());

        var transition = new TeleportTransition(
                mallus,
                new Vec3(spawn),
                Vec3.ZERO,
                serverPlayer.getYRot(),
                serverPlayer.getXRot(),
                TeleportTransition.DO_NOTHING
        );

        serverPlayer.teleport(transition);

        var respawn = new ServerPlayer.RespawnConfig(
                MALLUS,
                spawn,
                0.0F,
                true
        );

        serverPlayer.setRespawnPosition(respawn, false);
    }

    // TODO probably move this somewhere else
    private static BlockPos findSafeSpawn(ServerLevel level, int x, int z) {
        for (int y = level.getMaxY(); y >= level.getMinY() + 1; y--) {
            BlockPos feet = new BlockPos(x, y, z);
            BlockPos head = feet.above();
            BlockPos floor = feet.below();

            var feetState = level.getBlockState(feet);
            var headState = level.getBlockState(head);
            var floorState = level.getBlockState(floor);

            // TODO change blocksMotion to isValidSpawn or whatever maybe?
            if (floorState.blocksMotion() && feetState.isAir() && headState.isAir()) {
                return feet;
            }
        }

        return new BlockPos(x, level.getMaxY(), z);
    }
}
