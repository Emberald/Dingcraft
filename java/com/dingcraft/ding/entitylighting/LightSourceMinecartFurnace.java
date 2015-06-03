package com.dingcraft.ding.entitylighting;

import java.util.function.Function;

import net.minecraft.entity.Entity;
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
	
	public static void register()
	{
		EntityLighting.entityJoinWorldChecker.add(new Function<Entity, LightSourceEntity>() {
			public LightSourceEntity apply(Entity entity)
			{
				if(entity instanceof EntityMinecartFurnace)
					return new LightSourceMinecartFurnace((EntityMinecartFurnace)entity);
				else
					return null;
			}
		});
	}
}
