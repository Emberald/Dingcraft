package com.dingcraft.ding.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.block.BlockPhoton;
import com.dingcraft.ding.block.DingBlocks;

public class TileEntityPhoton extends TileEntity implements IUpdatePlayerListBox
{
	private int ticksOnCondensed;
	 
	public TileEntityPhoton()
	{
		this.ticksOnCondensed = 2;
	}
	
	public void setDuration(int duration)
	{
		this.ticksOnCondensed = duration;
	}
	
    public void update() 
    {
    	if(this.ticksOnCondensed <= 0)
    	{
    		IBlockState blockState = this.worldObj.getBlockState(pos);
    		Block lightedBlock = blockState.getBlock();
    		if(lightedBlock == DingBlocks.photonBlock)
    		{
    			this.worldObj.setBlockToAir(this.pos);    			
    		}
    		else if (lightedBlock.hasTileEntity(blockState) && (this.worldObj.getTileEntity(this.pos) instanceof TileEntityPhoton))
            {
                this.worldObj.removeTileEntity(pos);
            }
    	}
    	else
    	{
    		this.ticksOnCondensed--;
    	}
    }
}
