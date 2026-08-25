package com.dreamdawn.compat.mixin.youkaisfeasts;

import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.ShelfContainer;
import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.WineShelfBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 将 Youkai 酒架的取酒逻辑改为要求空主手：
 * 有酒的格子必须空手才能取，取出的酒进背包；手非空时不取。
 */
@Mixin(WineShelfBlockEntity.class)
public abstract class WineShelfTakeMixin {

    @Shadow(remap = false)
    public ShelfContainer items;

    @Inject(method = "click", remap = false, at = @At("HEAD"), cancellable = true)
    private void dreamdawn$emptyHandTakeOut(Player player, InteractionHand hand, int index,
                                            CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (items.getItem(index).isEmpty()) {
            if (WineShelfBlockEntity.isFlask(stack)) {
                if (!player.level().isClientSide()) {
                    items.setItem(index, stack.split(1));
                }
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
            return;
        }
        // 有酒：必须空主手才能取
        if (!stack.isEmpty()) {
            cir.setReturnValue(false);
            return;
        }
        if (!player.level().isClientSide()) {
            ItemStack ans = items.getItem(index);
            items.setItem(index, ItemStack.EMPTY);
            player.getInventory().placeItemBackInInventory(ans);
        }
        cir.setReturnValue(true);
    }
}
