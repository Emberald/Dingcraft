package com.dingcraft.ding.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityArrowBase extends Entity implements IProjectile
{
	protected Entity shooter;
	public int canBePickedUp;
	protected int ticksInGround;
	protected int ticksInAir;
	public int arrowShake;
	protected double damage = 2.0F;
	protected int knockbackStrength;
	protected boolean inGround;
	public boolean isCritical;
	protected Block tileIn;
	protected int tilePosX;
	protected int tilePosY;
	protected int tilePosZ;
	protected int tileData;
	private boolean firstUpdate = true;

	//Change these properties in subclass constructor when necessary.
	protected float dragInAir = 0.99F;
	protected float dragInWater = 0.6F;
	protected float gravity = 0.05F;
	protected EnumParticleTypes typePartical = EnumParticleTypes.CRIT;
	protected Item arrowItem = Items.arrow;
	
	public EntityArrowBase(World worldIn)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityArrowBase(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(x, y, z);
	}

	public EntityArrowBase(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.shooter = shooter;
		if (shooter instanceof EntityPlayer)
			this.canBePickedUp = 1;
		this.posY = shooter.posY + (double)shooter.getEyeHeight() - 0.1D;
		double d0 = target.posX - shooter.posX;
		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - this.posY;
		double d2 = target.posZ - shooter.posZ;
		double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		if (d3 >= 1.0E-7D)
		{
			float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			this.setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
			float f4 = (float)(d3 * 0.2D);
			this.setThrowableHeading(d0, d1 + (double)f4, d2, velocity, inaccuracy);
		}
	}

	public EntityArrowBase(World worldIn, EntityLivingBase shooter, float charge)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.shooter = shooter;
		if (shooter instanceof EntityPlayer)
			this.canBePickedUp = 1;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= 0.1D;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, charge * 1.5F, 1.0F);
	}

	protected void entityInit()
	{
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}
	
	public NBTTagCompound func_174819_aU()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("s", this.shooter == null? 0: this.shooter.getEntityId());
		tag.setBoolean("c", this.isCritical);
		return tag;
	}
	
	@SideOnly(Side.CLIENT)
	public void func_174834_g(NBTTagCompound tagCompound)
	{
		if(tagCompound.hasKey("s"))
			this.shooter = this.worldObj.getEntityByID(tagCompound.getInteger("s"));
		if(tagCompound.hasKey("c"))
			this.isCritical = tagCompound.getBoolean("c");
		if(tagCompound.hasKey("p"))
		{
			int Pos[] = tagCompound.getIntArray("p");
			this.tilePosX = Pos[0];
			this.tilePosY = Pos[1];
			this.tilePosZ = Pos[2];
			IBlockState blockState = this.worldObj.getBlockState(new BlockPos(this.tilePosX, this.tilePosY, this.tilePosZ));
			this.tileIn = blockState.getBlock();
			this.tileData = this.tileIn.getMetaFromState(blockState);
			this.arrowShake = 7;
		}
	}

	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
	{
		float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= (double)f2;
		y /= (double)f2;
		z /= (double)f2;
		x += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.0075D * (double)inaccuracy;
		y += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.0075D * (double)inaccuracy;
		z += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.0075D * (double)inaccuracy;
		x *= (double)velocity;
		y *= (double)velocity;
		z *= (double)velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f3 = MathHelper.sqrt_double(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f3) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}

	@SideOnly(Side.CLIENT)
	public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
	{
		this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
		this.setRotation(p_180426_7_, p_180426_8_);
	}

	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(x * x + z * z);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setShort("life", (short)this.ticksInGround);
		tagCompound.setInteger("flyTime", this.ticksInAir);
		tagCompound.setByte("shake", (byte)this.arrowShake);
		tagCompound.setByte("pickup", (byte)this.canBePickedUp);
		tagCompound.setDouble("damage", this.damage);
		tagCompound.setInteger("knockback", this.knockbackStrength);
		tagCompound.setByte("flags", (byte)((this.firstUpdate? 4: 0) | (this.isCritical? 2: 0) | (this.inGround? 1: 0)));
		ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.tileIn);
		tagCompound.setString("tile", resourcelocation == null ? "" : resourcelocation.toString());
		tagCompound.setInteger("tileX", this.tilePosX);
		tagCompound.setInteger("tileY", this.tilePosY);
		tagCompound.setInteger("tileZ", this.tilePosZ);
		tagCompound.setByte("tileData", (byte)this.tileData);
	}

	public void readEntityFromNBT(NBTTagCompound tagCompound)
	{
		this.ticksInGround = tagCompound.getShort("life");
		this.ticksInAir = tagCompound.getInteger("flyTime");
		this.arrowShake = tagCompound.getByte("shake");
		this.canBePickedUp = tagCompound.getByte("pickup");
		this.damage = tagCompound.getDouble("damage");
		this.knockbackStrength = tagCompound.getInteger("knockback");
		byte flags = tagCompound.getByte("flags");
		this.firstUpdate = (flags & 4) != 0;
		this.isCritical = (flags & 2) != 0;
		this.inGround = (flags & 1) != 0;
		this.tilePosX = tagCompound.getInteger("tileX");
		this.tilePosY = tagCompound.getInteger("tileY");
		this.tilePosZ = tagCompound.getInteger("tileZ");
		this.tileIn = Block.getBlockFromName(tagCompound.getString("tile"));
		this.tileData = tagCompound.getByte("tileData");
	}

	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!this.worldObj.isRemote && this.ticksInGround > 0 && this.arrowShake <= 0)
		{
			boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode;
			if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(new ItemStack(this.arrowItem, 1)))
				flag = false;
			if (flag)
			{
				this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				((WorldServer)entityIn.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S0DPacketCollectItem(this.getEntityId(), entityIn.getEntityId()));
				this.setDead();
			}
		}
	}

	protected boolean canTriggerWalking()
	{
		return false;
	}

	public void setDamage(double damage)
	{
		this.damage = damage;
	}

	public double getDamage()
	{
		return this.damage;
	}

	public void setKnockbackStrength(int knockback)
	{
		this.knockbackStrength = knockback;
	}

	public boolean canAttackWithItem()
	{
		return false;
	}

	public void onUpdate()
	{
		super.onUpdate();		
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
		}
		int i;
		if(this.arrowShake > 0)
			--this.arrowShake;
		if(this.inGround)
		{	//when in ground, check whether the tile is broken
			IBlockState blockState = this.worldObj.getBlockState(new BlockPos(this.tilePosX, this.tilePosY, this.tilePosZ));
			Block block = blockState.getBlock();
			if (block == this.tileIn && block.getMetaFromState(blockState) == this.tileData)
			{
				++this.ticksInGround;
				if (this.ticksInGround >= 1200)
					this.setDead();
			}
			else
			{
				this.ticksInGround = 0;
				this.inGround = false;
				this.motionX =  this.motionZ = 0;
				this.motionY = -(double)this.gravity;
			}
		}
		else
		{	//when in air,
			++this.ticksInAir;
			//check whether any entities are hit
			Vec3 currPos = new Vec3(this.posX, this.posY, this.posZ);
			Vec3 nextPos = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			Entity entityHit = null;
			Entity currEntity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			MovingObjectPosition MOP;
			double minDist = -1D, dist;
			float speed;
			for (i = 0; i < list.size(); ++i)
			{
				currEntity = (Entity)list.get(i);
				if (currEntity.canBeCollidedWith() && (currEntity != this.shooter || this.ticksInAir >= 5))
				{
					MOP = currEntity.getEntityBoundingBox().expand(0.3D, 0.3D, 0.3D).calculateIntercept(currPos, nextPos);
					if(MOP != null)
					{
						dist = nextPos.distanceTo(MOP.hitVec);
						if(dist < minDist || minDist < 0)
						{
							entityHit = currEntity;
							minDist = dist;
						}
					}
				}
			}
			//when hitting an entity
			if(entityHit != null)
			{
				//when hitting a player, check whether it can be hit
				if(entityHit instanceof EntityPlayer && this.shooter instanceof EntityPlayer)
					if(!((EntityPlayer)this.shooter).canAttackPlayer((EntityPlayer)entityHit))
						entityHit = null;
			}
			//when hitting an entity
			if(entityHit != null)
			{
				speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				int damageAmount = MathHelper.ceiling_double_int((double)speed * this.damage);
				if (this.isCritical)
					damageAmount += this.rand.nextInt(damageAmount / 2 + 2);
				int flag = this.onEntityHit(entityHit, damageAmount);
				if((flag & 2) != 0)//if causes damage, also knockback, set fire, etc.
				{
					if(this.isBurning())
						entityHit.setFire(5);
					if(entityHit instanceof EntityLivingBase)
					{
						if (this.knockbackStrength > 0)
						{
							speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
							if (speed > 0.0F)
								entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6D / (double)speed, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6D / (double)speed);
						}
						if (this.shooter instanceof EntityLivingBase)
						{
							EnchantmentHelper.func_151384_a((EntityLivingBase)entityHit, this.shooter);
							EnchantmentHelper.func_151385_b((EntityLivingBase)this.shooter, (EntityLivingBase)entityHit);
						}
						if (this.shooter != null && entityHit != this.shooter && entityHit instanceof EntityPlayer && this.shooter instanceof EntityPlayerMP)
							((EntityPlayerMP)this.shooter).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
						if (!this.worldObj.isRemote && (flag & 1) != 0)
							((EntityLivingBase)entityHit).setArrowCountInEntity(((EntityLivingBase)entityHit).getArrowCountInEntity() + 1);
					}
					this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					if((flag & 1) != 0)
						this.setDead();
				}
				else if((flag & 1) != 0)//if hits but cancels damage, bounce back
				{
					this.motionX *= -0.1D;
					this.motionY *= -0.1D;
					this.motionZ *= -0.1D;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.isCritical = false;
				}
			}
			boolean flag = false;
			if(!this.isDead)
			{	//if no entities were reported hit, check whether any blocks are hit
				currPos = new Vec3(this.posX, this.posY, this.posZ);
				nextPos = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);	
				MOP = this.worldObj.rayTraceBlocks(currPos, nextPos, false, true, false);
				if(MOP != null)
				{
					BlockPos blockPos = MOP.getBlockPos();
					flag = onBlockHit(blockPos, MOP.hitVec, MOP.sideHit);
					if(flag)
					{
						this.tilePosX = blockPos.getX();
						this.tilePosY = blockPos.getY();
						this.tilePosZ = blockPos.getZ();
						IBlockState blockState = this.worldObj.getBlockState(blockPos);
						this.tileIn = blockState.getBlock();
						this.tileData = this.tileIn.getMetaFromState(blockState);
						this.motionX = MOP.hitVec.xCoord - this.posX;
						this.motionY = MOP.hitVec.yCoord - this.posY;
						this.motionZ = MOP.hitVec.zCoord - this.posZ;
						speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
						this.posX -= this.motionX / (double)speed * 0.05D;
						this.posY -= this.motionY / (double)speed * 0.05D;
						this.posZ -= this.motionZ / (double)speed * 0.05D;
						this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
						this.arrowShake = 7;
						this.isCritical = false;
						this.inGround = true;
						if (this.tileIn.getMaterial() != Material.air)
							this.tileIn.onEntityCollidedWithBlock(this.worldObj, blockPos, blockState, this);
					}
				}
			}
			if(this.isCritical && this.typePartical != null)
			{
				for(i = 0; i < 4; ++i)
					this.worldObj.spawnParticle(typePartical, this.posX + this.motionX * (double)i / 4.0D, this.posY + this.motionY * (double)i / 4.0D, this.posZ + this.motionZ * (double)i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
			}
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.rotationPitch = (float)(Math.atan2(this.motionY, (double)speed) * 180.0D / Math.PI);
			this.prevRotationPitch += MathHelper.floor_double((this.rotationPitch - this.prevRotationPitch + 180.0F) / 360.0F) * 360.0F;
			this.prevRotationYaw   += MathHelper.floor_double((this.rotationYaw   - this.prevRotationYaw   + 180.0F) / 360.0F) * 360.0F;
			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			speed = this.dragInAir;
			if(this.isInWater())
			{
				for(i = 0; i < 4; ++i)
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
				speed = this.dragInWater;
			}
			if(this.isWet())
				this.extinguish();
			this.motionX *= (double)speed;
			this.motionY *= (double)speed;
			this.motionZ *= (double)speed;
			this.motionY -= (double)this.gravity;
			this.setPosition(this.posX, this.posY, this.posZ);
			if(flag)
				this.doBlockCollisions();
		}
		if(this.firstUpdate)
		{
			this.firstUpdate = false;
			if(!this.worldObj.isRemote)
			{
				byte x = (byte)MathHelper.floor_double((this.posX - this.prevPosX) * 32.0D);
				byte y = (byte)MathHelper.floor_double((this.posY - this.prevPosY) * 32.0D);
				byte z = (byte)MathHelper.floor_double((this.posZ - this.prevPosZ) * 32.0D);
				if(Math.abs(x) >= 4 || Math.abs(y) >= 4 || Math.abs(z) >= 4)
					((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S14PacketEntity.S15PacketEntityRelMove(this.getEntityId(), x, y, z, this.onGround));
			}
		}
	}

	/**
     * @return
     *   0 when canceling hit and damage,
     *   1 when canceling damage only, 
     *   2 when canceling hit but causing damage, and 
     *   3 when canceling nothing.
     */
	protected abstract int onEntityHit(Entity entity, float damage);

	/**
	 * @return
	 *   true when hitting, and
	 *   false when canceling hit
	 */
	protected abstract boolean onBlockHit(BlockPos blockPos, Vec3 hitVec, EnumFacing sideHit);

}
