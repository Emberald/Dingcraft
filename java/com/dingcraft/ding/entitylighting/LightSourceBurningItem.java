package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.Entity;

public class LightSourceBurningItem extends LightSourceEntity
{
	public LightSourceBurningItem(Entity lightSource, int lightLevel)
	{
		super(lightSource, lightLevel);
	}

	public boolean onUpdate()
	{
		return this.entity.isDead || this.entity.isBurning();
	}

}
