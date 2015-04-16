package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

/**
 * This class is responsible for keeping track of an entity and updating lighting.</br>
 * <b>NEVER</b> override methods defined here.
 * 
 * @author pyy
 *
 */
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
	 * This method will update the current light source block of the entity.
	 * @return Old block position if the entity has moved, and null otherwise.
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
