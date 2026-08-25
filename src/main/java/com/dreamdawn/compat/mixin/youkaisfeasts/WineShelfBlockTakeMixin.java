package com.dreamdawn.compat.mixin.youkaisfeasts;

import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.WineShelfBlock;
import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.WineShelfBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 方块级双保险：拦截 WineShelfBlock.onClick 中对 click 的调用，
 * 手非空且格子有酒时直接返回 false（不取酒），与实体级 mixin 逻辑一致。
 */
@Mixin(WineShelfBlock.class)
public abstract class WineShelfBlockTakeMixin {

    @Redirect(method = "onClick", remap = false,
            at = @At(value = "INVOKE",
                    target = "Ldev/xkmc/youkaishomecoming/content/pot/storage/shelf/WineShelfBlockEntity;click(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;I)Z"))
    private boolean dreamdawn$emptyHandTakeOut(WineShelfBlockEntity be, Player player,
                                               InteractionHand hand, int index) {
        if (!player.getItemInHand(hand).isEmpty() && !be.items.getItem(index).isEmpty()) {
            // 手非空 + 格子有酒：不取
            return false;
        }
        return be.click(player, hand, index);
    }
}
