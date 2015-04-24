package com.dingcraft.ding.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.block.BlockDing;
import com.dingcraft.ding.entity.EntityArrowFission;
import com.dingcraft.ding.entity.EntityArrowTorch;
import com.dingcraft.ding.entity.EntityArrowVoid;
import com.dingcraft.ding.entity.EntityOmnipunch;
import com.dingcraft.ding.item.ItemDing;
import com.dingcraft.ding.item.ItemPocketWatch;
import com.dingcraft.ding.item.ItemWandDing;
import com.dingcraft.ding.renderer.RenderArrowBase;
import com.dingcraft.ding.renderer.RenderOmnipunch;
import com.dingcraft.ding.skill.SkillOmnipunch;

public class ClientProxy extends CommonProxy
{	
	public void register()
	{
		super.register();
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
      
		//blocks' items
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Dingcraft.dingBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + BlockDing.name, "inventory"));
//		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Dingcraft.meteorBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + BlockMeteor.name, "inventory"));
		//items
		renderItem.getItemModelMesher().register(Dingcraft.dingItem, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ItemDing.name, "inventory"));
		renderItem.getItemModelMesher().register(Dingcraft.dingWand, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ItemWandDing.name, "inventory"));
		renderItem.getItemModelMesher().register(Dingcraft.pocketWatch, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ItemPocketWatch.name, "inventory"));
		renderItem.getItemModelMesher().register(Dingcraft.omnipunch, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + SkillOmnipunch.name, "inventory"));
		//entities
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowFission.class, new RenderArrowBase(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowVoid.class, new RenderArrowBase(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowTorch.class, new RenderArrowBase(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityOmnipunch.class, new RenderOmnipunch(renderManager));
	}
}
