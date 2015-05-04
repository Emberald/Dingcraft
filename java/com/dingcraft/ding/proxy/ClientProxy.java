package com.dingcraft.ding.proxy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.entity.DingEntities;
import com.dingcraft.ding.entitylighting.EntityLighting;

public class ClientProxy extends CommonProxy
{	
	private final List<Item> renderItems = new ArrayList<Item>();
	private final List<Integer> renderItemMetas = new ArrayList<Integer>();
	private final List<String> renderItemResources = new ArrayList<String>();
	
	public void registerBlock(Block block, Object... params)
	{
		super.registerBlock(block, params);
		for(int i = 0; i < params.length; i += 2)
		{
			renderItems.add(Item.getItemFromBlock(block));
			renderItemMetas.add((Integer)params[i]);
			renderItemResources.add(Dingcraft.MODID + ":" + (String)params[i + 1]);
		}
	}
	
	public void registerItem(Item item, Object... params)
	{
		super.registerItem(item, params);
		for(int i = 0; i < params.length; i += 2)
		{
			renderItems.add(item);
			renderItemMetas.add((Integer)params[i]);
			renderItemResources.add(Dingcraft.MODID + ":" + (String)params[i + 1]);
		}
	}
		
	public void registerRenderer()
	{
		super.registerRenderer();
				
		//blocks & items
		ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for(int i = 0; i < renderItems.size(); i++)
			itemModelMesher.register(renderItems.get(i), renderItemMetas.get(i), new ModelResourceLocation(renderItemResources.get(i), "inventory"));
		
		//entities
		DingEntities.registerRenderers(Minecraft.getMinecraft().getRenderManager());
	}

	public void registerHandler()
	{
		super.registerHandler();
		
		Dingcraft.entityLighting = new EntityLighting();
		MinecraftForge.EVENT_BUS.register(Dingcraft.entityLighting);
		FMLCommonHandler.instance().bus().register(Dingcraft.entityLighting);
	}

}
