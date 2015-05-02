package com.dingcraft.ding.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.dingcraft.ding.entitylighting.LightSourceSpotlight;

public class ItemFlashLight extends Item
{
	public ItemFlashLight()
	{
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName("flashLight");
		this.setMaxStackSize(1);
		LightSourceSpotlight.addLightSource(this, 0);
	}
}
