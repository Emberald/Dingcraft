package com.dingcraft.ding.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.entity.EntityArrowFission;
import com.dingcraft.ding.entity.EntityArrowTorch;
import com.dingcraft.ding.entity.EntityArrowVoid;
import com.dingcraft.ding.entitylighting.EntityLighting;
import com.dingcraft.ding.renderer.RenderArrowBase;

public class ClientProxy extends CommonProxy
{	
	public void registerRenderer()
	{
		super.registerRenderer();

		//renderers are client side only, so register here.
		ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		//blocks' items
		for(Block block : Dingcraft.listBlock)
			itemModelMesher.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + this.getName(block), "inventory"));
		//items
		for(Item item : Dingcraft.listItem)
			itemModelMesher.register(item, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + this.getName(item), "inventory"));
		//entities
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowFission.class, new RenderArrowBase(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowVoid.class, new RenderArrowBase(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowTorch.class, new RenderArrowBase(renderManager));
//		RenderingRegistry.registerEntityRenderingHandler(EntityOmnipunch.class, new RenderOmnipunch(renderManager));
	}
	
	public void registerHandler()
	{
		super.registerHandler();
		
		Dingcraft.entityLighting = new EntityLighting();
		MinecraftForge.EVENT_BUS.register(Dingcraft.entityLighting);
		FMLCommonHandler.instance().bus().register(Dingcraft.entityLighting);
	}
	
	public void registerBlockAndItem()
	{
		super.registerBlockAndItem();
	}

}
