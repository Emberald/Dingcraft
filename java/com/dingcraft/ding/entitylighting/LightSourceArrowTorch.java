package com.dingcraft.ding.entitylighting;

import java.util.function.Function;

import net.minecraft.entity.Entity;

import com.dingcraft.ding.entity.EntityArrowTorch;

public class LightSourceArrowTorch extends LightSourceEntity
{
	public LightSourceArrowTorch(EntityArrowTorch arrow)
	{
		super(arrow, 12);
	}

	public boolean onUpdate()
	{
		return this.entity.isDead;
	}
	
	public static void register()
	{
		EntityLighting.entityJoinWorldChecker.add(new Function<Entity, LightSourceEntity>() {
			public LightSourceEntity apply(Entity entity)
			{
				if(entity instanceof EntityArrowTorch)
					return new LightSourceArrowTorch((EntityArrowTorch)entity);
				else
					return null;
			}
		});
	}
}
