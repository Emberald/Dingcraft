package com.dingcraft.ding.entitylighting;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;

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
	
	public static void register()
	{
		EntityLighting.entityUpdateChecker.add(new Function<Entity, LightSourceEntity>() {
			public LightSourceEntity apply(Entity entity)
			{
				if(entity instanceof EntityArrow && entity.isBurning())
					return new LightSourceBurningItem(entity, 7);
				else
					return null;
			}
		});
	}

}
