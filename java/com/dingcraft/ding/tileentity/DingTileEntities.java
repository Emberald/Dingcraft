package com.dingcraft.ding.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class DingTileEntities
{
	public static void registerAll()
	{
		
	}
	
	private static void put(Class<? extends TileEntity> tileEntityClass, String name)
	{
		TileEntity.addMapping(tileEntityClass, name);
	}
	
}
