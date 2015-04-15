package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

public abstract class LightSourceEntity
{
	public final Entity entity;
	private BlockPos lightBlockPos;
	
	public LightSourceEntity(Entity lightSource)
	{
		this.entity = lightSource;
		this.lightBlockPos = new BlockPos(Math.floor(lightSource.posX), Math.floor(lightSource.posY), Math.floor(lightSource.posZ));
	}
	
	public BlockPos getBlockPos()
	{
		return this.lightBlockPos;
	}
	
	/**
	 * If the entity has moved, this method sets the light source block position to the new position,
	 * and returns the old position; otherwise this method returns null.
	 */
	public BlockPos hasMoved()
	{
		BlockPos blockPosNew = new BlockPos(Math.floor(this.entity.posX), Math.floor(this.entity.posY), Math.floor(this.entity.posZ));
		if(!blockPosNew.equals(this.lightBlockPos))
		{
			BlockPos blockPosOld = this.lightBlockPos;
			this.lightBlockPos = blockPosNew;
			return blockPosOld;
		}
		else
			return null;
	}
	
	public abstract int getLightLevel();
	
	public abstract boolean shouldBeRemoved();
	
}
