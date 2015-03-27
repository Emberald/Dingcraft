package com.Dingcraft;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerBow extends Item
{	
	private int[] posMap = {0, 1, -1, 2, -2, 3, -3};
	
	@SubscribeEvent
	public void ArrowLooseEvent(ArrowLooseEvent event)
	{	
		int lvlFlame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, event.bow);
		int lvlInf = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, event.bow);

    	if (lvlFlame >= 2 || lvlInf >= 2)
    	{
			event.setCanceled(true);

    		EntityPlayer player = event.entityPlayer;
    		World worldIn = player.worldObj;
    		int charge = event.charge;
    		int lvlPunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, event.bow);
    		int lvlPower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, event.bow);
    		
    		if (player.capabilities.isCreativeMode || lvlInf > 0 || player.inventory.hasItem(Items.arrow))
    		{	
    			float modifierCharge = charge / 20.0F;
    			modifierCharge = (modifierCharge * modifierCharge + modifierCharge * 2.0F) / 3.0F;	
    			if ((double) modifierCharge < 0.1D)
    			{
    				return;
    			}
    			if (modifierCharge > 1.0F)
    			{
    				modifierCharge = 1.0F;
    			}
    			float speed = modifierCharge * 2.0F;
    			int numArrow = 2 * lvlInf - 3;
    			if (numArrow < 1) 
    			{
    				numArrow = 1;
    			}
    			if (numArrow > 7) 
    			{
    				numArrow = 7;
    			}
				float yawDeviation;
    			if (lvlFlame >= 2)
    			{
        			EntityArrowVoid[] entityarrowvoid = new EntityArrowVoid[numArrow];
        			for (int i = 0; i < numArrow; i++)
        			{
        				yawDeviation = posMap[i] * (20 - 2 * numArrow);
        				entityarrowvoid[i] = new EntityArrowVoid(worldIn, player, speed, yawDeviation);
            			if (lvlPower > 0)
                    	{
                    		entityarrowvoid[i].setDamage(entityarrowvoid[i].getDamage() + (double) (lvlPower) * 0.24D + 0.2D);
                		}
                		if (lvlPunch > 0)
                		{
                			entityarrowvoid[i].setKnockbackStrength((int) (0.5D * lvlPunch));
                		}
                		event.bow.damageItem(15, player);
                		worldIn.playSoundAtEntity(player, Dingcraft.MODID + ":" + "bow", 0.3F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + modifierCharge * 0.25F + 0.2F);           			
            			if (!worldIn.isRemote)
            			{
            				worldIn.spawnEntityInWorld(entityarrowvoid[i]);
            			}
        			}	
    			}
    			else
    			{
        			EntityArrow[] entityarrow = new EntityArrow[numArrow];
        			for (int i = 0; i < numArrow; i++)
        			{        			
        				entityarrow[i] = new EntityArrow(worldIn, player, speed);
        				yawDeviation = posMap[i] * (20 - 2 * numArrow);
        				entityarrow[i].posX += (double)(MathHelper.cos(player.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        		        entityarrow[i].posZ += (double)(MathHelper.sin(player.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        		        entityarrow[i].posX -= (double)(MathHelper.cos((player.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * 0.16F);
        		        entityarrow[i].posZ -= (double)(MathHelper.sin((player.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * 0.16F);
        		        entityarrow[i].setPosition(entityarrow[i].posX, entityarrow[i].posY, entityarrow[i].posZ);
        		        entityarrow[i].motionX = (double)(-MathHelper.sin((player.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrow[i].rotationPitch / 180.0F * (float)Math.PI));
        		        entityarrow[i].motionZ = (double)(MathHelper.cos((player.rotationYaw + yawDeviation) / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrow[i].rotationPitch / 180.0F * (float)Math.PI));
        		        entityarrow[i].setThrowableHeading(entityarrow[i].motionX, entityarrow[i].motionY, entityarrow[i].motionZ, speed * 1.5F, 1.0F);
        				
        				if (modifierCharge == 1.0F)
        	            {
        	                entityarrow[i].setIsCritical(true);
        	            }
        				if (lvlPower > 0)
                    	{
                    		entityarrow[i].setDamage(entityarrow[i].getDamage() + (double) (lvlPower) * 0.5D + 0.5D);
                		}
                		if (lvlPunch > 0)
                		{
                			entityarrow[i].setKnockbackStrength(lvlPunch);
                		}
                		if (lvlFlame > 0)
                        {
                			entityarrow[i].setFire(100);
                        }
                		event.bow.damageItem(1, player);
                        worldIn.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + modifierCharge * 0.5F);
                        entityarrow[i].canBePickedUp = 2;
            			if (!worldIn.isRemote)
            			{
            				worldIn.spawnEntityInWorld(entityarrow[i]);
            			}
        			}
    			}
       		}
    	}
	}
}	
