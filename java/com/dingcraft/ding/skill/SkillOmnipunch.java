package com.dingcraft.ding.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.dingcraft.ding.Dingcraft;
import com.dingcraft.ding.entity.EntityOmnipunch;

public class SkillOmnipunch extends Skill
{
	public SkillOmnipunch()
	{
		super();
		this.setUnlocalizedName("omnipunch");
		this.setCreativeTab(Dingcraft.tabSkills);
	}

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) 
	{
		if(!worldIn.isRemote)
		{
			// worldIn.spawnEntityInWorld(new EntityOmnipunch(worldIn, playerIn, 5));
		}
		return itemStackIn;
	}
}
