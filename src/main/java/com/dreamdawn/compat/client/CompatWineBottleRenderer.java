package com.dreamdawn.compat.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.satisfy.vinery.client.render.block.storage.StorageTypeRenderer;
import net.satisfy.vinery.client.util.ClientUtil;
import net.satisfy.vinery.core.block.entity.StorageBlockEntity;

public class CompatWineBottleRenderer implements StorageTypeRenderer {

    @Override
    public void render(StorageBlockEntity entity, PoseStack pose, MultiBufferSource buffer, NonNullList<ItemStack> stacks) {
        pose.translate(-0.5, 0, -0.5);
        switch (getCount(stacks)) {
            case 1 -> renderOne(entity, pose, buffer, stacks);
            case 2 -> renderTwo(entity, pose, buffer, stacks);
            case 3 -> renderThree(entity, pose, buffer, stacks);
            default -> {
            }
        }
    }

    private int getCount(NonNullList<ItemStack> stacks) {
        int count = 0;
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private void renderOne(StorageBlockEntity entity, PoseStack pose, MultiBufferSource buffer, NonNullList<ItemStack> stacks) {
        renderBottle(entity, pose, buffer, stacks.get(0));
    }

    private void renderTwo(StorageBlockEntity entity, PoseStack pose, MultiBufferSource buffer, NonNullList<ItemStack> stacks) {
        pose.translate(-0.15F, 0.0F, -0.25F);
        renderBottle(entity, pose, buffer, stacks.get(0));
        pose.translate(0.1F, 0.0F, 0.8F);
        pose.mulPose(Axis.YP.rotationDegrees(30));
        renderBottle(entity, pose, buffer, stacks.get(1));
    }

    private void renderThree(StorageBlockEntity entity, PoseStack pose, MultiBufferSource buffer, NonNullList<ItemStack> stacks) {
        pose.translate(-0.25F, 0.0F, -0.25F);
        renderBottle(entity, pose, buffer, stacks.get(0));
        pose.translate(0.15F, 0.0F, 0.5F);
        renderBottle(entity, pose, buffer, stacks.get(1));

        ItemStack third = stacks.get(2);
        if (third.isEmpty()) {
            return;
        }
        Item kelpCider = ForgeRegistries.ITEMS.getValue(new ResourceLocation("vinery", "kelp_cider"));
        if (kelpCider != null && third.is(kelpCider)) {
            pose.translate(0.35F, 0.7F, -0.13F);
            pose.mulPose(Axis.XP.rotationDegrees(90));
            renderBottle(entity, pose, buffer, third);
            return;
        }
        pose.translate(0.1F, 0.0F, 0.0F);
        pose.mulPose(Axis.YP.rotationDegrees(30));
        renderBottle(entity, pose, buffer, third);
    }

    private static void renderBottle(StorageBlockEntity entity, PoseStack pose, MultiBufferSource buffer, ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
            return;
        }
        BlockState state = CompatRender.normalize(blockItem.getBlock().defaultBlockState());
        ClientUtil.renderBlock(state, pose, buffer, entity);
    }
}