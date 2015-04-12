package com.dingcraft.ding.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;

public class BlockMeteor extends BlockFalling
{
	public static final String name = "meteorBlock";

	public BlockMeteor()
	{
		super(Material.anvil);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setLightOpacity(3);
        this.setHardness(50.0F);
        this.setResistance(2000.0F);
        this.setStepSound(soundTypePiston);
        this.setUnlocalizedName("meteor");
	}
	
	protected void onStartFalling(EntityFallingBlock fallingEntity)
    {
        fallingEntity.setHurtEntities(true);
    }
}
