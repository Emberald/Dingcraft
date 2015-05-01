package com.dingcraft.ding.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemDing extends Item
{
	public static final ItemDing instance = new ItemDing();
	
	public ItemDing()
	{
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setUnlocalizedName("dingItem");
	}
}