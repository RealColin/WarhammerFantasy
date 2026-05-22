package realcolin.whmod.network;

import io.netty.util.Constant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import realcolin.whmod.Constants;
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
        var spawnX = spawnPos.a() * Constants.BLOCKS_PER_INCH;
        var spawnZ = spawnPos.b() * Constants.BLOCKS_PER_INCH;
        var spawn = findSafeSpawn(mallus, spawnX, spawnZ);
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

    private static BlockPos findSafeSpawn(ServerLevel level, int targetX, int targetZ) {
        int maxRadius = 64;

        // Make sure the target chunk is generated/loaded.
        level.getChunk(targetX >> 4, targetZ >> 4);

        for (int radius = 0; radius <= maxRadius; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    // Only check the outer edge of each radius square.
                    if (Math.abs(dx) != radius && Math.abs(dz) != radius) continue;

                    int x = targetX + dx;
                    int z = targetZ + dz;

                    BlockPos pos = findSafeSpawnInColumn(level, x, z);
                    if (pos != null) {
                        return pos;
                    }
                }
            }
        }

        // Last-resort fallback. Better than returning maxY exactly.
        return new BlockPos(targetX, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetX, targetZ), targetZ);
    }

    private static BlockPos findSafeSpawnInColumn(ServerLevel level, int x, int z) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

        // Check a small vertical range around the heightmap result.
        for (int dy = 4; dy >= -8; dy--) {
            BlockPos feet = new BlockPos(x, y + dy, z);

            if (isSafeSpawn(level, feet)) {
                return feet;
            }
        }

        // Fallback to full vertical scan if heightmap failed.
        for (int scanY = level.getMaxY() - 1; scanY >= level.getMinY() + 1; scanY--) {
            BlockPos feet = new BlockPos(x, scanY, z);

            if (isSafeSpawn(level, feet)) {
                return feet;
            }
        }

        return null;
    }

    private static boolean isSafeSpawn(ServerLevel level, BlockPos feet) {
        BlockPos head = feet.above();
        BlockPos floor = feet.below();

        BlockState feetState = level.getBlockState(feet);
        BlockState headState = level.getBlockState(head);
        BlockState floorState = level.getBlockState(floor);

        if (!level.getWorldBorder().isWithinBounds(feet)) return false;

        // Need solid ground.
        if (!floorState.blocksMotion()) return false;

        // Need room for the player.
        if (!feetState.getCollisionShape(level, feet).isEmpty()) return false;
        if (!headState.getCollisionShape(level, head).isEmpty()) return false;

        // Avoid liquids.
        if (!level.getFluidState(feet).isEmpty()) return false;
        if (!level.getFluidState(floor).isEmpty()) return false;

        // Avoid lava/fire-like bad blocks if desired.
        if (level.getFluidState(feet).is(FluidTags.LAVA)) return false;
        if (level.getFluidState(floor).is(FluidTags.LAVA)) return false;

        return true;
    }
}
