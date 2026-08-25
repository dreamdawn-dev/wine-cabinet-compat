package com.dreamdawn.compat.client;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public final class ClientRenderCompat {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static BlockState safeSetFakeModel(BlockState state, Property<?> property, Comparable<?> value) {
        if (state.hasProperty(property)) {
            return state.setValue((Property) property, (Comparable) value);
        }
        return state;
    }

    private ClientRenderCompat() {
    }
}