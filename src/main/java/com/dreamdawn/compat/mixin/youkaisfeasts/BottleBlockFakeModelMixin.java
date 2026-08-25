package com.dreamdawn.compat.mixin.youkaisfeasts;

import dev.xkmc.youkaishomecoming.content.block.food.BottleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.satisfy.vinery.core.block.WineBottleBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(BottleBlock.class)
public abstract class BottleBlockFakeModelMixin {

    @Redirect(method = "createBlockStateDefinition(Lnet/minecraft/world/level/block/state/StateDefinition$Builder;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/StateDefinition$Builder;add([Lnet/minecraft/world/level/block/state/properties/Property;)Lnet/minecraft/world/level/block/state/StateDefinition$Builder;"))
    private StateDefinition.Builder<Block, BlockState> dreamdawn$addFakeModelState(StateDefinition.Builder<Block, BlockState> builder, Property<?>[] properties) {
        Property<?>[] all = Arrays.copyOf(properties, properties.length + 1);
        all[all.length - 1] = WineBottleBlock.FAKE_MODEL;
        return builder.add(all);
    }
}