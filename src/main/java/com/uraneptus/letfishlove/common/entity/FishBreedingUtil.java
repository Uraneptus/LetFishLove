package com.uraneptus.letfishlove.common.entity;

import com.uraneptus.letfishlove.LetFishLoveMod;
import com.uraneptus.letfishlove.common.capabilities.FishBreedingCap;
import com.uraneptus.letfishlove.common.capabilities.FishBreedingCapAttacher;
import com.uraneptus.letfishlove.core.other.LFLEntityTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class FishBreedingUtil {

    public static void usePlayerItem(Player pPlayer, ItemStack pStack) {
        if (!pPlayer.getAbilities().instabuild) {
            pStack.shrink(1);
        }
    }

    @Nullable
    public static TagKey<Item> getTemptationItems(EntityType<?> entityType) {
        ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (rl != null) {
            return TagKey.create(Registries.ITEM, LetFishLoveMod.modPrefix("fish_food/" + rl.getNamespace() + "/" + rl.getPath()));
        }
        return null;
    }

    @Nullable
    public static TagKey<Block> getRoeBlock(EntityType<?> entityType) {
        ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (rl != null) {
            return TagKey.create(Registries.BLOCK, LetFishLoveMod.modPrefix("fish_roe/" + rl.getNamespace() + "/" + rl.getPath()));
        }
        return null;
    }

    public static boolean isBreedable(WaterAnimal entity) {
        return ForgeRegistries.ENTITY_TYPES.tags().getTag(LFLEntityTags.BREEDABLE_FISH).contains(entity.getType());
    }

    public static FishBreedingCap getFishCap(WaterAnimal fish) {
        LazyOptional<FishBreedingCap> fishOptional = FishBreedingCapAttacher.getFishBreedingCapability(fish).cast();
        if (fishOptional.isPresent() || !fishOptional.resolve().isEmpty()) {
            return fishOptional.resolve().get();
        }
        return new FishBreedingCap(fish);
    }

    public static boolean canMate(WaterAnimal thisFish, WaterAnimal pOtherFish) {
        if (pOtherFish == thisFish) {
            return false;
        } else if (pOtherFish.getClass() != thisFish.getClass()) {
            return false;
        } else {
            return getFishCap(thisFish).isInLove() && getFishCap(pOtherFish).isInLove();
        }
    }

    public static void spawnFishFromBreeding(ServerLevel pLevel, WaterAnimal thisFish, WaterAnimal otherFish) {
        FishBreedingCap thisFishCap = getFishCap(thisFish);
        FishBreedingCap otherFishCap = getFishCap(otherFish);

        ServerPlayer serverplayer = thisFishCap.getLoveCause(pLevel);
        if (serverplayer == null && otherFishCap.getLoveCause(pLevel) != null) {
            serverplayer = otherFishCap.getLoveCause(pLevel);
        }

        if (serverplayer != null) {
            //Usually a trigger would be here
            //TODO figure out how to trigger this
            serverplayer.awardStat(Stats.ANIMALS_BRED);
        }

        thisFishCap.resetLove();
        otherFishCap.resetLove();
        thisFishCap.setCanLoveCooldown(6000, true);
        otherFishCap.setCanLoveCooldown(6000, true);
        thisFishCap.setPregnant(true, true);
        pLevel.broadcastEntityEvent(thisFish, (byte)18);
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            pLevel.addFreshEntity(new ExperienceOrb(pLevel, thisFish.getX(), thisFish.getY(), thisFish.getZ(), thisFish.getRandom().nextInt(7) + 1));
        }
    }
}
