package com.dingcraft.ding.entitylighting;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

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
		if(LightSourceSpotlight.hasLight((EntityPlayer)this.entity))
		{
			if(!flag)
			{
				Dingcraft.entityLighting.addEntity(new LightSourceSpotlight((EntityPlayer)this.entity));				
				flag = true;
			}
		}
		else
			flag = false;

		ItemStack itemStack = ((EntityPlayer)this.entity).getHeldItem();
		if(itemStack != null)
			this.lightLevel = LightSourceDroppedItem.getLightFromItem(itemStack.getItem());
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
	
	public static void register()
	{
		EntityLighting.entityJoinWorldChecker.add(new Function<Entity, LightSourceEntity>() {
			public LightSourceEntity apply(Entity entity)
			{
				if(entity instanceof EntityPlayer)
					return new LightSourcePlayer((EntityPlayer)entity);
				else
					return null;
			}
		});
	}

}
