package simon.Dingcraft;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityArrowVoid extends Entity implements IProjectile
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInAir;
    private double damage = 2.0D;
    private int knockbackStrength;
    
    MovingObjectPosition keepermovingobjectposition;
    Entity shooter2;
    boolean flag = false;
    
    public EntityArrowVoid(World worldIn)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityArrowVoid(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
    }

    public EntityArrowVoid(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;

        this.posY = shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D;
        double d0 = p_i1755_3_.posX - shooter.posX;
        double d1 = p_i1755_3_.getEntityBoundingBox().minY + (double)(p_i1755_3_.height / 3.0F) - this.posY;
        double d2 = p_i1755_3_.posZ - shooter.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
            float f4 = (float)(d3 * 0.20000000298023224D);
            this.setThrowableHeading(d0, d1 + (double)f4, d2, p_i1755_4_, p_i1755_5_);
        }
    }

    public EntityArrowVoid(World worldIn, EntityLivingBase shooter, float speed)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;
        
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
    }
    
    public EntityArrowVoid(World worldIn, EntityLivingBase shooter, float speed, float yawDeviation)
    {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;
        
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
//        float radH = this.rotationYaw / 180.0F * (float)Math.PI;
//        float radV = this.rotationPitch / 180.0F * (float)Math.PI;
//        float radius = 1.5F;
//        this.posX -= (double)(MathHelper.sin(radH + 0.2618F)  * radius * MathHelper.cos(radV));
//        this.posY -= (double)(MathHelper.sin(radV) * radius) + 0.10000000149011612D;
//        this.posZ -= (double)(-MathHelper.cos(radH + 0.2618F) * radius * MathHelper.cos(radV));
        this.posX -= (double)(MathHelper.cos((this.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin((this.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (double)(-MathHelper.sin((this.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos((this.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double)f2;
        y /= (double)f2;
        z /= (double)f2;
        x += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        y += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        z += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        x *= (double)velocity;
        y *= (double)velocity;
        z *= (double)velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f3 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f3) * 180.0D / Math.PI);
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
        }
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

        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }


        ++this.ticksInAir;
        Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
        Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
        vec31 = new Vec3(this.posX, this.posY, this.posZ);
        vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;
        int i;
        float f1;

        for (i = 0; i < list.size(); ++i)
        {
            Entity entity1 = (Entity)list.get(i);

            if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5) && this.shootingEntity != null)
            {
            	f1 = 0.3F;
            	AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
            	MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

            	if (movingobjectposition1 != null)
            	{
            		double d1 = vec31.distanceTo(movingobjectposition1.hitVec);
            			
            		if (d1 < d0 || d0 == 0.0D)
            		{
            			entity = entity1;
            			d0 = d1;
            		}
            	}
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        float f2;
        float f3;
        float f4;
        
        if (movingobjectposition != null)
        {
            if (movingobjectposition.entityHit != null)
            {
                f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                int k = MathHelper.ceiling_double_int((double)f2 * this.damage);

                DamageSource damagesource;
                
                if (this.shootingEntity == null)
                {
                    damagesource = DamageSourceDing.causeVoidDamage((EntityArrowVoid) this,(Entity) this);
                }
                else
                {
                    damagesource = DamageSourceDing.causeVoidDamage((EntityArrowVoid) this,(Entity) this.shootingEntity);
                }

                if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
                {
                    movingobjectposition.entityHit.setFire(5);
                }

                if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float)k))
                {
                    if (movingobjectposition.entityHit instanceof EntityLivingBase)
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;

                        if (!this.worldObj.isRemote)
                        {
                            entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                        }

                        if (this.knockbackStrength > 0)
                        {
                            f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                            if (f4 > 0.0F)
                            {
                                movingobjectposition.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f4, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f4);
                            }
                        }

                        if (this.shootingEntity instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                            EnchantmentHelper.func_151385_b((EntityLivingBase)this.shootingEntity, entitylivingbase);
                        }

                        if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                        }
                    }

                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                    if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                        this.setDead();
                    }
                }
                else
                {
                    this.motionX *= -0.10000000149011612D;
                    this.motionY *= -0.10000000149011612D;
                    this.motionZ *= -0.10000000149011612D;
                    this.rotationYaw += 180.0F;
                    this.prevRotationYaw += 180.0F;
                    this.ticksInAir = 0;
                }
            }
        }
        
//      changes here
//      for (i = 0; i < 5; i++)
//      {
//      	this.worldObj.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, this.posX, this.posY, this.posZ, (double) rand.nextFloat(), (double) rand.nextFloat(), (double) rand.nextFloat(), new int[0]);
//      }
      
//      for (i = -2; i <= 2; ++i)
//      {
//      	for (int j = -2; j <= 2; ++j)
//      	{
//      		if (i > -2 && i < 2 && j == -1)
//      		{
//      			j = 2;
//      		}
//
//      		if (rand.nextInt(16) == 0)
//      		{
//      			for (int k = 0; k <= 1; ++k)
//      			{
//      				this.worldObj.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, this.posX, this.posY, this.posZ, (double)((float)i + rand.nextFloat()) - 0.5D, (double)((float)k - rand.nextFloat() - 1.0F), (double)((float)j + rand.nextFloat()) - 0.5D, new int[0]);
//      			}
//      		}
//      	}
//      }
        for (int var5 = 0; var5 < 3; ++var5)
        {
            double var8 = (double)((float)this.posY + rand.nextFloat());
            double var14 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            int var18 = rand.nextInt(2) * 2 - 1;

            double var6 = (double)this.posX + 0.5D + 0.25D * (double)var18;
            double var12 = (double)(rand.nextFloat() * 4.0F * (float)var18);

            double var10 = (double)this.posZ + 0.5D + 0.25D * (double)var18;
            double var16 = (double)(rand.nextFloat() * 4.0F * (float)var18);

            this.worldObj.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, var6, var8, var10, var12, var14, var16, new int[0]);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            
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
        f3 = 0.99F;
        f1 = 0.05F;

        if (this.isInWater())
        {
            for (int l = 0; l < 4; ++l)
            {
                f4 = 0.25F;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ, new int[0]);
            }

            f3 = 0.6F;
        }

        if (this.isWet())
        {
            this.extinguish();
        }

        this.motionX *= (double)f3;
        this.motionY *= (double)f3;
        this.motionZ *= (double)f3;
        this.motionY -= (double)f1;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.doBlockCollisions();
    
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte)this.inData);
        tagCompound.setByte("shake", (byte)this.arrowShake);
        tagCompound.setDouble("damage", this.damage);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");

        if (tagCompund.hasKey("inTile", 8))
        {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else
        {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        }

        this.inData = tagCompund.getByte("inData") & 255;
        this.arrowShake = tagCompund.getByte("shake") & 255;

        if (tagCompund.hasKey("damage", 99))
        {
            this.damage = tagCompund.getDouble("damage");
        }
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void setDamage(double p_70239_1_)
    {
        this.damage = p_70239_1_;
    }

    public double getDamage()
    {
        return this.damage;
    }

    public void setKnockbackStrength(int p_70240_1_)
    {
        this.knockbackStrength = p_70240_1_;
    }

    public boolean canAttackWithItem()
    {
        return false;
    }
}