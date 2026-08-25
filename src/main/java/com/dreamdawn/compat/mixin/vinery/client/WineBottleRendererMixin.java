package com.dreamdawn.compat.mixin.vinery.client;

import com.dreamdawn.compat.client.CompatWineBottleRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.client.render.block.storage.WineBottleRenderer;
import net.satisfy.vinery.core.block.entity.StorageBlockEntity;
import net.satisfy.vinery.core.item.DrinkBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WineBottleRenderer.class)
public abstract class WineBottleRendererMixin {

    @Unique
    private static final CompatWineBottleRenderer dreamdawn$renderer = new CompatWineBottleRenderer();

    @Inject(method = "render", remap = false, at = @At("HEAD"), cancellable = true)
    private void dreamdawn$renderForeignBottles(StorageBlockEntity entity, PoseStack pose, MultiBufferSource buffer, NonNullList<ItemStack> stacks, CallbackInfo ci) {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty() && !(stack.getItem() instanceof DrinkBlockItem) && stack.getItem() instanceof BlockItem) {
                dreamdawn$renderer.render(entity, pose, buffer, stacks);
                ci.cancel();
                return;
            }
        }
    }
}