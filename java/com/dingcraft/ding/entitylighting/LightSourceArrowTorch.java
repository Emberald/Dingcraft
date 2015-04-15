package com.dingcraft.ding.entitylighting;

import com.dingcraft.ding.entity.EntityArrowTorch;

public class LightSourceArrowTorch extends LightSourceEntity
{
	public LightSourceArrowTorch(EntityArrowTorch arrow)
	{
		super(arrow);
	}
	
	public int getLightLevel()
	{
		return 10;
	}

	public boolean shouldBeRemoved()
	{
		return this.entity.isDead;
	}

}
