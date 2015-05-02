package com.dingcraft.ding.item;

import java.lang.reflect.Field;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class DingItems
{
	public static final Item dingItem = (new Item()).setUnlocalizedName("dingItem").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemWandDing dingWand = new ItemWandDing();
	public static final ItemPocketWatch pocketWatch = new ItemPocketWatch();
	public static final ItemFlashLight flashLight = new ItemFlashLight();
	public static final Item arrowTorch = (new Item()).setUnlocalizedName("torchArrow").setCreativeTab(CreativeTabs.tabTools);
	public static final Item arrowDeliverer = (new Item()).setUnlocalizedName("deliverArrow").setCreativeTab(CreativeTabs.tabTools);
	public static final Item arrowDelivererLoaded = (new Item()).setUnlocalizedName("loadedDeliverArrow").setMaxStackSize(1);
	
	public static final Item[] items = new Item[] {
		dingItem,
		dingWand,
		pocketWatch,
		flashLight,
		arrowTorch,
		arrowDeliverer,
		arrowDelivererLoaded
	};
}

