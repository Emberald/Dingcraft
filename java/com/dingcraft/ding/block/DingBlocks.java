package com.dingcraft.ding.block;

import net.minecraft.block.Block;

import com.dingcraft.ding.Dingcraft;

public class DingBlocks
{
	public static final BlockDing dingBlock = new BlockDing();
	public static final BlockPhoton photonBlock = new BlockPhoton();
	public static final BlockMeteor meteorBlock = new BlockMeteor();

	public static void registerAll()
	{
		put(dingBlock, 0, "dingBlock");
		put(photonBlock, 0, "photonBlock");
		put(meteorBlock, 0, "meteorBlock");
	}
	
	private static void put(Block block, Object... resourceMap)
	{
		Dingcraft.proxy.registerBlock(block, resourceMap);
	}
}
