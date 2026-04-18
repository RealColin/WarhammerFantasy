package realcolin.whmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.menu.FactionCraftingMenu;

public class FactionCraftingTableBlock extends Block {
    private final Faction faction;

    public FactionCraftingTableBlock(Properties properties, Faction faction) {
        super(properties);
        this.faction = faction;
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            if (canPlayerOpen(player)) {
                player.openMenu(state.getMenuProvider(level, pos));
            } else {
                player.sendOverlayMessage(Component.literal("Wrong faction!"));
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos) {
        var title = Component.translatable("container." + faction.id() + "_faction_crafting");

        return new SimpleMenuProvider((containerId, inventory, _) ->
                new FactionCraftingMenu(containerId, inventory, ContainerLevelAccess.create(level, pos), faction), title);
    }

    // Right now this seems silly since it's just a simple boolean that could be done inline,
    // but when I implement faction reputation, the player will be able to use crafting tables
    // belonging to other factions as well, and I will need more if checks for that and I
    // decided it will be cleaner to put it in this separate method
    private boolean canPlayerOpen(Player player) {
        var playerFac = player.getData(WHMod.FACTION_ATTACHMENT);
        return playerFac == faction;
    }

    public Faction getFaction() {
        return this.faction;
    }
}
