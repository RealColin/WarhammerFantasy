package realcolin.whmod.data;

import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.entity.WHEntities;

import java.util.stream.Stream;

public class EntityLoot extends EntityLootSubProvider {
    protected EntityLoot(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        this.add(WHEntities.BOAR.get(), boarLoot());
        this.add(WHEntities.BROWN_BEAR.get(), brownBearLoot());
        this.add(WHEntities.IMPERIAL_SWORDSMAN.get(), npcLoot());
        this.add(WHEntities.UNGOR.get(), npcLoot());
    }

    private LootTable.Builder boarLoot() {
        var pork = LootItem.lootTableItem(Items.PORKCHOP)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition
                        .hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(
                                EntityFlagsPredicate.Builder.flags().setOnFire(true)
                        ))));

        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(pork));
    }

    private LootTable.Builder brownBearLoot() {

        return LootTable.lootTable();
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return WHEntities.ENTITY_TYPES.getEntries().stream().map(DeferredHolder::get);
    }

    private LootTable.Builder npcLoot() {
        return LootTable.lootTable();
    }
}
