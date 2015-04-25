package com.dingcraft.ding.proxy;

import java.util.ArrayList;

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
import com.dingcraft.ding.entity.EntityArrowFission;
import com.dingcraft.ding.entity.EntityArrowTorch;
import com.dingcraft.ding.entity.EntityArrowVoid;

public class CommonProxy
{
	public void registerBlockAndItem()
	{
		for(Block block : Dingcraft.listBlock)
		{
			GameRegistry.registerBlock(block, this.getName(block));			
		}
		for(Item item : Dingcraft.listItem)
		{
			GameRegistry.registerItem(item, this.getName(item));			
		}
	}
	
	public void registerEntity() 
	{
		//entity
		EntityRegistry.registerModEntity(EntityArrowFission.class, "FissionArrow", 1, Dingcraft.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityArrowVoid.class, "VoidArrow", 2, Dingcraft.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityArrowTorch.class, "TorchArrow", 3, Dingcraft.instance, 64, 10, true);
//		EntityRegistry.registerModEntity(EntityOmnipunch.class, "Omnipunch", 4, Dingcraft.instance, 64, 10, true);
		//tileentity
//		GameRegistry.registerTileEntity(com.dingcraft.ding.tileentity.TileEntityPhoton.class, "PhotonTileEntity");
	}
	
	public void registerRecipe() 
	{
		//recipe
		GameRegistry.addRecipe(new ItemStack(Dingcraft.dingBlock),"AAA","AAA","AAA",'A',new ItemStack(Dingcraft.dingItem));
		GameRegistry.addRecipe(new ItemStack(Dingcraft.dingItem,9),"A",'A',Dingcraft.dingBlock);
		GameRegistry.addRecipe(new ItemStack(Dingcraft.dingWand),"A","B","B",'A',new ItemStack(Dingcraft.dingItem),'B',Items.stick);
		//smelting
		GameRegistry.addSmelting(new ItemStack(Items.gold_ingot),new ItemStack(Dingcraft.dingItem), 4F);
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
	
	public String getName(Object objectIn)
	{
		String name = null;
		if(objectIn instanceof Item)
		{
			name = ((Item)objectIn).getUnlocalizedName();
		}
		else if(objectIn instanceof Block)
		{
			name = ((Block)objectIn).getUnlocalizedName();
		}
		return name.substring(5, name.length());
	}

}
