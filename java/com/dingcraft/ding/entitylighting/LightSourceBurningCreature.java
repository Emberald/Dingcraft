package com.dingcraft.ding.entitylighting;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
	
	public static void register()
	{
		EntityLighting.entityUpdateChecker.add(new Function<Entity, LightSourceEntity>() {
			public LightSourceEntity apply(Entity entity)
			{
				if(entity instanceof EntityLivingBase && entity.isBurning())
					return new LightSourceBurningCreature((EntityLivingBase)entity);
				else
					return null;
			}
		});
	}

}
