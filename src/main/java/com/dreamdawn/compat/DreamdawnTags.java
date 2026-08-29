package com.dreamdawn.compat;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class DreamdawnTags {
    public static final TagKey<Item> SMALL_BOTTLE_WINE = item("small_bottle_wine");
    public static final TagKey<Item> LARGE_BOTTLE_WINE = item("large_bottle_wine");
    /**
     * 双瓶大酒标签：加入此标签的酒在 Kaleidoscope Tavern 的 bar_cabinet / glass_bar_cabinet
     * 两个大酒柜中允许左右各放一瓶（与小瓶酒一致），其余场景仍按大瓶酒处理。
     * 建议同时加入 large_bottle_wine 标签。
     */
    public static final TagKey<Item> DOUBLE_BOTTLE_WINE = item("double_bottle_wine");
    /**
     * 玩家自定义酒瓶标签：加入此标签的物品在非潜行右键方块时，禁止物品自身的放置回退
     * （useOn / onItemUseFirst），方块自身的 use（酒柜存取等）不受影响。默认为空，由玩家自行添加。
     */
    public static final TagKey<Item> WINE_BOTTLE = item("wine_bottle");

    private static TagKey<Item> item(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(Dreamdawn.MOD_ID, name));
    }

    private DreamdawnTags() {
    }
}
