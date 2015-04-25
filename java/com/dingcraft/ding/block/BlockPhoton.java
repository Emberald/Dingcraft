package com.dingcraft.ding.block;

import net.minecraft.block.BlockAir;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.dingcraft.ding.tileentity.TileEntityPhoton;

public class BlockPhoton extends BlockAir implements ITileEntityProvider
{
	public static final String name = "photonBlock";
		
	public BlockPhoton()
	{
        super();
		this.setUnlocalizedName(this.name);
		this.setLightLevel(0.9375f);
	}
    
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
        return new TileEntityPhoton();
	}	   

}
