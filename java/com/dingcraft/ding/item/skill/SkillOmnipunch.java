package com.dingcraft.ding.item.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SkillOmnipunch extends Skill
{
	public static final String name = "skillOmnipunch";

	public SkillOmnipunch()
	{
		super();
		this.setUnlocalizedName(this.name);
	}

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return null;
	}
}
