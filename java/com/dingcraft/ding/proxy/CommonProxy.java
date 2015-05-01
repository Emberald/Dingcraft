package com.dingcraft.ding.proxy;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.block.BlockDing;
import com.dingcraft.ding.item.ItemDing;
import com.dingcraft.ding.item.ItemWandDing;

public class CommonProxy
{
	public void registerBlockAndItem()
	{
		for(Block block : Dingcraft.blocks)
			GameRegistry.registerBlock(block, this.getName(block));
		for(Item item : Dingcraft.items)
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
		GameRegistry.addRecipe(new ItemStack(BlockDing.instance), "AAA", "AAA", "AAA", 'A', ItemDing.instance);
		GameRegistry.addRecipe(new ItemStack(ItemDing.instance, 9), "A", 'A', BlockDing.instance);
		GameRegistry.addRecipe(new ItemStack(ItemWandDing.instance), "A", "B", "B", 'A', ItemDing.instance, 'B', Items.stick);
		//smelting
		GameRegistry.addSmelting(new ItemStack(Items.gold_ingot),new ItemStack(ItemDing.instance), 4F);
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
