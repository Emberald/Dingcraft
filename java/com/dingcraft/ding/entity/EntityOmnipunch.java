package com.dingcraft.ding.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityOmnipunch extends Entity
{
	private int ticksInAir;
	private int level;
	public Entity attackingEntity;

	public EntityOmnipunch (World worldIn, EntityPlayer attacker, int level)
	{
		super(worldIn);
        this.renderDistanceWeight = 5.0D;
		this.ticksInAir = 0;
		this.level = level;
		
		this.setSize(2.0F, 2.0F);
		this.setPositionAndRotation(attacker.posX, attacker.posY, attacker.posZ, attacker.rotationYaw, attacker.rotationPitch, 1.0D);
	}
	
	public void setPositionAndRotation(double x, double y, double z, float playerYaw, float playerPitch, double inaccuracy)
    {
        this.prevPosX = this.posX = x;
        this.prevPosY = this.posY = y;
        this.prevPosZ = this.posZ = z;
        double boxWidth = this.width / 2.0F;
        double boxHeight = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - boxWidth, y, z - boxWidth, x + boxWidth, y + boxHeight, z + boxWidth));
        playerYaw += this.rand.nextGaussian() * inaccuracy;
        playerPitch += this.rand.nextGaussian() * inaccuracy;
        this.prevRotationYaw = this.rotationYaw = playerYaw;
        this.prevRotationPitch = this.rotationPitch = playerPitch;
    }
	
	public void onUpdate()
	{
		super.onUpdate();
		
		BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block.getMaterial() != Material.air)
        {
        	
        }
        
	}

	protected void entityInit() {}

	protected void readEntityFromNBT(NBTTagCompound tagCompund) 
	{
        this.ticksInAir = tagCompund.getShort("inAir");
        this.level = tagCompund.getByte("level");

	}

	protected void writeEntityToNBT(NBTTagCompound tagCompound) 
	{
        tagCompound.setShort("inAir", (short)this.ticksInAir);
        tagCompound.setByte("level", (byte)this.level);
	}
}
