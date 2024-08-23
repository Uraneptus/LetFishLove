package com.uraneptus.letfishlove.core.events;

import com.uraneptus.letfishlove.common.RoeHatchDataReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LFLForgeEvents {

    @SubscribeEvent
    public static void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new RoeHatchDataReloadListener());
    }
}
