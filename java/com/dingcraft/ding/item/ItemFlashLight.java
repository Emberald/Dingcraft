package com.dingcraft.ding.item;

import com.dingcraft.ding.entitylighting.LightSourceSpotlight;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemFlashLight extends Item
{
	public static final String name = "flashLight";
	
	public ItemFlashLight()
	{
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName(this.name);
		this.setMaxStackSize(1);
		LightSourceSpotlight.addLightSource(this, 0);
	}
}
