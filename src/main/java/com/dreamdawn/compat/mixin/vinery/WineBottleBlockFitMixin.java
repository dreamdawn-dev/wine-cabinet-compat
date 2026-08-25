package com.dreamdawn.compat.mixin.vinery;

import com.dreamdawn.compat.CompatRules;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.core.block.WineBottleBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WineBottleBlock.class)
public abstract class WineBottleBlockFitMixin {

    @Inject(method = "willFitStack", remap = false, at = @At("HEAD"), cancellable = true)
    private void dreamdawn$allowTaggedBottles(ItemStack stack, NonNullList<ItemStack> inventory, CallbackInfoReturnable<Boolean> cir) {
        if (CompatRules.isSmallBottle(stack)) {
            cir.setReturnValue(true);
        }
    }
}