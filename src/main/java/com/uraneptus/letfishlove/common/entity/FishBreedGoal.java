package com.uraneptus.letfishlove.common.entity;

import com.uraneptus.letfishlove.common.capabilities.AbstractFishCap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class FishBreedGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
    protected final AbstractFish fish;
    private final Class<? extends AbstractFish> partnerClass;
    protected final Level level;
    @Nullable
    protected AbstractFish partner;
    private int loveTime;
    private final double speedModifier;

    public FishBreedGoal(AbstractFish fish, double pSpeedModifier) {
        this(fish, pSpeedModifier, fish.getClass());
    }

    public FishBreedGoal(AbstractFish fish, double pSpeedModifier, Class<? extends AbstractFish> pPartnerClass) {
        this.fish = fish;
        this.level = fish.level;
        this.partnerClass = pPartnerClass;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        if (!FishBreedingUtil.isInLove(this.fish)) {
            return false;
        } else {
            this.partner = this.getFreePartner();
            return this.partner != null;
        }
    }

    public boolean canContinueToUse() {
        return this.partner.isAlive() && FishBreedingUtil.isInLove(partner) && this.loveTime < 60;
    }

    public void stop() {
        this.partner = null;
        this.loveTime = 0;
    }

    public void tick() {
        this.fish.getLookControl().setLookAt(this.partner, 10.0F, (float)this.fish.getMaxHeadXRot());
        this.fish.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.loveTime;
        if (this.loveTime >= this.adjustedTickDelay(60) && this.fish.distanceToSqr(this.partner) < 9.0D) {
            this.breed();
        }
    }

    @Nullable
    private AbstractFish getFreePartner() {
        List<? extends AbstractFish> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.fish, this.fish.getBoundingBox().inflate(8.0D));
        double d0 = Double.MAX_VALUE;
        AbstractFish fish = null;

        for(AbstractFish otherFish : list) {
            if (FishBreedingUtil.canMate(fish, otherFish) && this.fish.distanceToSqr(otherFish) < d0) {
                fish = otherFish;
                d0 = this.fish.distanceToSqr(otherFish);
            }
        }

        return fish;
    }

    protected void breed() {
        FishBreedingUtil.spawnFishFromBreeding((ServerLevel) this.level, this.fish, this.partner);
    }
}