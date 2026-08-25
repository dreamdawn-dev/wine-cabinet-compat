package com.dreamdawn.compat;

import net.minecraft.world.item.ItemStack;

public final class CompatRules {
    public static boolean isSmallBottle(ItemStack stack) {
        return stack.is(DreamdawnTags.SMALL_BOTTLE_WINE);
    }

    public static boolean isLargeBottle(ItemStack stack) {
        return stack.is(DreamdawnTags.LARGE_BOTTLE_WINE);
    }

    public static boolean isBottle(ItemStack stack) {
        return isSmallBottle(stack) || isLargeBottle(stack);
    }

    private CompatRules() {
    }
}