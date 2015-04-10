package com.dingcraft.ding.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemWandDing extends Item
{
	public static final String name = "dingWand";

	public ItemWandDing()
	{
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setUnlocalizedName(this.name);
		this.setMaxDamage(30);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack,EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		par1ItemStack.damageItem(1, par3EntityLivingBase);
		if(par3EntityLivingBase.worldObj.isRemote)
			return true;
		float Angle = (par3EntityLivingBase.rotationYaw/ 180F) * 3.141593F;
		float x = 0.2f * -MathHelper.sin(Angle);
		float y = 3f;
		float z = 0.2f * MathHelper.cos(Angle);
		par2EntityLivingBase.setVelocity(x, y, z);
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,EntityPlayer par3EntityPlayer)
	{
		if(!par2World.isRemote)
		{
			EntityTNTPrimed entity = new EntityTNTPrimed(par2World, par3EntityPlayer.posX, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ, par3EntityPlayer);
			entity.fuse=1;
			float angle = (par3EntityPlayer.rotationYaw / 180.0F) * 3.141593F;
			float angle2 = (-par3EntityPlayer.rotationPitch/ 180.0F) * 3.141593F;
			final float speed = 2.0f;
			entity.motionY = speed * MathHelper.sin(angle2);
			entity.motionX = speed * MathHelper.cos(angle2) * -MathHelper.sin(angle);
			entity.motionZ = speed * MathHelper.cos(angle2) * MathHelper.cos(angle);
			par2World.spawnEntityInWorld(entity);
		}
		return par1ItemStack;
	}
}
