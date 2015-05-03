package com.dingcraft.ding.entity;

import com.dingcraft.ding.item.DingItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArrowDeliverer extends EntityArrowBase
{
	private ItemStack carriedItem;
	
	public EntityArrowDeliverer(World worldIn)
	{
		super(worldIn);
	}

	public EntityArrowDeliverer(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityArrowDeliverer(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn, shooter, target, velocity, inaccuracy);
	}

	public EntityArrowDeliverer(World worldIn, EntityLivingBase shooter, float charge)
	{
		super(worldIn, shooter, charge);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		if(this.carriedItem != null)
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			this.carriedItem.writeToNBT(itemTag);
			tagCompound.setTag("carriedItem", itemTag);
		}
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompound)
	{
		super.readEntityFromNBT(tagCompound);
		if(tagCompound.hasKey("carriedItem", 10))
			this.carriedItem = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("carriedItem"));
	}
	
	public void findCarriedItem(ItemStack[] inventory)
	{
		int i;
		for(i = 0; i < inventory.length; i++)
		{
			if(inventory[i] != null && inventory[i].getItem() == DingItems.arrowDelivererLoaded)
			{
				this.carriedItem = ItemStack.loadItemStackFromNBT(inventory[i].getTagCompound());
				inventory[i] = null;
				break;
			}
		}
	}
	
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!this.worldObj.isRemote && this.ticksInGround > 0 && this.arrowShake <= 0)
		{
			if (entityIn.inventory.addItemStackToInventory(new ItemStack(DingItems.arrowDeliverer, 1)))
			{
				this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				((WorldServer)entityIn.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S0DPacketCollectItem(this.getEntityId(), entityIn.getEntityId()));
				if(this.carriedItem != null)
					this.entityDropItem(this.carriedItem, 0.0F);
				this.setDead();
			}
		}
	}

	protected int onEntityHit(Entity entity, float damage)
	{
		if(entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage)
			return 0;
		if(this.carriedItem != null)
		{
			this.entityDropItem(this.carriedItem, 0.0F);
			this.carriedItem = null;
		}
		DamageSource damagesource;
		if (this.shooter == null)
			damagesource = (new EntityDamageSourceIndirect("arrow", this, this)).setProjectile();
		else
			damagesource = (new EntityDamageSourceIndirect("arrow", this, this.shooter)).setProjectile();
		if(entity.attackEntityFrom(damagesource, damage))
			return 3;
		else
			return 1;
	}

	protected boolean onBlockHit(BlockPos blockPos, Vec3 hitVec, EnumFacing sideHit)
	{
		return true;
	}

}
