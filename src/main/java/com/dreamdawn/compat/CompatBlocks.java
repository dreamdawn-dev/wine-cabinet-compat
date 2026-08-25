package com.dreamdawn.compat;

import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.BottleBlock;
import com.github.ysbbbbbb.kaleidoscopetavern.init.ModBlocks;
import net.minecraft.world.item.ItemStack;

public final class CompatBlocks {
    public static BottleBlock placeholder(ItemStack stack) {
        if (CompatRules.isLargeBottle(stack)) {
            return (BottleBlock) ModBlocks.BRANDY.get();
        }
        return (BottleBlock) ModBlocks.EMPTY_BOTTLE.get();
    }

    private CompatBlocks() {
    }
}