package com.dingcraft.ding.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.dingcraft.ding.Dingcraft;

public class CommonProxy
{
	public void registerBlock(Block block, Object... params)
	{
		GameRegistry.registerBlock(block, this.getName(block));
	}
	
	public void registerItem(Item item, Object... params)
	{
		GameRegistry.registerItem(item, this.getName(item));
	}
	
	public void registerRenderer() {}
	
	public void registerHandler()
	{
		//Forge event handler registry
		MinecraftForge.EVENT_BUS.register(Dingcraft.handlerBow);
		MinecraftForge.EVENT_BUS.register(Dingcraft.handlerDrops);
		//FML event handler registry
		FMLCommonHandler.instance().bus().register(Dingcraft.handlerBow);
		FMLCommonHandler.instance().bus().register(Dingcraft.handlerDrops);
	}
	
	public String getName(Item item)
	{
		return item.getUnlocalizedName().substring(5);
	}

	public String getName(Block block)
	{
		return block.getUnlocalizedName().substring(5);
	}
	
}
