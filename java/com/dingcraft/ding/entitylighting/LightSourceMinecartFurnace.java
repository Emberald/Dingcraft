package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.item.EntityMinecartFurnace;

public class LightSourceMinecartFurnace extends LightSourceEntity
{
	public LightSourceMinecartFurnace(EntityMinecartFurnace lightSource)
	{
		super(lightSource, 0);
	}

	public boolean onUpdate()
	{
		this.lightLevel = (this.entity.getDataWatcher().getWatchableObjectByte(16) & 1) == 0? 0: 13;
		return this.entity.isDead;
	}
}
