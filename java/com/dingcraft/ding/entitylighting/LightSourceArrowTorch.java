package com.dingcraft.ding.entitylighting;

import net.minecraft.util.BlockPos;

import com.dingcraft.ding.entity.EntityArrowTorch;

public class LightSourceArrowTorch extends LightSourceEntity
{
	public LightSourceArrowTorch(EntityArrowTorch arrow)
	{
		super(arrow, 10);
	}

	public boolean onUpdate()
	{
		return this.entity.isDead;
	}

}
