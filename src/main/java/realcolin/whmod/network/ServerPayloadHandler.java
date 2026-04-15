package realcolin.whmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.worldgen.WHDimensions;

@SuppressWarnings("deprecation")
public class ServerPayloadHandler {

    public static void handleSelectFactionPayload(SelectFactionPayload payload, final IPayloadContext context) {
        var player = context.player();

        if (!(player instanceof ServerPlayer serverPlayer)) return;

        var fac = payload.faction();
        if (fac == Faction.NONE) fac = Faction.EMPIRE; // this is only temporary until I think of a better fix

        if (serverPlayer.getData(WHMod.FACTION_ATTACHMENT) != Faction.NONE) return;

        serverPlayer.setData(WHMod.FACTION_ATTACHMENT, fac);

        var mallus = serverPlayer.level().getServer().getLevel(WHDimensions.MALLUS);

        if (mallus == null) return;

        var spawnPos = fac.spawnPos();
        var spawn = findSafeSpawn(mallus, spawnPos.a(), spawnPos.b());
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

        var globalPos = GlobalPos.of(WHDimensions.MALLUS, spawn);
        var respawnData = new LevelData.RespawnData(globalPos, serverPlayer.getXRot(), serverPlayer.getYRot());

        var respawn = new ServerPlayer.RespawnConfig(
                respawnData,
                true
        );

        serverPlayer.setRespawnPosition(respawn, false);
    }

    private static BlockPos findSafeSpawn(ServerLevel level, int x, int z) {
        for (int y = level.getMaxY(); y >= level.getMinY() + 1; y--) {
            BlockPos feet = new BlockPos(x, y, z);
            BlockPos head = feet.above();
            BlockPos floor = feet.below();

            var feetState = level.getBlockState(feet);
            var headState = level.getBlockState(head);
            var floorState = level.getBlockState(floor);

            if (floorState.blocksMotion() && feetState.isAir() && headState.isAir()) {
                return feet;
            }
        }

        return new BlockPos(x, level.getMaxY(), z);
    }
}
