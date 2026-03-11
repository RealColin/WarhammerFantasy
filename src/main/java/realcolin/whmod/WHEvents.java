package realcolin.whmod;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.OpenFactionScreenPayload;

@EventBusSubscriber(modid = WHMod.MOD_ID)
public class WHEvents {

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        var level = event.getLevel();
        var entity = event.getEntity();

        if (!(entity instanceof Player player) || !(level instanceof ServerLevel)) return;

        var faction = player.getData(WHMod.FACTION_ATTACHMENT);

        if (faction == Faction.NONE) {
            PacketDistributor.sendToPlayer((ServerPlayer)player, new OpenFactionScreenPayload());
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("faction")
                        .executes(ctx -> {
                            var player = ctx.getSource().getPlayerOrException();
                            var fac = player.getData(WHMod.FACTION_ATTACHMENT);

                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("Faction: " + fac.name()), false
                            );

                            return 1;
                        })
        );
    }
}
