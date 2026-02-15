package realcolin.whmod.block;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import realcolin.whmod.WHMod;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class WHBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WHMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WHMod.MOD_ID);

    public static final DeferredBlock<Block> PINE_LOG = log("pine");
    public static final DeferredBlock<Block> BEECH_LOG = log("beech");

    private static DeferredBlock<Block> log(String wood) {
        var name = wood + "_log";

        return blockItem(name, res -> new FlammableRotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(p_152624_ -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.PODZOL : MapColor.COLOR_BROWN)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));
    }

    private static DeferredBlock<Block> blockItem(String name, Function<ResourceLocation, ? extends Block> func) {

        DeferredBlock<Block> block = BLOCKS.register(name, func);
        ITEMS.register(name, res -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));

        return block;
    }
}
