package com.dingcraft.ding.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.block.BlockDing;
import com.dingcraft.ding.entity.EntityArrowFission;
import com.dingcraft.ding.entity.EntityArrowTorch;
import com.dingcraft.ding.entity.EntityArrowVoid;
import com.dingcraft.ding.entitylighting.EntityLighting;
import com.dingcraft.ding.item.ItemDing;
import com.dingcraft.ding.item.ItemWandDing;
import com.dingcraft.ding.renderer.RenderArrowGeneral;

public class ClientProxy extends CommonProxy
{
	public void register()
	{
		super.register();
		Dingcraft.entityLighting = new EntityLighting();
		MinecraftForge.EVENT_BUS.register(Dingcraft.entityLighting);
		FMLCommonHandler.instance().bus().register(Dingcraft.entityLighting);

		//renderers are client side only, so register here.
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//blocks' items
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Dingcraft.dingBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + BlockDing.name, "inventory"));
//		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Dingcraft.meteorBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + BlockMeteor.name, "inventory"));
		//items
		renderItem.getItemModelMesher().register(Dingcraft.dingItem, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ItemDing.name, "inventory"));
		renderItem.getItemModelMesher().register(Dingcraft.dingWand, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ItemWandDing.name, "inventory"));
		//entities
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowFission.class, new RenderArrowGeneral(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowVoid.class, new RenderArrowGeneral(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowTorch.class, new RenderArrowGeneral(renderManager));
	}
}
