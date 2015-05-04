package com.dingcraft.ding.eventhandler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.item.DingItems;
import com.dingcraft.ding.item.ItemPocketWatch;

public class EventHandlerPlayerDrops 
{
	@SubscribeEvent
    public void onPlayerDropInventory(PlayerDropsEvent event)
	{
		//for pocket watch: cancel drops and reset time rate
		Item dropItem;
		for(int i = event.drops.size() - 1; i >= 0; i--)
		{
			dropItem = event.drops.get(i).getEntityItem().getItem();
			if(dropItem == DingItems.pocketWatch)
			{
				((ItemPocketWatch)dropItem).setTimeRate(1.0F);
				event.drops.remove(i);
			}
		}
	}
}
