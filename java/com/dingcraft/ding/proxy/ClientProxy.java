package com.dingcraft.ding.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.RenderArrowVoid;
import com.dingcraft.ding.entity.EntityArrowVoid;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers() 
	{
    	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
    	//blocks' items
    	renderItem.getItemModelMesher().register(Item.getItemFromBlock(Dingcraft.dingBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + Dingcraft.dingBlock.getName(), "inventory"));
    			
    	//items
    	renderItem.getItemModelMesher().register(Dingcraft.dingItem, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + Dingcraft.dingItem.getName(), "inventory"));
    	renderItem.getItemModelMesher().register(Dingcraft.dingWand, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + Dingcraft.dingWand.getName(), "inventory"));
    			
    	//entities
    	RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
    	RenderingRegistry.registerEntityRenderingHandler(EntityArrowVoid.class, new RenderArrowVoid(renderManager));
	}
}
