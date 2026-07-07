package realcolin.whmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;
import realcolin.whmod.client.renderer.*;
import realcolin.whmod.client.screen.FactionCraftingScreen;
import realcolin.whmod.client.screen.InGameMenuScreen;
import realcolin.whmod.entity.WHEntities;
import realcolin.whmod.menu.WHMenuTypes;
import realcolin.whmod.network.CloseScreenPayload;
import realcolin.whmod.network.OpenFactionScreenPayload;

import java.util.List;

@Mod(value = WHMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = WHMod.MOD_ID, value = Dist.CLIENT)
public class WHClient {
    public WHClient(ModContainer container) {

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(WHEntities.BOAR.get(), BoarRenderer::new);
        EntityRenderers.register(WHEntities.BROWN_BEAR.get(), BrownBearRenderer::new);
        EntityRenderers.register(WHEntities.IMPERIAL_SWORDSMAN.get(), ctx -> new NPCRenderer<>(ctx, "npc/human/imperial_swordsman"));
        EntityRenderers.register(WHEntities.UNGOR.get(), ctx -> new NPCRenderer<>(ctx, "npc/beastmen/ungor"));
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(WHMenuTypes.FACTION_CRAFTING.get(), FactionCraftingScreen::new);
    }

    @SubscribeEvent
    static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {

        event.register(List.of(
                BlockTintSources.foliage()
        ),
                WHBlocks.PINE.leaves().get(),
                WHBlocks.BEECH.leaves().get(),
                WHBlocks.ELM.leaves().get());
    }

    @SubscribeEvent
    static void registerClientPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(OpenFactionScreenPayload.TYPE, HandlerThread.NETWORK, ClientPayloadHandler::handleOpenFactionScreen);
        event.register(CloseScreenPayload.TYPE, HandlerThread.NETWORK, ClientPayloadHandler::handleCloseScreen);
    }

    @SubscribeEvent
    static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        WHKeybinds.init();
        event.register(WHKeybinds.OPEN_MENU);
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        while (WHKeybinds.OPEN_MENU.consumeClick()) {
            var mc = Minecraft.getInstance();

            if (mc.level == null) break;

            if (mc.screen instanceof InGameMenuScreen)
                mc.setScreen(null);
            else if (mc.screen == null)
                mc.setScreen(new InGameMenuScreen());
        }
    }

    @SubscribeEvent
    static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isAttack())
            return;

        var mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        // if breaking blocks, then use normal minecraft block break logic
        var hitResult = mc.hitResult;
        var stack = mc.player.getMainHandItem();

        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && canMine(stack)) {
            System.out.println("Swinging at block.");
            return;
        }

        // if not breaking blocks, send a custom attack swing packet
        System.out.println("Click canceled");
        event.setSwingHand(false);
        event.setCanceled(true);
    }

    private static boolean canMine(ItemStack stack) {
        return !(stack.is(ItemTags.SPEARS) || stack.is(Items.MACE));
    }
}
