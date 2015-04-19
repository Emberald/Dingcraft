package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class LightSourceMinerHelmet extends LightSourceEntity
{
	public static final Item boundItem = Items.golden_helmet;
	private int newLightLvl = 0;
	
	public LightSourceMinerHelmet(EntityPlayer lightSource)
	{
		super(lightSource, 0);
		this.calculateLight();
	}

	public boolean onUpdate()
	{
		this.lightLevel = this.newLightLvl;
		ItemStack armor = ((EntityPlayer)this.entity).getCurrentArmor(3);
		if(armor == null)
			return true;
		else
			return !armor.getItem().equals(boundItem);
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
}
