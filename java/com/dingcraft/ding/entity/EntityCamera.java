package com.dingcraft.ding.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityCamera extends Entity
{
	private EntityPlayer player;
	private Entity target;
	
	private boolean hideGUI;
	private float fov;
	private int thirdPerson;
	
	private static EntityCamera instance;
	
	private static final double yOffset = 1.0D;
	
	public EntityCamera(World worldIn, EntityPlayer player, Entity target)
	{
		super(worldIn);
		this.player = player;
		this.target = target;
		this.setPosition(target.posX, target.posX, target.posZ);
		this.setRotation(target.rotationYaw, target.rotationPitch);
		this.motionX = target.motionX;
		this.motionY = target.motionY;
		this.motionZ = target.motionZ;
		this.setSize(0.0F, 0.0F);
	}

	protected void entityInit() {}

	protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

	protected void writeEntityToNBT(NBTTagCompound tagCompound) {}

	public boolean isEntityInvulnerable(DamageSource p)
	{
		return true;
	}
	
	public boolean canTriggerWalking()
	{
		return false;
	}
	
	public boolean canBeCollidedWith()
	{
		return false;
	}
	
	public boolean canBePushed()
	{
		return false;
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		if(!this.player.isSneaking() || this.player != Minecraft.getMinecraft().thePlayer || this.target.isDead)
		{
			stopCam();
			return;
		}
		this.motionX = this.target.motionX;
		this.motionY = this.target.motionY;
		this.motionZ = this.target.motionZ;
		
		double d = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double pitch = Math.atan2(-this.motionY, d) * 180.0D / Math.PI;
		double yaw = Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI - 90.0D;
		
		this.setRotation((float)yaw, (float)pitch);
		this.setPosition(this.target.posX, this.target.posY + yOffset, this.target.posZ);
//		System.out.printf("x=%f,y=%f,z=%f,x1=%f,y1=%f,z1=%f\n", this.posZ, this.posY, this.posZ, this.target.posX, this.target.posY, this.target.posZ);
	}
	
	public void stopCam()
	{
		this.worldObj.removeEntity(this);
		
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.gameSettings.hideGUI = this.hideGUI;
		mc.gameSettings.fovSetting = this.fov;
		mc.gameSettings.thirdPersonView = this.thirdPerson;
		
		mc.setRenderViewEntity(this.player);
		instance = null;
	}
	
	public static void startCam(EntityPlayer player, Entity target)
	{
		if(instance == null)
		{
			Minecraft mc = Minecraft.getMinecraft();
			World world = player.worldObj;
			
			instance = new EntityCamera(world, player, target);
			
			instance.hideGUI = mc.gameSettings.hideGUI;
			instance.fov = mc.gameSettings.fovSetting;
			instance.thirdPerson = mc.gameSettings.thirdPersonView;
			
			mc.gameSettings.hideGUI = true;
			mc.gameSettings.thirdPersonView = 1;
//			mc.gameSettings.fovSetting = 160;
			mc.setRenderViewEntity(instance);
			
			world.spawnEntityInWorld(instance);
		}
	}
}
