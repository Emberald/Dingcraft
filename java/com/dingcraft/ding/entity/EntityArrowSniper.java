package com.dingcraft.ding.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrowSniper extends EntityArrowBase {

	public EntityArrowSniper(World worldIn)
	{
		super(worldIn);
		this.gravity = 0.0F;
		this.dragInAir = 1.0F;
		this.dragInWater = 0.9F;
	}

	public EntityArrowSniper(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.gravity = 0.0F;
		this.dragInAir = 1.0F;
		this.dragInWater = 0.9F;
	}

	public EntityArrowSniper(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn, shooter, target, velocity, inaccuracy);
		this.gravity = 0.0F;
		this.dragInAir = 1.0F;
		this.dragInWater = 0.9F;
	}

	public EntityArrowSniper(World worldIn, EntityLivingBase shooter, float charge)
	{
		super(worldIn, shooter, charge);
		this.gravity = 0.0F;
		this.dragInAir = 1.0F;
		this.dragInWater = 0.9F;
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
			return 3;
		else
			return 1;
	}

	protected boolean onBlockHit(BlockPos blockPos, Vec3 hitVec, EnumFacing sideHit)
	{
		return true;
	}
	
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
	{
		super.setThrowableHeading(x, y, z, velocity, inaccuracy);
		this.motionX *= 5.0D;
		this.motionY *= 5.0D;
		this.motionZ *= 5.0D;
	}

	public void onUpdate()
	{
		super.onUpdate();
		if(this.posY > 256.0D && this.motionY >= 0.0D)
			this.setDead();
	}
	
}
