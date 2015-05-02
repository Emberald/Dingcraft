package com.dingcraft.ding.block;

import net.minecraft.block.BlockAir;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.dingcraft.ding.tileentity.TileEntityPhoton;

public class BlockPhoton extends BlockAir implements ITileEntityProvider
{
	public BlockPhoton()
	{
        super();
		this.setUnlocalizedName("photonBlock");
		this.setLightLevel(0.9375f);
	}
    
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
        return new TileEntityPhoton();
	}	   

}
