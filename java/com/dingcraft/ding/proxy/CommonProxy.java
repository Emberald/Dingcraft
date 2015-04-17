package com.dingcraft.ding.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.dingcraft.ding.Dingcraft;

public class CommonProxy
{
	public void register()
	{
		//Forge event handler registry
		MinecraftForge.EVENT_BUS.register(Dingcraft.handler);
		//FML event handler registry
		FMLCommonHandler.instance().bus().register(Dingcraft.handler);
	}
}
