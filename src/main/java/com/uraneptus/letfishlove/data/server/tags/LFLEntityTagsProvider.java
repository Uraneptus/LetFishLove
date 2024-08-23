package com.uraneptus.letfishlove.data.server.tags;

import com.uraneptus.letfishlove.LetFishLoveMod;
import com.uraneptus.letfishlove.core.other.LFLEntityTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class LFLEntityTagsProvider extends EntityTypeTagsProvider {

    public LFLEntityTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> pProvider, ExistingFileHelper fileHelper) {
        super(packOutput, pProvider, LetFishLoveMod.MOD_ID, fileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider pProvider) {
        tag(LFLEntityTags.BREEDABLE_FISH).add(EntityType.COD, EntityType.SALMON, EntityType.PUFFERFISH, EntityType.TROPICAL_FISH);
    }
}
