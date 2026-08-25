package com.dreamdawn.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 针对加入 {@link DreamdawnTags#WINE_BOTTLE} 标签的物品：
 * 非潜行右键方块时禁止物品自身的放置回退（onItemUseFirst / useOn），
 * 避免自定义酒瓶在点酒柜背面/顶面时被放到后面/上面，以及客户端预测放置产生的闪烁。
 * 方块自身的 use（三个模组酒柜、酒架、shelf 的存取）不受影响。
 */
@Mod.EventBusSubscriber(modid = Dreamdawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BottlePlacementHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }
        if (!stack.is(DreamdawnTags.WINE_BOTTLE)) {
            return;
        }
        Player player = event.getEntity();
        if (player.isCrouching()) {
            // 潜行时保留正常放置
            return;
        }
        event.setUseItem(Event.Result.DENY);
    }
}
