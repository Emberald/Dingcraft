package com.dingcraft.ding.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.renderer.*;

public class DingEntities
{
	private static int idCounter;
	
	public static void registerAll()
	{
		idCounter = 0;
		put(EntityArrowFission.class, "FissionArrow");
		put(EntityArrowVoid.class, "VoidArrow");
		put(EntityArrowTorch.class, "TorchArrow");
		put(EntityArrowSniper.class, "SniperArrow");
		put(EntityArrowDeliverer.class, "DeliverArrow");
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenderers(net.minecraft.client.renderer.entity.RenderManager renderManager)
	{
		setRenderer(EntityArrowFission.class, new RenderArrowBase(renderManager, "textures/entity/arrow.png"));
		setRenderer(EntityArrowVoid.class, new RenderArrowBase(renderManager, Dingcraft.MODID + ":textures/entity/arrowDing.png"));
		setRenderer(EntityArrowTorch.class, new RenderArrowBase(renderManager, "textures/entity/arrow.png"));
		setRenderer(EntityArrowSniper.class, new RenderArrowBase(renderManager, "textures/entity/arrow.png"));
		setRenderer(EntityArrowDeliverer.class, new RenderArrowBase(renderManager, "textures/entity/arrow.png"));
	}
	
	private static void put(Class<? extends Entity> entityClass, String entityName)
	{
		put(entityClass, entityName, 64, 10, true);
	}
	
	private static void put(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, entityName, ++idCounter, Dingcraft.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
	
	@SideOnly(Side.CLIENT)
	private static void setRenderer(Class<? extends Entity> entityClass, net.minecraft.client.renderer.entity.Render renderer)
	{
		net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(entityClass, renderer);
	}
}
