package com.dingcraft.ding.entitylighting;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

import com.google.common.collect.ImmutableMap;

public class LightSourceItem extends LightSourceEntity
{
	public static final Map VANILLA_ITEMS = ImmutableMap.of(Items.lava_bucket, 12, Items.glowstone_dust, 5, Items.nether_star, 10, Items.prismarine_crystals, 5);

	private static Map<Item, Integer> LightingItems = new HashMap<Item, Integer>() {{
		putAll(VANILLA_ITEMS);
	}};
	
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
		if(LightingItems.containsKey(item))
			return LightingItems.get(item).intValue();
		else if(block != null)
			return MathHelper.floor_float(block.getLightValue() * 0.75F);
		else
			return 0;
	}
	
	/**
     * Register a block that lights when dropped.
     * @param blockIn the block to register
     * @param lightValue amount of light emitted
     */
	public static void addLightSource(Block blockIn, int lightValue)
	{
		addLightSource(Item.getItemFromBlock(blockIn), lightValue);
	}
	
	/**
     * Register an item that lights when dropped.
     * @param itemIn the item to register
     * @param lightValue amount of light emitted
     */
	public static void addLightSource(Item itemIn, int lightValue)
	{		
		LightingItems.put(itemIn, lightValue);
	}
}
