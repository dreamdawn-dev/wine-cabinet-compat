package com.dreamdawn.compat.mixin.kaleidoscopetavern;

import com.dreamdawn.compat.CompatBlocks;
import com.dreamdawn.compat.CompatRules;
import com.github.ysbbbbbb.kaleidoscopetavern.block.AbstractStorageBlock;
import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.BottleBlock;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractStorageBlock.class)
public abstract class AbstractStorageBlockMixin {

    @Inject(method = "getBottleBlock", remap = false, at = @At("HEAD"), cancellable = true)
    private void dreamdawn$allowTaggedBottles(ItemStack stack, CallbackInfoReturnable<BottleBlock> cir) {
        if (CompatRules.isBottle(stack)) {
            cir.setReturnValue(CompatBlocks.placeholder(stack));
        }
    }
}