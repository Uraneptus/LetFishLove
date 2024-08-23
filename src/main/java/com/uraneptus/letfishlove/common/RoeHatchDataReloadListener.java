package com.uraneptus.letfishlove.common;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

public class RoeHatchDataReloadListener extends SimpleJsonResourceReloadListener {
    public static Map<ResourceLocation, RoeData> ROE_HATCH_DATA_MAP = new LinkedHashMap<>();

    public RoeHatchDataReloadListener() {
        super(new GsonBuilder().create(), "roe_hatch_data");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ROE_HATCH_DATA_MAP.clear();

        for (JsonElement entry : pObject.values()) {
            JsonObject obj = entry.getAsJsonObject();
            String name = obj.getAsJsonPrimitive("registry_name").getAsString();
            ResourceLocation location = new ResourceLocation(name);
            if (!ForgeRegistries.BLOCKS.containsKey(location)) {
                continue;
            }
            int minHatchAmount = obj.getAsJsonPrimitive("minHatchAmount").getAsInt();
            int maxHatchAmount = obj.getAsJsonPrimitive("maxHatchAmount").getAsInt();
            int minHatchDuration = obj.getAsJsonPrimitive("minHatchDuration").getAsInt();
            int maxHatchDuration = obj.getAsJsonPrimitive("maxHatchDuration").getAsInt();

            ROE_HATCH_DATA_MAP.put(location, new RoeData(minHatchAmount, maxHatchAmount, minHatchDuration, maxHatchDuration));
        }
    }

    public record RoeData(int minHatchAmount, int maxHatchAmount, int minHatchDuration, int maxHatchDuration) {}
}
