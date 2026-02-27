package realcolin.whmod.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;
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

    private static DeferredBlock<Block> blockItem(String name, Function<ResourceLocation, ? extends Block> func) {

        DeferredBlock<Block> block = BLOCKS.register(name, func);
        WHItems.ITEMS.register(name, res -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));

        return block;
    }
}
