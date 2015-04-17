package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

/**
 * This class is responsible for keeping track of an entity.
 * Once initialized, binded entity cannot be changed; however, brightness can be changed at each update.
 * 
 * @author pyy
 *
 */
public abstract class LightSourceEntity
{
	public final Entity entity;
	protected BlockPos lightBlockPos;
	protected int lightLevel;
	
	public LightSourceEntity(Entity lightSource, int lightLevel)
	{
		this.entity = lightSource;
		this.lightBlockPos = new BlockPos(Math.floor(lightSource.posX), Math.floor(lightSource.posY), Math.floor(lightSource.posZ));
		this.lightLevel = lightLevel;
	}
	
	public BlockPos getBlockPos()
	{
		return this.lightBlockPos;
	}
	
	public int getLightLevel()
	{
		return this.lightLevel;
	}
		
	/**
	 * This method is called at the beginning of a light update, and after entity's <i>onUpdate</i> event is triggered.</br>
	 * Use this method to update some properties of the entity(e.g. light level),
	 * and to determine whether the entity should be removed.</br>
	 * <b>DO NOT</b> update lighting in this method; this will be done later by <i>EntityLighting</i> class.
	 * @return True if the entity should be removed, or false if the entity should be kept alive.
	 */
	public abstract boolean onUpdate();

	/**
	 * This method is called before lighting of the entity is recomputed.</br>
	 * This method should update the position of current light source block of the entity.</br>
	 * Override this method in subclass if you want to modify the position of the light source block.
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

}
