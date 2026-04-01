package realcolin.whmod.entity.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import realcolin.whmod.WHMod;
import realcolin.whmod.entity.npc.goal.NPCMeleeAttackGoal;
import realcolin.whmod.entity.npc.goal.TargetEnemyGoal;
import realcolin.whmod.faction.Faction;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Ungor extends NPC {
    private final ResourceLocation TEXTURE_LOCATION =
            ResourceLocation.fromNamespaceAndPath(WHMod.MOD_ID, "textures/entity/npc/beastmen/ungor.png");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private boolean attacking = false;
    private int attackTick = 0;
    private int attackDurationTicks = 0;
    private int damageTick = 0;
    private boolean damageApplied = false;

    public Ungor(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new NPCMeleeAttackGoal(this, 1.3F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

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
        return Faction.BEASTMEN;
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
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    @Override
    public void startAttack(LivingEntity target) {
        if (attacking || this.getTarget() == null) return;

        var attackSpeed = this.getAttributeValue(Attributes.ATTACK_SPEED);

        this.attackDurationTicks = Math.max(1, Mth.floor((20.0 / attackSpeed)));
        this.damageTick = Math.max(1, Mth.floor(this.attackDurationTicks * 0.6));

        System.out.println("Duration: " + attackDurationTicks);
        System.out.println("Damage at tick: " + damageTick);

        this.attackTick = 0;
        this.damageApplied = false;
        this.attacking = true;

        stopTriggeredAnim("attack_controller", "attack");
        triggerAnim("attack_controller", "attack");
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && attacking) {
            tickAttack();
        }
    }

    private void tickAttack() {
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
        if (!withinAttackRange(target))
            return;
        if (!this.hasLineOfSight(target))
            return;

        this.doHurtTarget((ServerLevel)level(), target);
    }

    private boolean withinAttackRange(LivingEntity target) {
        if (target == null) return false;

        var reach = this.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);

        var eyePos = this.getEyePosition();
        var look = this.getViewVector(1.0F);
        var reachVec = eyePos.add(look.scale(reach));

        var hitbox = target.getBoundingBox().inflate(target.getPickRadius());

        var hit = hitbox.clip(eyePos, reachVec);

        return hit.isPresent();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericWalkIdleController());

        var attackController = new AnimationController<>("attack_controller", 0, state -> PlayState.STOP)
                .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING)
                .setAnimationSpeedHandler(state -> {
                    var attackSpeed = this.getAttributeValue(Attributes.ATTACK_SPEED);
                    int attackTicks = Math.max(1, Mth.floor((float) (20.0 / attackSpeed)));
                    return 20.0 / attackTicks;
                });

        controllers.add(attackController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
