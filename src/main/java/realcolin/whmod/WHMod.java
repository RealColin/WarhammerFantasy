package realcolin.whmod;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.block.WHBlocks;
import realcolin.whmod.entity.WHEntities;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.WHCreativeTabs;
import realcolin.whmod.item.WHItems;
import realcolin.whmod.menu.WHMenuTypes;
import realcolin.whmod.worldgen.biome.WHBiomeSource;
import realcolin.whmod.worldgen.densityfunction.*;

import java.util.function.Supplier;

@Mod(WHMod.MOD_ID)
public class WHMod {
    public static final String MOD_ID = "whmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static final Supplier<AttachmentType<Faction>> FACTION_ATTACHMENT = ATTACHMENT_TYPES.register(
            "faction", () -> AttachmentType.builder(() -> Faction.NONE)
                    .serialize(Faction.CODEC.fieldOf("faction"))
                    .sync((holder, player) -> holder == player, Faction.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    public WHMod(IEventBus modEventBus, ModContainer modContainer) {
        WHBlocks.BLOCKS.register(modEventBus);
        WHItems.ITEMS.register(modEventBus);
        WHMenuTypes.MENU_TYPES.register(modEventBus);
        WHCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        WHEntities.ENTITY_TYPES.register(modEventBus);
        WHDensityFunctions.DENSITY_FUNCTIONS.register(modEventBus);
        WHBiomeSource.BIOME_SOURCES.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
