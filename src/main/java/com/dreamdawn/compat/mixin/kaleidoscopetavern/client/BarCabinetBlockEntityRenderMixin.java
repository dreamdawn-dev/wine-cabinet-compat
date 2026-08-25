package com.dreamdawn.compat.mixin.kaleidoscopetavern.client;

import com.dreamdawn.compat.client.CompatRender;
import com.github.ysbbbbbb.kaleidoscopetavern.client.render.block.BarCabinetBlockEntityRender;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BarCabinetBlockEntityRender.class)
public abstract class BarCabinetBlockEntityRenderMixin {

    @Redirect(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState dreamdawn$normalizeState(Block block) {
        return CompatRender.normalize(block.defaultBlockState());
    }
}