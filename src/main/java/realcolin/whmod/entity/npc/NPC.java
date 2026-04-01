package realcolin.whmod.entity.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
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
import software.bernie.geckolib.animatable.GeoEntity;

public abstract class NPC extends PathfinderMob implements GeoEntity {
    private boolean equipmentInitialized = false;

    protected NPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
    }

    public abstract Faction getFaction();
    public abstract ResourceLocation getTextureLocation();
    public abstract Gender getGender();
    public abstract void initializeEquipment();
    public abstract void startAttack(LivingEntity target);

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

    @SuppressWarnings("resource")
    @Override
    public void tick() {
        if (!this.level().isClientSide() && !equipmentInitialized) {
            initializeEquipment();
            equipmentInitialized = true;
        }
        super.tick();
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
}
