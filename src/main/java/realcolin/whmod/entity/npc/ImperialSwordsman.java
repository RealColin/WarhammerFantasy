package realcolin.whmod.entity.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import realcolin.whmod.WHMod;
import realcolin.whmod.entity.npc.goal.TargetEnemyGoal;
import realcolin.whmod.faction.Faction;

public class ImperialSwordsman extends NPC {
    private final ResourceLocation TEXTURE_LOCATION =
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "textures/entity/npc/human/imperial_swordsman.png");

    public ImperialSwordsman(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3F, true));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new TargetEnemyGoal(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    public Faction getFaction() {
        return Faction.EMPIRE;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE_LOCATION;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public void initializeEquipment() {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }
}
