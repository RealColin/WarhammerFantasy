package realcolin.whmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import realcolin.whmod.item.WHItems;

public record WoodSet(
        DeferredBlock<Block> log,
        DeferredBlock<Block> wood,
        DeferredBlock<Block> strippedLog,
        DeferredBlock<Block> strippedWood,
        DeferredBlock<Block> planks,
        DeferredBlock<Block> slab,
        DeferredBlock<Block> stairs,
        DeferredBlock<Block> fence,
        DeferredBlock<Block> fenceGate,
        DeferredBlock<Block> button,
        DeferredBlock<Block> pressurePlate,
        DeferredBlock<Block> leaves,
        DeferredBlock<Block> sapling,

        DeferredItem<Item> logItem,
        DeferredItem<Item> woodItem,
        DeferredItem<Item> strippedLogItem,
        DeferredItem<Item> strippedWoodItem,
        DeferredItem<Item> planksItem,
        DeferredItem<Item> slabItem,
        DeferredItem<Item> stairsItem,
        DeferredItem<Item> fenceItem,
        DeferredItem<Item> fenceGateItem,
        DeferredItem<Item> buttonItem,
        DeferredItem<Item> pressurePlateItem,
        DeferredItem<Item> leavesItem,
        DeferredItem<Item> saplingItem
) {

    public static WoodSet make(String name) {
        String logName = name + "_log";
        DeferredBlock<Block> log = WHBlocks.BLOCKS.register(logName, res -> new FlammableRotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(p_152624_ -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.PODZOL : MapColor.COLOR_BROWN)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));

        String woodName = name + "_wood";
        DeferredBlock<Block> wood = WHBlocks.BLOCKS.register(woodName, res -> new FlammableRotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));

        String strippedLogName = "stripped_" + name + "_log";
        DeferredBlock<Block> strippedLog = WHBlocks.BLOCKS.register(strippedLogName, res -> new FlammableRotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(p_152624_ -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.PODZOL : MapColor.COLOR_BROWN)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));

        String strippedWoodName = "stripped_" + name + "_wood";
        DeferredBlock<Block> strippedWood = WHBlocks.BLOCKS.register(strippedWoodName, res -> new FlammableRotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));

        String planksName = name + "_planks";
        DeferredBlock<Block> planks = WHBlocks.BLOCKS.register(planksName, res -> new Block(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F, 3.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));

        String slabName = name + "_slab";
        DeferredBlock<Block> slab = WHBlocks.BLOCKS.register(slabName, res -> new SlabBlock(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F, 3.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));

        String stairsName = name + "_stairs";
        DeferredBlock<Block> stairs = WHBlocks.BLOCKS.register(stairsName, res -> new StairBlock(planks.get().defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(planks.get()).setId(ResourceKey.create(Registries.BLOCK, res))));

        String fenceName = name + "_fence";
        DeferredBlock<Block> fence = WHBlocks.BLOCKS.register(fenceName, res -> new FenceBlock(
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(planks.get().defaultMapColor())
                        .forceSolidOn()
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F, 3.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()));

        String fenceGateName = name + "_fence_gate";
        DeferredBlock<Block> fenceGate = WHBlocks.BLOCKS.register(fenceGateName, res -> new FenceGateBlock(WoodType.OAK,
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(planks.get().defaultMapColor())
                        .forceSolidOn()
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F, 3.0F)
                        .ignitedByLava()));

        String buttonName = name + "_button";
        DeferredBlock<Block> button = WHBlocks.BLOCKS.register(buttonName, res -> new ButtonBlock(BlockSetType.OAK, 30,
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .noCollission()
                        .strength(0.5F)
                        .pushReaction(PushReaction.DESTROY)));

        String pressurePlateName = name + "_pressure_plate";
        DeferredBlock<Block> pressurePlate = WHBlocks.BLOCKS.register(pressurePlateName, res -> new PressurePlateBlock(BlockSetType.OAK,
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(planks.get().defaultMapColor())
                        .forceSolidOn()
                        .instrument(NoteBlockInstrument.BASS)
                        .noCollission()
                        .strength(0.5F)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)));

        String leavesName = name + "_leaves";
        DeferredBlock<Block> leaves = WHBlocks.BLOCKS.register(leavesName, res -> new TintedParticleLeavesBlock(0.01f,
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(MapColor.PLANT)
                        .strength(0.2F).randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isValidSpawn(Blocks::ocelotOrParrot)
                        .isSuffocating(WoodSet::never)
                        .isViewBlocking(WoodSet::never)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(WoodSet::never)));

        String saplingName = name + "_sapling";
        DeferredBlock<Block> sapling = WHBlocks.BLOCKS.register(saplingName, res -> new SaplingBlock(TreeGrower.SPRUCE,
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, res))
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .pushReaction(PushReaction.DESTROY)));

        DeferredItem<Item> logItem = WHItems.ITEMS.register(logName,
                res -> new BlockItem(log.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> woodItem = WHItems.ITEMS.register(woodName,
                res -> new BlockItem(wood.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> strippedLogItem = WHItems.ITEMS.register(strippedLogName,
                res -> new BlockItem(strippedLog.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> strippedWoodItem = WHItems.ITEMS.register(strippedWoodName,
                res -> new BlockItem(strippedWood.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> planksItem = WHItems.ITEMS.register(planksName,
                res -> new BlockItem(planks.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> slabItem = WHItems.ITEMS.register(slabName,
                res -> new BlockItem(slab.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> stairsItem = WHItems.ITEMS.register(stairsName,
                res -> new BlockItem(stairs.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> fenceItem = WHItems.ITEMS.register(fenceName,
                res -> new BlockItem(fence.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> fenceGateItem = WHItems.ITEMS.register(fenceGateName,
                res -> new BlockItem(fenceGate.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> buttonItem = WHItems.ITEMS.register(buttonName,
                res -> new BlockItem(button.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> pressurePlateItem = WHItems.ITEMS.register(pressurePlateName,
                res -> new BlockItem(pressurePlate.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> leavesItem = WHItems.ITEMS.register(leavesName,
                res -> new BlockItem(leaves.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));
        DeferredItem<Item> saplingItem = WHItems.ITEMS.register(saplingName,
                res -> new BlockItem(sapling.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, res))));


        var set = new WoodSet(
                log,
                wood,
                strippedLog,
                strippedWood,
                planks,
                slab,
                stairs,
                fence,
                fenceGate,
                button,
                pressurePlate,
                leaves,
                sapling,
                logItem,
                woodItem,
                strippedLogItem,
                strippedWoodItem,
                planksItem,
                slabItem,
                stairsItem,
                fenceItem,
                fenceGateItem,
                buttonItem,
                pressurePlateItem,
                leavesItem,
                saplingItem
        );
        WHBlocks.woodSets.add(set);
        return set;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

}
