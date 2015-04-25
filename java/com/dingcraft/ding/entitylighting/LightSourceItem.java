package com.dingcraft.ding.entitylighting;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

public class LightSourceItem extends LightSourceEntity
{
	Item item = null;
	
	public LightSourceItem(EntityItem item)
	{
		super(item, 0);
	}

	public boolean onUpdate()
	{
		if(this.item == null)
		{
			this.item = ((EntityItem)this.entity).getEntityItem().getItem();
			this.lightLevel = LightSourceItem.getLightFromItem(this.item);
			if(this.lightLevel == 0) return true;
		}
		return this.entity.isDead;
	}
		
	public static int getLightFromItem(Item item)
	{
		Block block = Block.getBlockFromItem(item);
		if(block != null)
			return MathHelper.floor_float(block.getLightValue() * 0.75F);
		else if(item.equals(Items.lava_bucket))
			return 12;
		else if(item.equals(Items.glowstone_dust))
			return 5;
		else if(item.equals(Items.nether_star))
			return 10;
		else if(item.equals(Items.prismarine_crystals))
			return 5;
		else
			return 0;
	}
}
