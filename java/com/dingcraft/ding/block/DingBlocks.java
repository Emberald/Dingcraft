package com.dingcraft.ding.block;

import java.lang.reflect.Field;

import net.minecraft.block.Block;

public class DingBlocks
{
	public static final BlockDing dingBlock = new BlockDing();
	public static final BlockPhoton photonBlock = new BlockPhoton();
	public static final BlockMeteor meteorBlock = new BlockMeteor();

	public static final Block[] blocks = new Block[] {
		dingBlock,
		photonBlock,
		meteorBlock
	};

}
