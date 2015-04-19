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
		ItemStack itemStack = ((EntityPlayer)this.entity).getCurrentArmor(3);
		if(itemStack == null)
			flag = false;
		else if(itemStack.getItem().equals(LightSourceMinerHelmet.boundItem))
		{
			if(!flag)
				Dingcraft.entityLighting.addEntity(new LightSourceMinerHelmet((EntityPlayer)this.entity));
			flag = true;
		}
		else
			flag = false;
		itemStack = ((EntityPlayer)this.entity).getHeldItem();
		if(itemStack != null)
			this.lightLevel = LightSourceItem.getLightFromItem(itemStack.getItem());
		else
			this.lightLevel = 0;
		return this.entity.isDead;
	}
	
	public BlockPos hasMoved()
	{
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
