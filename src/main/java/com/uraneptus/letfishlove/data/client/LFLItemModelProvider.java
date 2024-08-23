package com.uraneptus.letfishlove.data.client;


import com.uraneptus.letfishlove.LetFishLoveMod;
import com.uraneptus.letfishlove.core.registry.LFLItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class LFLItemModelProvider extends ItemModelProvider {

    public LFLItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, LetFishLoveMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        LFLItems.ITEMS.getEntries().forEach(this::basicItem);
    }

    private void basicItem(Supplier<? extends Item> item) {
        basicItem(item.get());
    }
}
