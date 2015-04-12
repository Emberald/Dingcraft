package com.dingcraft.ding.block;

import java.util.Random;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPhoton extends BlockAir
{
	public static final String name = "photonBlock";
		
	public BlockPhoton()
	{
        super();
		this.setUnlocalizedName(this.name);
		this.setLightLevel(0.9375f);
	}


}
