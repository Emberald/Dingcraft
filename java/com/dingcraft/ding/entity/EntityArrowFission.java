package simon.dingcraft.entity;

import simon.dingcraft.Dingcraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrowFission extends EntityArrowGeneral
{
	protected int fissionCnt = 6;
	
	protected ResourceLocation getTexture()
	{
		return new ResourceLocation("textures/entity/arrow.png");
	}
	
	public EntityArrowFission(World worldIn)
	{
		super(worldIn);
	}

	public EntityArrowFission(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	
	public EntityArrowFission(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn, shooter, target, velocity, inaccuracy);
	}
	
	public EntityArrowFission(World worldIn, EntityLivingBase shooter, float charge)
	{
		super(worldIn, shooter, charge);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setByte("fissionCnt", (byte)this.fissionCnt);
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompound)
	{
		super.readEntityFromNBT(tagCompound);
		this.fissionCnt = tagCompound.getByte("fissionCnt");
	}

	public void onUpdate()
	{
		if(!this.worldObj.isRemote && this.fissionCnt > 0 && this.ticksInAir % 5 == 4)
		{
			--this.fissionCnt;
			EntityArrowFission arrow = new EntityArrowFission(this.worldObj, this.posX, this.posY, this.posZ);
			float speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			arrow.shooter = this.shooter;
			arrow.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed, 2.0F);
			arrow.canBePickedUp = this.canBePickedUp;
			arrow.setFissionCnt(this.fissionCnt);
			arrow.isCritical = this.isCritical;
			this.worldObj.spawnEntityInWorld(arrow);
			this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed, 2.0F);
		}
		super.onUpdate();
	}
	
	public void setFissionCnt(int fissionCnt)
	{
		this.fissionCnt = fissionCnt;
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
		this.fissionCnt = 0;
		return true;
	}

}
