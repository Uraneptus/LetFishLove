package com.uraneptus.letfishlove.data.client;

import com.uraneptus.letfishlove.LetFishLoveMod;
import com.uraneptus.letfishlove.core.registry.LFLBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public class LFLLangProvider extends LanguageProvider {
    public LFLLangProvider(PackOutput packOutput) {
        super(packOutput, LetFishLoveMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        LFLBlocks.BLOCKS.getEntries().forEach(this::forBlock);
    }

    public void forBlock(Supplier<? extends Block> block) {
        addBlock(block, createTranslation(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
    }

    public static String createTranslation(String path) {
        final StringBuilder builder = new StringBuilder();

        for (String part : path.split("_")) {
            if (!builder.isEmpty()) {
                builder.append(" ");
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }
}
