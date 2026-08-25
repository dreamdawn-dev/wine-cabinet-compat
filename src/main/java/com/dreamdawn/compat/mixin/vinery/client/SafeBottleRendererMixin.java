package com.dreamdawn.compat.mixin.vinery.client;

import com.dreamdawn.compat.client.ClientRenderCompat;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.satisfy.vinery.client.render.block.storage.BigBottleRenderer;
import net.satisfy.vinery.client.render.block.storage.FourBottleRenderer;
import net.satisfy.vinery.client.render.block.storage.NineBottleRenderer;
import net.satisfy.vinery.client.render.block.storage.WineBoxRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({BigBottleRenderer.class, FourBottleRenderer.class, NineBottleRenderer.class, WineBoxRenderer.class})
public abstract class SafeBottleRendererMixin {

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;setValue(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"))
    private Object dreamdawn$safelySetFakeModel(BlockState state, Property<?> property, Comparable<?> value) {
        return ClientRenderCompat.safeSetFakeModel(state, property, value);
    }
}