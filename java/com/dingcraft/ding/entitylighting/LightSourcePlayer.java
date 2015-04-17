package com.dingcraft.ding.entitylighting;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class LightSourcePlayer extends LightSourceEntity {

	public LightSourcePlayer(EntityPlayer player)
	{
		super(player, 0);
	}

	public boolean onUpdate()
	{
		ItemStack itemStack = ((EntityPlayer)this.entity).getHeldItem();
		if(itemStack != null)
		{
			Block block = Block.getBlockFromItem(itemStack.getItem());
			if(block != null)
				this.lightLevel = MathHelper.floor_float(block.getLightValue() * 0.75F);
			else
				this.lightLevel = 0;
		}
		else
			this.lightLevel = 0;
		return this.entity.isDead;
	}
	
	public BlockPos hasMoved()
	{
		BlockPos blockPosNew = new BlockPos(Math.floor(this.entity.posX), Math.floor(this.entity.posY + this.entity.getEyeHeight()), Math.floor(this.entity.posZ));
		if(!blockPosNew.equals(this.lightBlockPos))
		{
			BlockPos blockPosOld = this.lightBlockPos;
			this.lightBlockPos = blockPosNew;
			return blockPosOld;
		}
		else
			return null;
	}
}
