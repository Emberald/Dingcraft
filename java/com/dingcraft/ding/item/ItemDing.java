package com.dingcraft.ding.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
 
public class ItemDing extends Item
{
	private final String name = "dingItem";

	public ItemDing() {}
	
	public String getName()
	{
		return name;
	}
}