package com.dingcraft.ding.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.block.DingBlocks;

public class DingItems
{
	public static final Item dingItem = (new Item()).setUnlocalizedName("dingItem").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemWandDing dingWand = new ItemWandDing();
	public static final ItemPocketWatch pocketWatch = new ItemPocketWatch();
	public static final ItemFlashLight flashLight = new ItemFlashLight();
	public static final Item arrowTorch = (new Item()).setUnlocalizedName("torchArrow").setCreativeTab(CreativeTabs.tabTools);
	public static final Item arrowDeliverer = (new Item()).setUnlocalizedName("deliverArrow").setCreativeTab(CreativeTabs.tabTools);
	public static final Item arrowDelivererLoaded = (new Item()).setUnlocalizedName("loadedDeliverArrow").setMaxStackSize(1);
	
	public static void registerAll()
	{
		put(dingItem, 0, "dingItem");
		put(dingWand, 0, "dingWand");
		put(pocketWatch, 0, "pocketWatch");
		put(flashLight, 0, "flashLight");
		put(arrowTorch, 0, "torchArrow");
		put(arrowDeliverer, 0, "deliverArrow");
		put(arrowDelivererLoaded, 0, "loadedDeliverArrow");
	}
	
	public static void registerRecipes()
	{
		crafting(new ItemStack(DingBlocks.dingBlock), "AAA", "AAA", "AAA", 'A', DingItems.dingItem);
		crafting(new ItemStack(DingItems.dingItem, 9), DingBlocks.dingBlock);
		crafting(new ItemStack(DingItems.dingWand), "A", "B", "B", 'A', DingItems.dingItem, 'B', Items.stick);
		crafting(new ItemStack(DingItems.arrowTorch), Items.arrow, Blocks.torch, Items.slime_ball);
		crafting(new RecipeArrowDeliverer());
		crafting(new ItemStack(DingItems.arrowDeliverer), Items.arrow, Items.leather, Items.string);
		crafting(new ItemStack(DingItems.arrowDeliverer), Items.arrow, Items.rabbit_hide, Items.string);
		
		smelting(Items.gold_ingot, DingItems.dingItem, 4F);
	}
	
	private static void put(Item item, Object... resourceMap)
	{
		Dingcraft.proxy.registerItem(item, resourceMap);
	}
	
	private static void crafting(ItemStack output, Object... params)
	{
		if(params[0] instanceof String)
			GameRegistry.addRecipe(output, params);
		else if(params[0] instanceof Item || params[0] instanceof Block || params[0] instanceof ItemStack)
			GameRegistry.addShapelessRecipe(output, params);
	}
	
	private static void crafting(IRecipe recipe)
	{
		CraftingManager.getInstance().addRecipe(recipe);
	}
	
	private static void smelting(Item input, Item output, float xp)
	{
		GameRegistry.addSmelting(new ItemStack(input, 1, 0), new ItemStack(output, 1, 0), xp);
	}

}

