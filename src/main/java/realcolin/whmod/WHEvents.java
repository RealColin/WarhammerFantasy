package realcolin.whmod;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import realcolin.whmod.entity.combat.CombatController;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.network.OpenFactionScreenPayload;

import java.util.Locale;

@EventBusSubscriber(modid = WHMod.MOD_ID)
public class WHEvents {

    @SubscribeEvent
    public static void tickEntities(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;

        if (livingEntity.level().isClientSide()) return;

        CombatController.tickCombat(livingEntity);
    }

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
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                        .then(Commands.literal("get").executes(ctx -> {
                            var player = ctx.getSource().getPlayerOrException();
                            var fac = player.getData(WHMod.FACTION_ATTACHMENT);

                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("Your faction is: " + fac.name()), false
                            );

                            return 1;
                        }))
                        .then(Commands.literal("set")
                                .then(Commands.argument("faction", StringArgumentType.word())
                                        .suggests((_, builder) -> {
                                            for (var fac : Faction.values()) {
                                                if (fac == Faction.NONE) continue;

                                                var name = fac.name().toLowerCase(Locale.ROOT);
                                                if (name.startsWith(builder.getRemainingLowerCase())) {
                                                    builder.suggest(fac.name());
                                                }
                                            }
                                            return builder.buildFuture();
                                        }).executes(ctx -> {
                                            var player = ctx.getSource().getPlayerOrException();
                                            var facName = StringArgumentType.getString(ctx, "faction");
                                            var fac = Faction.valueOf(facName);

                                            player.setData(WHMod.FACTION_ATTACHMENT, fac);

                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("Your faction has been set to: " + fac.name()), false
                                            );

                                            return 1;
                                        })))
        );

    }
}
