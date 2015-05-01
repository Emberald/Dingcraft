package com.dingcraft.ding.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.dingcraft.ding.skill.SkillTimeManipulation;

public class ItemPocketWatch extends Item
{
	private double timeRate;
	private SkillTimeManipulation timer;
	public static final double RATE_MIN = 0.1;
	public static final double RATE_MAX = 8.0;
	public static final double RATE_STEP = 0.05;
	public int ticksAtRest;
	
	public static final ItemPocketWatch instance = new ItemPocketWatch();

	public ItemPocketWatch()
	{
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName("pocketWatch");
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		
		this.timeRate = 1.0F;
	}

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
    	if(worldIn.isRemote)
    	{	
    		if(this.timer == null)
    		{
    			this.timer = new SkillTimeManipulation();
    		}
    		
    		if(this.ticksAtRest <= 0)
    		{	
        		if(this.timeRate > RATE_MIN && this.timeRate < RATE_MAX || 
        		   this.timeRate >= RATE_MAX && playerIn.isSneaking() || 
        		   this.timeRate <= RATE_MIN && !playerIn.isSneaking())
        		{	        	
     				this.setTimeRate(this.timeRate + RATE_STEP * (playerIn.isSneaking() ? -1 : 1));

        			if(this.timeRate > 0.999 && this.timeRate < 1.001)
    				{    				
    					this.ticksAtRest = 12;
    					this.resetTimeRate();
    				}
        		}
    		}
        	else
    		{
    			this.ticksAtRest--;
    		}
    	}
    	return itemStackIn;
    }
	
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
    	damageStackItem(stack, 30, playerIn);
		this.resetTimeRate();
    	return false;
    }
    
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
    	if(target instanceof EntityPlayer)
    	{
    		//TODO do something, temporarily
    	}
    	damageStackItem(stack, 5, attacker);
		this.resetTimeRate();
        return false;
    }
    
    public void resetTimeRate()
    {
    	this.setTimeRate(1.0F);
    }
    
    public void setTimeRate(double timeRate)
    {
    	if(this.timer != null)
		{
    		this.timeRate = timeRate;
			timer.setTimeRate((float)timeRate);
		}
    }

    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
    {
		this.resetTimeRate();
    	return true;
    }
    
    public boolean isFull3D()
    {
        return true;
    }
    
//    @SideOnly(Side.CLIENT)
//    public int getColorFromItemStack(ItemStack stack, int renderLayer)
//    {
//    	switch (renderLayer) 
//    	{
//        	case 0: return Color.ORANGE.getRGB();
//        	case 1: return Color.BLUE.getRGB();
//        	default: return Color.BLACK.getRGB();
//      	}
//    }
    
//    @Override
//    public int getMetadata(int damage) 
//    {
//      return (int) ((this.timeRate >= 1.0) ? Math.round((this.timeRate - 1.0 / RATE_MAX) * 4) : Math.round(((1.0 - this.timeRate) / (1.0 - RATE_MIN)) * 4));
//    }

    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }
    
    //for fun
    public void damageStackItem(ItemStack stackIn, int damage, EntityLivingBase attacker)
    {
    	stackIn.damageItem(damage, attacker);
    	
    	if (!stackIn.hasTagCompound())
        {
    		stackIn.setTagCompound(new NBTTagCompound());
        }
    	stackIn.getTagCompound().setFloat("randDamage", this.itemRand.nextFloat());
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
    	double durability = 0.0;
    	NBTTagCompound tagCompound = stack.getTagCompound();
    	if(tagCompound != null)
    	{
    		durability = tagCompound.getFloat("randDamage");
    	}
        return durability;
    }
}
