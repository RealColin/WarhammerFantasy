package realcolin.whmod.entity.npc;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import realcolin.whmod.entity.npc.goal.NPCLookGoal;
import realcolin.whmod.entity.npc.goal.NPCMeleeAttackGoal;
import realcolin.whmod.entity.npc.goal.TargetEnemyGoal;
import realcolin.whmod.faction.Faction;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ImperialSwordsman extends NPC {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public ImperialSwordsman(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new NPCMeleeAttackGoal(this, 1.3F));
        this.goalSelector.addGoal(2, new NPCLookGoal(this, 6.0));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.F));
        this.targetSelector.addGoal(1, new TargetEnemyGoal(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.ATTACK_SPEED, 4.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public Faction getFaction() {
        return Faction.EMPIRE;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public void initializeEquipment() {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
