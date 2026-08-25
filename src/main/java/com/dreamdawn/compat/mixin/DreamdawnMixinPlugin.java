package com.dreamdawn.compat.mixin;

import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class DreamdawnMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("FakeModel")) {
            if (mixinClassName.contains("kaleidoscopetavern")) {
                return isModLoaded("kaleidoscope_tavern") && isModLoaded("vinery");
            }
            if (mixinClassName.contains("youkaisfeasts")) {
                return isModLoaded("youkaisfeasts") && isModLoaded("vinery");
            }
            return true;
        }
        if (mixinClassName.contains("kaleidoscopetavern")) {
            return isModLoaded("kaleidoscope_tavern");
        }
        if (mixinClassName.contains("vinery")) {
            return isModLoaded("vinery");
        }
        if (mixinClassName.contains("youkaisfeasts")) {
            return isModLoaded("youkaisfeasts");
        }
        return true;
    }

    private static boolean isModLoaded(String modId) {
        return LoadingModList.get() != null && LoadingModList.get().getModFileById(modId) != null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}