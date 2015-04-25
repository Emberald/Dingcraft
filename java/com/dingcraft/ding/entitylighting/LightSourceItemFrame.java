package com.dingcraft.ding.entitylighting;

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
			return LightSourceItem.getLightFromItem(itemStack.getItem());
	}
}
