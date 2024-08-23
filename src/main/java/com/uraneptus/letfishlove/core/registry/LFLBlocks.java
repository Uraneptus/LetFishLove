package com.uraneptus.letfishlove.core.registry;

import com.uraneptus.letfishlove.LetFishLoveMod;
import com.uraneptus.letfishlove.common.blocks.RoeBlock;
import com.uraneptus.letfishlove.core.other.LFLProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LFLBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LetFishLoveMod.MOD_ID);

    public static final RegistryObject<Block> COD_ROE_BLOCK = BLOCKS.register("cod_roe_block", () -> new RoeBlock(() -> EntityType.COD, LFLProperties.roeBlockProperties()));
    public static final RegistryObject<Block> PUFFERFISH_ROE_BLOCK = BLOCKS.register("pufferfish_roe_block", () -> new RoeBlock(() -> EntityType.PUFFERFISH, LFLProperties.roeBlockProperties()));
    public static final RegistryObject<Block> SALMON_ROE_BLOCK = BLOCKS.register("salmon_roe_block", () -> new RoeBlock(() -> EntityType.SALMON, LFLProperties.roeBlockProperties()));
    public static final RegistryObject<Block> TROPICAL_FISH_ROE_BLOCK = BLOCKS.register("tropical_fish_roe_block", () -> new RoeBlock(() -> EntityType.TROPICAL_FISH, LFLProperties.roeBlockProperties()));

}