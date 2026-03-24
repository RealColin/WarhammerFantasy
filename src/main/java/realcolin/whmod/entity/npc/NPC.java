package realcolin.whmod.entity.npc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import realcolin.whmod.faction.Faction;

public abstract class NPC extends PathfinderMob {
    protected NPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public abstract Faction getFaction();
    public abstract ResourceLocation getTextureLocation();
    public abstract Gender getGender();

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }
}
