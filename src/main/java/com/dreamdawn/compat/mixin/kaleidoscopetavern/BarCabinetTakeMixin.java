package com.dreamdawn.compat.mixin.kaleidoscopetavern;

import com.dreamdawn.compat.DreamdawnTags;
import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.BarCabinetBlock;
import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.BottleBlock;
import com.github.ysbbbbbb.kaleidoscopetavern.blockentity.brew.BarCabinetBlockEntity;
import com.github.ysbbbbbb.kaleidoscopetavern.init.tag.TagMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 将 tavern 酒柜的取酒逻辑改为 Vinery 风格：
 * 右键有酒的格子时必须空主手才能取酒，酒直接进背包（背包满则掉落）；
 * 空格子 + 手上是酒瓶时仍然正常放入。
 */
@Mixin(BarCabinetBlock.class)
public abstract class BarCabinetTakeMixin {

    @Invoker(value = "getBottleBlock", remap = false)
    protected abstract BottleBlock dreamdawn$invokeGetBottleBlock(ItemStack stack);

    @Inject(method = "onClick", remap = false, at = @At("HEAD"), cancellable = true)
    private void dreamdawn$vineryStyleOnClick(BarCabinetBlockEntity barCabinet, Player player,
                                              ItemStack stack, boolean isLeftSide,
                                              CallbackInfoReturnable<Boolean> cir) {
        boolean single = barCabinet.isSingle();
        boolean irregular = false;
        BottleBlock bottleBlock = this.dreamdawn$invokeGetBottleBlock(stack);

        if (bottleBlock != null && !stack.isEmpty()) {
            // A single-cabinet bottle occupies the whole cabinet: no further bottle may be added
            if (single) {
                cir.setReturnValue(false);
                return;
            }
            // Large/irregular bottles (except double_bottle_wine) require BOTH sides empty and always go to the left.
            // Checking only the left slot caused left/right asymmetry (right occupied, left empty => wrongly inserted).
            if (stack.is(TagMod.BAR_CABINET_IRREGULAR) && !stack.is(DreamdawnTags.DOUBLE_BOTTLE_WINE)) {
                if (!barCabinet.getLeftItem().isEmpty() || !barCabinet.getRightItem().isEmpty()) {
                    cir.setReturnValue(false);
                    return;
                }
                isLeftSide = true;
                irregular = true;
            }
        } else if (single) {
            // single mode (single large/irregular bottle shown): empty hand may only interact with the left slot
            isLeftSide = true;
        }

        if (isLeftSide) {
            if (stack.isEmpty() && !barCabinet.getLeftItem().isEmpty()) {
                // 空主手 + 有酒：取酒进背包
                ItemStack taken = barCabinet.getLeftItem();
                barCabinet.setLeftItem(ItemStack.EMPTY);
                barCabinet.setSingle(false);
                barCabinet.refresh();
                giveToInventory(player, taken);
                cir.setReturnValue(true);
                return;
            }
            if (bottleBlock != null && barCabinet.getLeftItem().isEmpty()) {
                ItemStack split = stack.split(1);
                barCabinet.setLeftItem(split);
                barCabinet.setSingle(irregular);
                barCabinet.refresh();
                cir.setReturnValue(true);
                return;
            }
        } else {
            if (stack.isEmpty() && !barCabinet.getRightItem().isEmpty()) {
                // 空主手 + 有酒：取酒进背包
                ItemStack taken = barCabinet.getRightItem();
                barCabinet.setRightItem(ItemStack.EMPTY);
                barCabinet.refresh();
                giveToInventory(player, taken);
                cir.setReturnValue(true);
                return;
            }
            if (bottleBlock != null && barCabinet.getRightItem().isEmpty()) {
                ItemStack split = stack.split(1);
                barCabinet.setRightItem(split);
                barCabinet.refresh();
                cir.setReturnValue(true);
                return;
            }
        }
        cir.setReturnValue(false);
    }

    private static void giveToInventory(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}
