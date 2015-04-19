package com.dingcraft.ding.entitylighting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import com.dingcraft.ding.Dingcraft;

public class LightSourcePlayer extends LightSourceEntity
{
	private boolean flag = false;
	
	public LightSourcePlayer(EntityPlayer player)
	{
		super(player, 0);
	}

	public boolean onUpdate()
	{
		if(this.flag)
		{
			Vec3 posPlayer = new Vec3(this.entity.posX, this.entity.posY + this.entity.getEyeHeight(), this.entity.posZ);
			double dist = posPlayer.distanceTo(new Vec3(this.lightBlockPos.getX(), this.lightBlockPos.getY(), this.lightBlockPos.getZ()));
			this.lightLevel = Math.max(MathHelper.floor_double((30 - dist) / 2.0D), 0);
		}
		else
		{
			ItemStack itemStack = ((EntityPlayer)this.entity).getHeldItem();
			if(itemStack != null)
				this.lightLevel = LightSourceItem.getLightFromItem(itemStack.getItem());
			else
				this.lightLevel = 0;
		}
		return this.entity.isDead;
	}
	
	public BlockPos hasMoved()
	{
		ItemStack itemStack = ((EntityPlayer)this.entity).getHeldItem();
		if(itemStack != null)
		{
			if(itemStack.getItem().equals(Dingcraft.dingWand))
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
					BlockPos blockPosNew = MOP.getBlockPos().offset(MOP.sideHit);
					this.flag = true;
					if(blockPosNew.equals(this.lightBlockPos))
						return null;
					else
					{
						BlockPos blockPosOld = this.lightBlockPos;
						this.lightBlockPos = blockPosNew;
						return blockPosOld;
					}
				}
			}
		}
		this.flag = false;
		BlockPos blockPosNew = new BlockPos(this.entity.posX, this.entity.posY + this.entity.getEyeHeight(), this.entity.posZ);
		if(!blockPosNew.equals(this.lightBlockPos))
		{
			BlockPos blockPosOld = this.lightBlockPos;
			this.lightBlockPos = blockPosNew;
			return blockPosOld;
		}
		else
			return null;

	}

}
