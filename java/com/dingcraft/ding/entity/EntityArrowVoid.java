package com.dingcraft.ding.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.dingcraft.ding.DamageSourceDing;

public class EntityArrowVoid extends EntityArrowBase
{
	public EntityArrowVoid(World worldIn)
	{
		super(worldIn);
	}

	public EntityArrowVoid(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityArrowVoid(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn, shooter, target, velocity, inaccuracy);
	}

	public EntityArrowVoid(World worldIn, EntityLivingBase shooter, float charge)
	{
		super(worldIn, shooter, charge);
	}

	public void onUpdate()
	{
		super.onUpdate();
		if(!this.inGround)
		{
			for(int i = 0; i < 4; ++i)
                this.worldObj.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH,
                		this.posX + this.motionX * (double)i / 4.0D + (this.rand.nextFloat() - 0.5F) * 0.2F,
                		this.posY + this.motionY * (double)i / 4.0D + (this.rand.nextFloat() - 0.5F) * 0.2F,
                		this.posZ + this.motionZ * (double)i / 4.0D + (this.rand.nextFloat() - 0.5F) * 0.2F,
                		-this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
		}
	}
	
	protected int onEntityHit(Entity entityHit, float damage)
	{
		DamageSource damagesource;
		if(this.shooter != null)
			damagesource = DamageSourceDing.causeVoidDamage(this, this.shooter);
		else
			damagesource = DamageSourceDing.causeVoidDamage(this, this);
		if(entityHit.attackEntityFrom(damagesource, damage))
			return 3;
		else
			return 1;
	}

	protected boolean onBlockHit(BlockPos blockPos, Vec3 hitVec, EnumFacing sideHit)
	{
		return false;
	}
}
