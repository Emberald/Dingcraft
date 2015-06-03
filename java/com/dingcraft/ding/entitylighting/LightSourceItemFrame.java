package com.dingcraft.ding.entitylighting;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;

public class LightSourceItemFrame extends LightSourceEntity
{
	public LightSourceItemFrame(EntityItemFrame lightSource, int lightLevel)
	{
		super(lightSource, lightLevel);
	}

	public boolean onUpdate()
	{
		this.lightLevel = LightSourceItemFrame.getLightFromItemFrame((EntityItemFrame)this.entity);
		return this.entity.isDead || this.lightLevel == 0;
	}

	public static int getLightFromItemFrame(EntityItemFrame itemFrame)
	{
		ItemStack itemStack = itemFrame.getDisplayedItem();
		if(itemStack == null)
			return 0;
		else
			return LightSourceDroppedItem.getLightFromItem(itemStack.getItem());
	}
	
	public static void register()
	{
		EntityLighting.entityUpdateChecker.add(new Function<Entity, LightSourceEntity>() {
			public LightSourceEntity apply(Entity entity)
			{
				int light;
				if(entity instanceof EntityItemFrame && (light = getLightFromItemFrame((EntityItemFrame)entity)) != 0)
					return new LightSourceItemFrame((EntityItemFrame)entity, light);
				else
					return null;
			}
		});
	}
}
