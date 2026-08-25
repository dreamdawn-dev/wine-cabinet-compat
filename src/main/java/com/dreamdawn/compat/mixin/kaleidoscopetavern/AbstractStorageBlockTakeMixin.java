package com.dreamdawn.compat.mixin.kaleidoscopetavern;

import com.github.ysbbbbbb.kaleidoscopetavern.block.AbstractStorageBlock;
import com.github.ysbbbbbb.kaleidoscopetavern.blockentity.deco.StorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 将 tavern 的 holder / 倾斜酒架 / 圆周酒架 / 地窖酒柜取酒逻辑改为 Vinery 风格：
 * 右键有酒的格子必须空主手才能取酒，酒直接进背包（背包满则掉落）；
 * 非空手时保持原有放置/放入逻辑。
 */
@Mixin(AbstractStorageBlock.class)
public abstract class AbstractStorageBlockTakeMixin {

    @Shadow(remap = false)
    protected abstract int getClickedSlot(Direction direction, BlockPos pos, BlockHitResult hitResult);

    @Shadow(remap = false)
    protected abstract InteractionResult putOn(Level level, BlockPos pos, Player player,
                                               StorageBlockEntity storage, int clickedSlot);

    @Shadow(remap = false)
    protected static InteractionResult takeOut(Level level, BlockPos pos, Player player,
                                               InteractionHand hand, StorageBlockEntity storage,
                                               int clickedSlot) {
        throw new AssertionError();
    }

    @Inject(method = "handleUse", remap = false, at = @At("HEAD"), cancellable = true)
    private void dreamdawn$vineryStyleHandleUse(BlockState state, Level level, BlockPos pos,
                                                Player player, InteractionHand hand,
                                                BlockHitResult hitResult,
                                                CallbackInfoReturnable<InteractionResult> cir) {
        if (hand != InteractionHand.MAIN_HAND) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof StorageBlockEntity storage)) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }
        int clickedSlot = this.getClickedSlot(state.getValue(HorizontalDirectionalBlock.FACING), pos, hitResult);
        if (clickedSlot == -1) {
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }
        if (player.getItemInHand(hand).isEmpty()) {
            // 空主手才取酒
            cir.setReturnValue(takeOut(level, pos, player, hand, storage, clickedSlot));
            return;
        }
        cir.setReturnValue(this.putOn(level, pos, player, storage, clickedSlot));
    }

    @Inject(method = "takeOut", remap = false, at = @At("HEAD"), cancellable = true)
    private static void dreamdawn$vineryStyleTakeOut(Level level, BlockPos pos, Player player,
                                                     InteractionHand hand, StorageBlockEntity storage,
                                                     int clickedSlot,
                                                     CallbackInfoReturnable<InteractionResult> cir) {
        ItemStackHandler items = storage.getItems();
        ItemStack stack = items.getStackInSlot(clickedSlot);
        if (!stack.isEmpty()) {
            // 与 Vinery 一致：只有服务端实际改动库存，客户端只返回结果等待同步
            if (!level.isClientSide) {
                ItemStack taken = stack.copy();
                items.setStackInSlot(clickedSlot, ItemStack.EMPTY);
                storage.refresh();
                if (!player.getInventory().add(taken)) {
                    player.drop(taken, false);
                }
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }
        cir.setReturnValue(InteractionResult.PASS);
    }
}
