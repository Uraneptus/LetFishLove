package com.uraneptus.letfishlove.core.other;

import com.uraneptus.letfishlove.LetFishLoveMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LFLBlockTags {
    public static final TagKey<Block> COD = create("cod");
    public static final TagKey<Block> SALMON = create("salmon");
    public static final TagKey<Block> PUFFERFISH = create("pufferfish");
    public static final TagKey<Block> TROPICAL_FISH = create("tropical_fish");

    public static TagKey<Block> create(String fishName) {
        return TagKey.create(Registries.BLOCK, LetFishLoveMod.modPrefix("fish_roe/minecraft/" + fishName));
    }

}
