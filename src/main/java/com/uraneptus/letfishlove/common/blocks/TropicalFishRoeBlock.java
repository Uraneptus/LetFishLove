package com.uraneptus.letfishlove.common.blocks;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;

public class TropicalFishRoeBlock extends RoeBlock {
    private int fishVariant;
    private boolean fromBreeding = false;

    public TropicalFishRoeBlock(EntityType<?> fish, Properties properties) {
        super(fish, properties);
    }

    @Override
    protected void spawnFish(ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int i = pRandom.nextInt(calculateHatchAmount(pLevel).getMinValue(), calculateHatchAmount(pLevel).getMaxValue());

        for(int j = 1; j <= i; ++j) {
            if (getFish() != null && getFish().create(pLevel) instanceof TropicalFish tropicalFish) {
                double d0 = (double)pPos.getX() + this.getRandomPositionOffset(pRandom);
                double d1 = (double)pPos.getZ() + this.getRandomPositionOffset(pRandom);
                int k = pRandom.nextInt(1, 361);
                tropicalFish.moveTo(d0, (double)pPos.getY() - 0.5D, d1, (float)k, 0.0F);
                tropicalFish.setPersistenceRequired();
                if (fromBreeding) {
                    tropicalFish.setVariant(this.getFishVariant());
                } else {
                    tropicalFish.setVariant(Util.getRandom(TropicalFish.COMMON_VARIANTS, pRandom));
                }

                pLevel.addFreshEntity(tropicalFish);
            }
        }
    }

    public int getFishVariant() {
        return this.fishVariant;
    }

    public void setFishVariant(int variant) {
        this.fishVariant = variant;
        this.fromBreeding = true;
    }
}
