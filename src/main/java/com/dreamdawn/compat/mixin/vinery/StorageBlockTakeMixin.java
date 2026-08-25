package com.dreamdawn.compat.mixin.vinery;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.vinery.core.block.StorageBlock;
import net.satisfy.vinery.core.block.entity.StorageBlockEntity;
import net.satisfy.vinery.core.util.GeneralUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * 将 Vinery 酒架/酒瓶容器的取酒逻辑改为要求空主手：
 * 有酒的格子必须空手才能取（取出进背包，与原有行为一致）；
 * 手非空时不取不放，并阻止放置回退（CONSUME）。
 */
@Mixin(StorageBlock.class)
public abstract class StorageBlockTakeMixin {

    @Shadow(remap = false)
    public abstract Direction[] unAllowedDirections();

    @Shadow(remap = false)
    public abstract int getSection(Float x, Float y);

    @Shadow(remap = false)
    public abstract boolean canInsertStack(ItemStack stack);

    @Shadow(remap = false)
    public abstract void add(Level level, BlockPos pos, Player player,
                             StorageBlockEntity storage, ItemStack stack, int section);

    @Shadow(remap = false)
    public abstract void remove(Level level, BlockPos pos, Player player,
                                StorageBlockEntity storage, int section);

    @Inject(method = "use(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"), cancellable = true)
    private void dreamdawn$emptyHandTakeOut(BlockState state, Level level, BlockPos pos,
                                            Player player, InteractionHand hand,
                                            BlockHitResult hitResult,
                                            CallbackInfoReturnable<InteractionResult> cir) {
        if (!(level.getBlockEntity(pos) instanceof StorageBlockEntity storage)) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }
        Optional<Tuple<Float, Float>> relative = GeneralUtil.getRelativeHitCoordinatesForBlockFace(
                hitResult, state.getValue(HorizontalDirectionalBlock.FACING), this.unAllowedDirections());
        if (relative.isEmpty()) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }
        Tuple<Float, Float> tuple = relative.get();
        int section = this.getSection(tuple.getA(), tuple.getB());
        if (section == Integer.MIN_VALUE) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }
        ItemStack inSlot = storage.getInventory().get(section);
        if (!inSlot.isEmpty()) {
            if (player.getItemInHand(hand).isEmpty()) {
                // 空主手 + 有酒：取酒（remove 内部只有服务端实际改动库存）
                this.remove(level, pos, player, storage, section);
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            } else {
                // 手非空：不取不放，也不触发放置回退
                cir.setReturnValue(InteractionResult.CONSUME);
            }
            return;
        }
        ItemStack handItem = player.getItemInHand(hand);
        if (!handItem.isEmpty() && this.canInsertStack(handItem)) {
            this.add(level, pos, player, storage, handItem, section);
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            return;
        }
        cir.setReturnValue(InteractionResult.CONSUME);
    }
}
