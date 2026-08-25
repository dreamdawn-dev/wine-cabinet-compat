package com.dreamdawn.compat.client;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public final class CompatRender {
    public static BlockState normalize(BlockState state) {
        Property<?> property = state.getBlock().getStateDefinition().getProperty("fake_model");
        if (property instanceof BooleanProperty booleanProperty) {
            return state.setValue(booleanProperty, false);
        }
        return state;
    }

    private CompatRender() {
    }
}