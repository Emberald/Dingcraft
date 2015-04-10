package com.dingcraft.ding.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPhoton extends BlockAir
{
	public static final String name = "photonBlock";

	public BlockPhoton()
	{
		super();
		this.setUnlocalizedName(this.name);
		this.setLightLevel(0.9375f);
		this.setTickRandomly(true);
	}
	
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int ticksOnCondensed, EntityLivingBase placer)
    {
		return this.getDefaultState();
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) 
    {
        worldIn.setBlockToAir(pos);
    }
    
}
