package realcolin.whmod.entity.npc;

import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.entity.npc.goal.NPCLookGoal;
import realcolin.whmod.entity.npc.goal.NPCMeleeAttackGoal;
import realcolin.whmod.entity.npc.goal.TargetEnemyGoal;
import realcolin.whmod.faction.Faction;

public class Ungor extends NPC {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Ungor(EntityType<? extends PathfinderMob> entityType, Level level) {
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

    @Override
    public Faction getFaction() {
        return Faction.BEASTMEN;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public void initializeEquipment() {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
