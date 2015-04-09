package simon.dingcraft.entity;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrowTorch extends EntityArrowGeneral
{
	protected int airPosX = 0;
	protected int airPosY = 0;
	protected int airPosZ = 0;
	
	
	protected ResourceLocation getTexture()
	{
		return new ResourceLocation("textures/entity/arrow.png");
	}
	
	public EntityArrowTorch(World worldIn)
	{
		super(worldIn);
	}

	public EntityArrowTorch(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	
	public EntityArrowTorch(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn, shooter, target, velocity, inaccuracy);
	}
	
	public EntityArrowTorch(World worldIn, EntityLivingBase shooter, float charge)
	{
		super(worldIn, shooter, charge);
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

	protected boolean onBlockHit(BlockPos blockPos, Vec3 hitVec, EnumFacing sideHit)
	{
		if(sideHit.equals(EnumFacing.DOWN) || this.worldObj.getBlockState(blockPos.offset(sideHit)).getBlock().equals(Blocks.torch))
			return true;
		IBlockState blockState = this.worldObj.getBlockState(blockPos);
		if((sideHit.equals(EnumFacing.UP) && blockState.getBlock().canPlaceTorchOnTop(this.worldObj, blockPos))
			|| (sideHit.getAxis().isHorizontal() && this.worldObj.isSideSolid(blockPos, sideHit, false)))
		{
			BlockTorch torch = (BlockTorch)Blocks.torch;
			IBlockState blockState1 = torch.onBlockPlaced(this.worldObj, blockPos.offset(sideHit), sideHit, (float)hitVec.xCoord, (float)hitVec.yCoord, (float)hitVec.zCoord, 0, (EntityLivingBase)null);
			this.worldObj.setBlockState(blockPos.offset(sideHit), blockState1, 3);
			this.setDead();
		}
		return true;
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		if(this.inWater)
		{
			EntityArrow arrow = new EntityArrow(this.worldObj, this.posX, this.posY, this.posZ);
			float speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			arrow.shootingEntity = this.shooter;
			arrow.motionX = this.motionX;
			arrow.motionY = this.motionY;
			arrow.motionZ = this.motionZ;
			arrow.canBePickedUp = this.canBePickedUp;
			if(!this.worldObj.isRemote)
				this.worldObj.spawnEntityInWorld(arrow);
			this.setDead();
			return;
		}
		float f = this.rand.nextFloat();
		if(f < 0.2F)
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + this.rand.nextDouble() * 0.2D, this.posY + this.rand.nextDouble() * 0.2D + 0.1D, this.posZ + this.rand.nextDouble() * 0.2D, 0.0D, 0.0D, 0.0D, new int[0]);
		else if(f < 0.4F)
			this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + this.rand.nextDouble() * 0.2D, this.posY + this.rand.nextDouble() * 0.2D, this.posZ + this.rand.nextDouble() * 0.2D, 0.0D, 0.01D, 0.0D, new int[0]);
	}
}
