package realcolin.whmod.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.WHItems;
import realcolin.whmod.worldgen.tree.WHTreeGrowers;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public class WHBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WHMod.MOD_ID);

    public static final List<WoodSet> woodSets = new ArrayList<>();

    public static final WoodSet PINE = WoodSet.make("pine", WHTreeGrowers.PINE);
    public static final WoodSet BEECH = WoodSet.make("beech", WHTreeGrowers.BEECH);
    public static final WoodSet ELM = WoodSet.make("elm", WHTreeGrowers.PINE);

    public static final Map<Faction, DeferredBlock<FactionCraftingTableBlock>> CRAFTING_TABLE_MAP = craftingTableMap();

    private static DeferredBlock<Block> blockItem(String name, Function<Identifier, ? extends Block> func) {

        DeferredBlock<Block> block = BLOCKS.register(name, func);
        WHItems.ITEMS.register(name, res -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));

        return block;
    }

    private static Map<Faction, DeferredBlock<FactionCraftingTableBlock>> craftingTableMap() {
        var ret = new HashMap<Faction, DeferredBlock<FactionCraftingTableBlock>>();

        for (var fac : Faction.values()) {
            var name = fac.adjective().toLowerCase(Locale.ROOT) + "_crafting_table";
            var block = BLOCKS.registerBlock(name,
                    (props) -> new FactionCraftingTableBlock(
                            props
                                    .mapColor(MapColor.WOOD)
                                    .instrument(NoteBlockInstrument.BASS)
                                    .strength(2.5F)
                                    .sound(SoundType.WOOD)
                                    .ignitedByLava(),
                            fac));
            var item = WHItems.ITEMS.register(name,
                    res -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
            ret.put(fac, block);
        }
        return ret;
    }
}
