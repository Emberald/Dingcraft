package com.dingcraft.ding.entity;

import com.dingcraft.ding.item.DingItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrowTorch extends EntityArrowBase
{
	public EntityArrowTorch(World worldIn)
	{
		super(worldIn);
		this.arrowItem = DingItems.arrowTorch;
	}

	public EntityArrowTorch(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.arrowItem = DingItems.arrowTorch;
	}
	
	public EntityArrowTorch(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn, shooter, target, velocity, inaccuracy);
		this.arrowItem = DingItems.arrowTorch;
	}
	
	public EntityArrowTorch(World worldIn, EntityLivingBase shooter, float charge)
	{
		super(worldIn, shooter, charge);
		this.arrowItem = DingItems.arrowTorch;
	}
		
	protected int onEntityHit(Entity entity, float damage)
	{
		if(entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage)
			return 0;
		DamageSource damagesource;
		if (this.shooter == null)
			damagesource = (new EntityDamageSourceIndirect("arrow", this, this)).setProjectile();
		else
			damagesource = (new EntityDamageSourceIndirect("arrow", this, this.shooter)).setProjectile();
		if(entity.attackEntityFrom(damagesource, damage))
		{
			entity.setFire(10);
			return 3;
		}
		else
			return 1;
	}

	protected boolean onBlockHit(BlockPos blockPosHit, Vec3 hitVec, EnumFacing sideHit)
	{
		BlockPos blockPosIn = blockPosHit.offset(sideHit);
		Block blockIn = this.worldObj.getBlockState(blockPosIn).getBlock();
		if(!this.worldObj.isRemote)
		{
			if(!sideHit.equals(EnumFacing.DOWN) && !blockIn.equals(Blocks.torch) && blockIn.getMaterial().isReplaceable())
			{
				if((sideHit.equals(EnumFacing.UP) && this.worldObj.getBlockState(blockPosHit).getBlock().canPlaceTorchOnTop(this.worldObj, blockPosHit))
						|| (sideHit.getAxis().isHorizontal() && this.worldObj.isSideSolid(blockPosHit, sideHit, false)))
				{
					IBlockState blockState1 = ((BlockTorch)Blocks.torch).onBlockPlaced(this.worldObj, blockPosIn, sideHit, (float)hitVec.xCoord, (float)hitVec.yCoord, (float)hitVec.zCoord, 0, (EntityLivingBase)null);
					this.worldObj.setBlockState(blockPosIn, blockState1, 3);
					this.setDead();
				}
			}
		}
		
		return true;
	}
		
	public void onUpdate()
	{
		super.onUpdate();
		if(this.inWater)
		{
			EntityArrow arrow = new EntityArrow(this.worldObj, this.posX, this.posY, this.posZ);
			arrow.shootingEntity = this.shooter;
			arrow.motionX = this.motionX;
			arrow.motionY = this.motionY;
			arrow.motionZ = this.motionZ;
			arrow.canBePickedUp = this.canBePickedUp;
			if(!this.worldObj.isRemote && !this.isDead)
			{
				this.worldObj.spawnEntityInWorld(arrow);
	            this.entityDropItem(new ItemStack(Blocks.torch, 1), 0.0F);
			}
			this.setDead();
		}
		
		if(this.inGround)
			this.ticksInGround += 2;
		
		if(!this.isDead)
		{
			float f = this.rand.nextFloat();
			if(f < 0.2F)
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + this.rand.nextDouble() * 0.2D, this.posY + this.rand.nextDouble() * 0.2D + 0.1D, this.posZ + this.rand.nextDouble() * 0.2D, 0.0D, 0.0D, 0.0D, new int[0]);
			else if(f < 0.4F)
				this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + this.rand.nextDouble() * 0.2D, this.posY + this.rand.nextDouble() * 0.2D, this.posZ + this.rand.nextDouble() * 0.2D, 0.0D, 0.01D, 0.0D, new int[0]);
		}
	}
}
