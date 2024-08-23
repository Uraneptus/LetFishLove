package com.uraneptus.letfishlove.core.events;

import com.uraneptus.letfishlove.LetFishLoveMod;
import com.uraneptus.letfishlove.common.capabilities.FishBreedingCap;
import com.uraneptus.letfishlove.common.entity.FishBreedGoal;
import com.uraneptus.letfishlove.common.entity.FishBreedingUtil;
import com.uraneptus.letfishlove.common.entity.FishLayRoeGoal;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = LetFishLoveMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    @SubscribeEvent
    public static void onEnityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemInHand = player.getItemInHand(hand);
        Entity target = event.getTarget();
        Level level = event.getLevel();

        if (target instanceof WaterAnimal fish && FishBreedingUtil.isBreedable(fish)) {
            TagKey<Item> temptationItems = FishBreedingUtil.getTemptationItems(fish.getType());
            if (temptationItems != null) {
                if (Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).isKnownTagName(temptationItems) && itemInHand.is(temptationItems)) {
                    FishBreedingCap fishCap = FishBreedingUtil.getFishCap(fish);
                    if (fishCap.canFallInLove()) {
                        fishCap.setInLove(fish, player, level);
                        FishBreedingUtil.usePlayerItem(player, itemInHand);
                        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof WaterAnimal fish && FishBreedingUtil.isBreedable(fish)) {
            FishBreedingCap cap = FishBreedingUtil.getFishCap(fish);
            if (cap.getCanLoveCooldown() > 0) {
                cap.setCanLoveCooldown(cap.getCanLoveCooldown() - 1, true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof WaterAnimal fish && FishBreedingUtil.isBreedable(fish)) {
            TagKey<Item> temptationItems = FishBreedingUtil.getTemptationItems(fish.getType());
            if (temptationItems != null) {
                fish.goalSelector.addGoal(0, new TemptGoal(fish, 1.2D, Ingredient.of(temptationItems), false));
                fish.goalSelector.addGoal(0, new FishBreedGoal(fish, 1.0D));
                fish.goalSelector.addGoal(0, new FishLayRoeGoal(fish));
            }
        }
    }
}
