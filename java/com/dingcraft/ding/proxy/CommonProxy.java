package com.dingcraft.ding.proxy;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.block.DingBlocks;
import com.dingcraft.ding.item.DingItems;

public class CommonProxy
{
	public void registerBlockAndItem()
	{
		for(Block block : DingBlocks.blocks)
			GameRegistry.registerBlock(block, this.getName(block));
		for(Item item : DingItems.items)
			GameRegistry.registerItem(item, this.getName(item));
	}
	
	public void registerEntity() 
	{
		int idCounter = 0;

		//entity
		for(Dingcraft.DingcraftEntity entity : Dingcraft.entities)
			EntityRegistry.registerModEntity(entity.entityClass, entity.name, ++idCounter, Dingcraft.instance, entity.trackingRange, entity.updateFrequency, entity.sendsVelocityUpdates);

		//tileentity
//		GameRegistry.registerTileEntity(com.dingcraft.ding.tileentity.TileEntityPhoton.class, "PhotonTileEntity");
	}
	
	public void registerRecipe() 
	{
		//recipe
		GameRegistry.addRecipe(new ItemStack(DingBlocks.dingBlock), "AAA", "AAA", "AAA", 'A', DingItems.dingItem);
		GameRegistry.addRecipe(new ItemStack(DingItems.dingItem, 9), "A", 'A', DingBlocks.dingBlock);
		GameRegistry.addRecipe(new ItemStack(DingItems.dingWand), "A", "B", "B", 'A', DingItems.dingItem, 'B', Items.stick);
		GameRegistry.addShapelessRecipe(new ItemStack(DingItems.arrowTorch), Items.arrow, Item.getItemFromBlock(Blocks.torch), Items.slime_ball);
		//smelting
		GameRegistry.addSmelting(new ItemStack(Items.gold_ingot),new ItemStack(DingItems.dingItem), 4F);
	}

	public void registerRenderer() {}
	
	public void registerHandler()
	{
		//Forge event handler registry
		MinecraftForge.EVENT_BUS.register(Dingcraft.handlerBow);
		MinecraftForge.EVENT_BUS.register(Dingcraft.handlerDrops);
		//FML event handler registry
		FMLCommonHandler.instance().bus().register(Dingcraft.handlerBow);
		FMLCommonHandler.instance().bus().register(Dingcraft.handlerDrops);
	}
	
	public String getName(Item item)
	{
		return item.getUnlocalizedName().substring(5);
	}

	public String getName(Block block)
	{
		return block.getUnlocalizedName().substring(5);
	}
	
}
