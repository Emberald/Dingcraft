package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class LightSourceBurningCreature extends LightSourceEntity {

	public LightSourceBurningCreature(EntityLivingBase lightSource)
	{
		super(lightSource, MathHelper.floor_float((20 - lightSource.deathTime) * 0.6F));
	}

	public boolean onUpdate()
	{
		this.lightLevel = MathHelper.floor_float((20 - ((EntityLivingBase)this.entity).deathTime) * 0.6F);
		return this.entity.isDead || !this.entity.isBurning();
	}

}
