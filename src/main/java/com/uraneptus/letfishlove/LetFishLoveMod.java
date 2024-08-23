package com.uraneptus.letfishlove;

import com.mojang.logging.LogUtils;
import com.uraneptus.letfishlove.common.capabilities.FishBreedingCapAttacher;
import com.uraneptus.letfishlove.core.registry.LFLBlocks;
import com.uraneptus.letfishlove.core.registry.LFLItems;
import com.uraneptus.letfishlove.data.client.LFLBlockStateProvider;
import com.uraneptus.letfishlove.data.client.LFLItemModelProvider;
import com.uraneptus.letfishlove.data.client.LFLLangProvider;
import com.uraneptus.letfishlove.data.server.loot.LFLLootTableProvider;
import com.uraneptus.letfishlove.data.server.tags.LFLBlockTagsProvider;
import com.uraneptus.letfishlove.data.server.tags.LFLEntityTagsProvider;
import com.uraneptus.letfishlove.data.server.tags.LFLItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@Mod(LetFishLoveMod.MOD_ID)
@Mod.EventBusSubscriber(modid = LetFishLoveMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LetFishLoveMod {
    public static final String MOD_ID = "letfishlove";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation modPrefix(String path) {
        return new ResourceLocation(LetFishLoveMod.MOD_ID, path);
    }

    public LetFishLoveMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::gatherData);
        bus.addListener(this::setup);

        LFLBlocks.BLOCKS.register(bus);
        LFLItems.ITEMS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        FishBreedingCapAttacher.setupChannel();
        FishBreedingCapAttacher.register();
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(includeClient, new LFLBlockStateProvider(packOutput, fileHelper));
        generator.addProvider(includeClient, new LFLItemModelProvider(packOutput, fileHelper));
        generator.addProvider(includeClient, new LFLLangProvider(packOutput));

        LFLBlockTagsProvider blockTagsProvider = new LFLBlockTagsProvider(packOutput, lookupProvider, fileHelper);
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new LFLItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), fileHelper));
        generator.addProvider(includeServer, new LFLEntityTagsProvider(packOutput, lookupProvider, fileHelper));
        generator.addProvider(includeServer, new LFLLootTableProvider(packOutput));
    }

}
