package com.dingcraft.ding.entitylighting;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import com.google.common.collect.ImmutableSet;

public class LightSourceSpotlight extends LightSourceEntity
{
	public static final Set VANILLA_ITEMS_ON_HEAD = ImmutableSet.of(Items.golden_helmet);
	public static final Set VANILLA_ITEMS_IN_HAND = ImmutableSet.of();

	private static Set<Item> ItemsOnHead = new HashSet<Item>() {{
		addAll(VANILLA_ITEMS_ON_HEAD);
	}};
	private static Set<Item> ItemsInHand = new HashSet<Item>() {{
		addAll(VANILLA_ITEMS_IN_HAND);
	}};

	private int newLightLvl = 0;
	
	public LightSourceSpotlight(EntityPlayer lightSource)
	{
		super(lightSource, 0);		
		this.calculateLight();
	}

	public boolean onUpdate()
	{
		this.lightLevel = this.newLightLvl;
		return !hasLight((EntityPlayer)this.entity);
	}

	public BlockPos hasMoved()
	{
		BlockPos blockPosOld = this.lightBlockPos;
		this.calculateLight();
		if(!this.lightBlockPos.equals(blockPosOld))
			return blockPosOld;
		else
			return null;
	}
	
	private void calculateLight()
	{
		Vec3 posPlayer = new Vec3(this.entity.posX, this.entity.posY + this.entity.getEyeHeight(), this.entity.posZ);
		double pitch = -this.entity.rotationPitch * Math.PI / 180.0D;
		double yaw = (this.entity.rotationYaw + 90.0D) * Math.PI / 180.0D;
		double x = 30.0D * Math.cos(pitch) * Math.cos(yaw);
		double y = 30.0D * Math.sin(pitch);
		double z = 30.0D * Math.cos(pitch) * Math.sin(yaw);
		MovingObjectPosition MOP = this.entity.worldObj.rayTraceBlocks(posPlayer, posPlayer.addVector(x, y, z));
		if(MOP != null)
		{
			this.lightBlockPos = MOP.getBlockPos().offset(MOP.sideHit);
			double dist = posPlayer.distanceTo(new Vec3(this.lightBlockPos.getX(), this.lightBlockPos.getY(), this.lightBlockPos.getZ()));
			this.newLightLvl = Math.max(MathHelper.floor_double((30 - dist) / 2.0D), 0);
		}
		else
			this.newLightLvl = 0;
	}
	
	/**
     * Register a block that projects light in player's direction.
     * @param blockIn the block to register
     * @param slotIn the place where the block lights (0 = held, 1 = helmet's slot)
     */
	public static void addLightSource(Block blockIn, int slotIn)
	{
		addLightSource(Item.getItemFromBlock(blockIn), slotIn);
	}
	
	/**
     * Register an item that projects light in player's direction.
     * @param itemIn the item to register
     * @param slotIn the place where the item lights (0 = held, 1 = helmet's slot)
     */
	public static void addLightSource(Item itemIn, int slotIn)
	{		
		if(slotIn == 0)
		{
			ItemsInHand.add(itemIn);
		}
		else if(slotIn == 1)
		{
			ItemsOnHead.add(itemIn);
		}
	}
	
	/**
     * Check whether player has lighting item.
     */
	public static boolean hasLight(EntityPlayer entityPlayer)
	{
		return (hasLightOnHead(entityPlayer) || hasLightInHand(entityPlayer));
	}

	public static boolean hasLightOnHead(EntityPlayer entityPlayer)
	{
		ItemStack itemStack = entityPlayer.getCurrentArmor(3);
		if(itemStack != null && ItemsOnHead.contains(itemStack.getItem()))
		{
			return true;
		}
		return false;
	}
	
	public static boolean hasLightInHand(EntityPlayer entityPlayer)
	{
		ItemStack itemStack = entityPlayer.getHeldItem();
		if(itemStack != null && ItemsInHand.contains(itemStack.getItem()))
		{
			return true;
		}
		return false;
	}
}
