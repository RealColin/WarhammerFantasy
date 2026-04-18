package realcolin.whmod.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.WHItems;
import realcolin.whmod.worldgen.tree.WHTreeGrowers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class WHBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WHMod.MOD_ID);

    public static final List<WoodSet> woodSets = new ArrayList<>();

    public static final WoodSet PINE = WoodSet.make("pine", WHTreeGrowers.PINE);
    public static final WoodSet BEECH = WoodSet.make("beech", WHTreeGrowers.BEECH);
    public static final WoodSet ELM = WoodSet.make("elm", WHTreeGrowers.PINE);


    public static final List<DeferredBlock<FactionCraftingTableBlock>> CRAFTING_TABLES = craftingTables();

    private static DeferredBlock<Block> blockItem(String name, Function<Identifier, ? extends Block> func) {

        DeferredBlock<Block> block = BLOCKS.register(name, func);
        WHItems.ITEMS.register(name, res -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));

        return block;
    }

    private static List<DeferredBlock<FactionCraftingTableBlock>> craftingTables() {
        var ret = new ArrayList<DeferredBlock<FactionCraftingTableBlock>>();

        for (var fac : Faction.values()) {
            var name = fac.id() + "_crafting_table";
            var block = BLOCKS.registerBlock(name,
                    (props) -> new FactionCraftingTableBlock(props, fac));
            var item = WHItems.ITEMS.register(name,
                    res -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
            ret.add(block);
        }

        return ret;
    }
}
