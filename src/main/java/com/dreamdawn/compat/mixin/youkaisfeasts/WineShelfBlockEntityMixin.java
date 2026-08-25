package com.dreamdawn.compat.mixin.youkaisfeasts;

import com.dreamdawn.compat.CompatRules;
import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.WineShelfBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WineShelfBlockEntity.class)
public abstract class WineShelfBlockEntityMixin {

    @Inject(method = "isFlask", remap = false, at = @At("HEAD"), cancellable = true)
    private static void dreamdawn$allowTaggedBottles(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (CompatRules.isSmallBottle(stack)) {
            cir.setReturnValue(true);
        }
    }
}