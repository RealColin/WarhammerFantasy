package realcolin.whmod.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.block.WHBlocks;
import realcolin.whmod.item.WHItems;

import java.util.Collections;
import java.util.Optional;

public class WHModelProvider extends ModelProvider {
    public WHModelProvider(PackOutput output) {
        super(output, WHMod.MOD_ID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        for (var woodSet : WHBlocks.woodSets) {
            var log = woodSet.log();
            blockModels.woodProvider(log.get()).log(log.get());

            var wood = woodSet.wood();
            blockModels.woodProvider(log.get()).wood(wood.get());

            var strippedLog = woodSet.strippedLog();
            blockModels.woodProvider(strippedLog.get()).log(strippedLog.get());

            var stippedWood = woodSet.strippedWood();
            blockModels.woodProvider(strippedLog.get()).wood(stippedWood.get());

            blockModels.family(woodSet.planks().get())
                    .slab(woodSet.slab().get())
                    .stairs(woodSet.stairs().get())
                    .fence(woodSet.fence().get())
                    .fenceGate(woodSet.fenceGate().get())
                    .button(woodSet.button().get())
                    .pressurePlate(woodSet.pressurePlate().get())

                    .door(woodSet.door().get())
                    .trapdoor(woodSet.trapdoor().get());

            var leaves = woodSet.leaves();
            blockModels.createTintedLeaves(leaves.get(), TexturedModel.LEAVES, FoliageColor.FOLIAGE_DEFAULT);

            var sapling = woodSet.sapling();
            blockModels.createCrossBlock(sapling.get(), BlockModelGenerators.PlantType.NOT_TINTED);
            blockModels.registerSimpleFlatItemModel(sapling.get());
        }

        handheld(itemModels, WHItems.IMPERIAL_SWORD.get());
        armorSets(itemModels);

        craftingTables(blockModels);
    }

    private void handheld(ItemModelGenerators itemModels, Item item) {
        ModelTemplates.FLAT_HANDHELD_ITEM.create(
                ModelLocationUtils.getModelLocation(item),
                TextureMapping.layer0(item),
                itemModels.modelOutput
        );

        itemModels.itemModelOutput.accept(
                item,
                new CuboidItemModelWrapper.Unbaked(
                        ModelLocationUtils.getModelLocation(item),
                        Optional.empty(),
                        Collections.emptyList()
                )
        );
    }

    private void armorSets(ItemModelGenerators itemModels) {
        for (var set : WHItems.ARMOR_SETS) {
            itemModels.generateFlatItem(set.helmet().get(), ModelTemplates.FLAT_ITEM);
            itemModels.generateFlatItem(set.chestplate().get(), ModelTemplates.FLAT_ITEM);
            itemModels.generateFlatItem(set.leggings().get(), ModelTemplates.FLAT_ITEM);
            itemModels.generateFlatItem(set.boots().get(), ModelTemplates.FLAT_ITEM);
        }
    }

    private void craftingTables(BlockModelGenerators blockModels) {
        for (var table : WHBlocks.CRAFTING_TABLE_MAP.entrySet()) {
            var block = table.getValue().get();
            blockModels.createCraftingTableLike(block, block, WHModelProvider::table);
        }
    }

    private static TextureMapping table(Block table, Block nothing) {
        return new TextureMapping()
                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(table, "_front"))
                .put(TextureSlot.UP, TextureMapping.getBlockTexture(table, "_top"))
                .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(table, "_bottom"))
                .put(TextureSlot.EAST, TextureMapping.getBlockTexture(table, "_side"))
                .put(TextureSlot.WEST, TextureMapping.getBlockTexture(table, "_side"))
                .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(table, "_front"))
                .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(table, "_front"));
    }
}
