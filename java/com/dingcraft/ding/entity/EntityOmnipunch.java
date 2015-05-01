package com.dingcraft.ding.entity;

import java.util.ArrayList;
import java.util.List;

import com.dingcraft.ding.Dingcraft;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityOmnipunch extends Entity
{
	public boolean inGround;
	public int ticksInAir;
	public int lifeTime;
	public int level;
	public float explosionRate;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;
	public double motionFactor;
	public Entity shootingEntity;

	public EntityOmnipunch(World worldIn)
	{
		super(worldIn);
		this.renderDistanceWeight = 1.0D;
		this.setSize(1.0F, 1.0F);
	}
	
	public EntityOmnipunch (World worldIn, EntityPlayer attacker, int level)
	{
		super(worldIn);
        this.renderDistanceWeight = 1.0D;//
        this.shootingEntity = attacker;
		this.ticksInAir = 0;
		this.motionFactor = 1.0F;
		this.setLevel(level);
		float attackerRotationYaw = (float) (attacker.rotationYaw + this.rand.nextGaussian());
		float attackerRotationPitch = (float) (attacker.rotationPitch+ this.rand.nextGaussian());
		this.setPositionAndRotation(attacker.posX, attacker.posY, attacker.posZ, attackerRotationYaw, attackerRotationPitch);
//		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
//		this.posY -= 0.1D;
//		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
//		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.accelerationX = this.motionX * 0.1D;
		this.accelerationY = this.motionY * 0.1D;
		this.accelerationZ = this.motionZ * 0.1D;
	}
	
	public void onUpdate()
	{		
//		if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isBlockLoaded(new BlockPos(this))))
		if ((this.shootingEntity != null && this.shootingEntity.isDead) || !this.worldObj.isBlockLoaded(new BlockPos(this)) || this.ticksInAir >= this.lifeTime)
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();

            if (this.inGround)
            {
                this.ticksInAir++;
                this.motionFactor = 0.6F;
            }
            if (this.inWater)
            {
                this.ticksInAir++;
                this.motionFactor = 0.8F;
            }
            this.ticksInAir++;

//            Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
//            Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
//            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
//            vec3 = new Vec3(this.posX, this.posY, this.posZ);
//            vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
//
//            if (movingobjectposition != null)
//            {
//                vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
//            }

            if(this.ticksInAir >= 10)
            {
            	List listEntity = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(3.0D, 1.0D, 1.0D));//
                for (int i = 0; i < listEntity.size(); i++)
                {
                	Entity entity = (Entity)listEntity.get(i);
                	if (entity.canBeCollidedWith() && !entity.isEntityEqual(this.shootingEntity))//25 in origin
                    {
//                        float f = 0.3F;
//                        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)f, (double)f, (double)f);
//                        MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
            			this.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 2.0F, false);
            			System.out.println("explode entity");
                    }
                }
                List listBlock = this.getBlocksWithinAABB(this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(3.0D, 1.0D, 1.0D));//
                BlockPos blockPos;
                for (int i = 0; i < listBlock.size(); i++)
                {
                	if(this.rand.nextFloat() < this.explosionRate)
        			{
                		blockPos = (BlockPos)listBlock.get(i);
            			this.worldObj.createExplosion(this, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0F, false);
            			System.out.println("explode block");
        			}
                }
                if(listBlock.size() > 0)
                {
                	this.inGround = true;
                }
                else
                {
                	this.inGround = false;
                }  
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;

            for (this.rotationPitch = (float)(Math.atan2((double)f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
            {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

            if (this.isInWater())
            {
            	float f3 = 0.25F;
                for (int j = 0; j < 4; ++j)
                {
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ, new int[0]);
                }

                this.motionFactor = 0.8F;
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= this.motionFactor;
            this.motionY *= this.motionFactor;
            this.motionZ *= this.motionFactor;
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        
	}

	protected void entityInit() {}

	protected void readEntityFromNBT(NBTTagCompound tagCompund) 
	{
        this.ticksInAir = tagCompund.getShort("inAir");
        this.level = tagCompund.getByte("level");
        this.setLevel(level);
	}

	protected void writeEntityToNBT(NBTTagCompound tagCompound) 
	{
        tagCompound.setShort("inAir", (short)this.ticksInAir);
        tagCompound.setByte("level", (byte)this.level);
	}
	
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }
    
    public List getBlocksWithinAABB(AxisAlignedBB punchAABB)
    {
        ArrayList arrayList = Lists.newArrayList();
        IBlockState iblockstate;
        BlockPos blockpos;
        Block block;
        boolean flag = this.isOutsideBorder();
        boolean flag1 = this.worldObj.isInsideBorder(this.worldObj.getWorldBorder(), this);
        if (flag && flag1)
        {
            this.setOutsideBorder(false);
        }
        else if (!flag && !flag1)
        {
            this.setOutsideBorder(true);
        }
        int i = MathHelper.floor_double(punchAABB.minX);
        int j = MathHelper.floor_double(punchAABB.maxX + 1.0D);
        int k = MathHelper.floor_double(punchAABB.minY);
        int l = MathHelper.floor_double(punchAABB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(punchAABB.minZ);
        int j1 = MathHelper.floor_double(punchAABB.maxZ + 1.0D);

        for (int k1 = i; k1 < j; ++k1)
        {
            for (int l1 = i1; l1 < j1; ++l1)
            {
                if (this.worldObj.isBlockLoaded(new BlockPos(k1, 64, l1)))
                {
                    for (int i2 = k - 1; i2 < l; ++i2)
                    {
                        blockpos = new BlockPos(k1, i2, l1);
                        if (!this.worldObj.getWorldBorder().contains(blockpos) && flag1)
                        {
                            iblockstate = Blocks.stone.getDefaultState();
                        }
                        else
                        {
                            iblockstate = this.worldObj.getBlockState(blockpos);
                        }
//                        block = iblockstate.getBlock();
//                        if(block.isCollidable())
                        if(iblockstate.getBlock().canCollideCheck(iblockstate, false))
                        {
//                        	block.addCollisionBoxesToList(this.worldObj, blockpos, iblockstate, punchAABB, arraylist, this);        
                        	arrayList.add(blockpos);
                        }
                    }
                }
            }
        }
        return arrayList;
    }
    
    protected void setLevel(int level)
    {
    	if(level > 100)
    	{
    		level = 100;
    	}
    	else if(level < 1)
    	{
    		level = 1;
    	}
    	
    	this.level = level;
    	this.lifeTime = (int)(Math.log(level) + 1) * 30;
    	this.explosionRate = (float)Math.log(level) * 0.02F + 0.01F;
    	this.setSize((float)(1.0 + Math.log(level)), (float)(1.0 + Math.log(level)));//TODO can this change the actual render size?
    }
}
