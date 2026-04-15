package realcolin.whmod.entity.npc;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.constant.DefaultAnimations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.faction.FactionRelationship;
import realcolin.whmod.faction.FactionRelationships;
import realcolin.whmod.util.CombatHelper;

public abstract class NPC extends PathfinderMob implements GeoEntity {
    private boolean equipmentInitialized = false;

    // melee attack stuff
    private boolean attacking = false;
    private int attackTick = 0;
    private int attackDurationTicks = 0;
    private int damageTick = 0;
    private boolean damageApplied = false;

    protected NPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
    }

    public abstract Faction getFaction();
    @SuppressWarnings("unused")
    public abstract Gender getGender();
    public abstract void initializeEquipment();

    public boolean isEnemy(LivingEntity other) {
        if (other == this) return false;

        if (other instanceof NPC npc) {
            var relationship = FactionRelationships.get(this.getFaction(), npc.getFaction());
            return relationship == FactionRelationship.UNFRIENDLY || relationship == FactionRelationship.HOSTILE;
        } else if (other instanceof Player player) {
            Faction playerFac = player.getData(WHMod.FACTION_ATTACHMENT);

            var relationship = FactionRelationships.get(this.getFaction(), playerFac);
            return relationship == FactionRelationship.UNFRIENDLY || relationship == FactionRelationship.HOSTILE;
        }

        return false;
    }

    public void beginAttack() {
        if (attacking || this.getTarget() == null) return;

        var attackSpeed = this.getAttributeValue(Attributes.ATTACK_SPEED);

        this.attackDurationTicks = Math.max(1, Mth.floor((20.0 / attackSpeed)));
        this.damageTick = Math.max(1, Mth.floor(this.attackDurationTicks * 0.6));

        this.attackTick = 0;
        this.damageApplied = false;
        this.attacking = true;

        playAttackAnimation();
    }

    private void attackTick() {
        attackTick++;

        if (!damageApplied && attackTick >= damageTick) {
            tryApplyAttackDamage();
            damageApplied = true;
        }

        if (attackTick >= attackDurationTicks) {
            attacking = false;
            attackTick = 0;
            attackDurationTicks = 0;
            damageTick = 0;
            damageApplied = false;
        }
    }

    private void tryApplyAttackDamage() {
        var target = this.getTarget();

        if (target == null || !target.isAlive())
            return;
        if (!CombatHelper.withinAttackRange(this, target))
            return;
        if (!this.hasLineOfSight(target))
            return;

        this.doHurtTarget((ServerLevel)level(), target);
    }

    private void playAttackAnimation() {
        // TODO handle different attack animations based on Weapon and Combo
        stopTriggeredAnim("attack_controller", "attack");
        triggerAnim("attack_controller", "attack");
    }

    @SuppressWarnings("resource")
    @Override
    public void tick() {
        // equipment initialized here because the attributes don't get applied properly if done in constructor
        if (!this.level().isClientSide() && !equipmentInitialized) {
            initializeEquipment();
            equipmentInitialized = true;
        }
        super.tick();

        if (!this.level().isClientSide() && attacking)
            attackTick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("initializedEquipment", equipmentInitialized);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        equipmentInitialized = input.getBooleanOr("initializedEquipment", false);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericWalkIdleController());

        var attackController = new AnimationController<>("attack_controller", 0, _ -> PlayState.STOP)
                .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING);

        // TODO figure out workaround for this
//                .setAnimationSpeed(state -> {
//                    var attackSpeed = this.getAttributeValue(Attributes.ATTACK_SPEED);
//                    int attackTicks = Math.max(1, Mth.floor((float) (20.0 / attackSpeed)));
//                    return 20.0 / attackTicks;
//                });

        controllers.add(attackController);
    }
}
