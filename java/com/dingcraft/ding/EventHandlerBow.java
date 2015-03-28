package com.dingcraft.ding;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.dingcraft.ding.entity.EntityArrowVoid;

public class EventHandlerBow extends Item
{	
	public final int[] posMap = {0, 1, -1, 2, -2, 3, -3};
	
	@SubscribeEvent
	public void ArrowLooseEvent(ArrowLooseEvent event)
	{	
		int lvlFlame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, event.bow);
		int lvlInf = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, event.bow);

    	if (lvlFlame >= 2 || lvlInf >= 2)
    	{
			event.setCanceled(true);

			EntityPlayer player = event.entityPlayer;    		
    		boolean isArrowUnlimited = player.capabilities.isCreativeMode || lvlInf > 0;
    		if (isArrowUnlimited || player.inventory.hasItem(Items.arrow))
    		{	
        		World worldIn = player.worldObj;
        		int charge = event.charge;
        		int lvlPunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, event.bow);
        		int lvlPower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, event.bow);
        		
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
				
				switch (lvlFlame)
				{
				case 0:
				case 1:
					EntityArrow[] entityarrow = new EntityArrow[numArrow];
        			for (int i = 0; i < numArrow; i++)
        			{        			
        				entityarrow[i] = new EntityArrow(worldIn);
        				entityarrow[i].shootingEntity = player;

        		        if (player instanceof EntityPlayer)
        		        {
        		            entityarrow[i].canBePickedUp = 1;
        		        }
        		        
        		        entityarrow[i].setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
        				yawDeviation = player.rotationYaw + posMap[i] * (20 - 2 * numArrow);
        		        entityarrow[i].posX -= (double)(MathHelper.cos(yawDeviation / 180.0F * (float)Math.PI) * 0.16F);
        		        entityarrow[i].posY -= 0.10000000149011612D;
        		        entityarrow[i].posZ -= (double)(MathHelper.sin(yawDeviation / 180.0F * (float)Math.PI) * 0.16F);
        		        entityarrow[i].setPosition(entityarrow[i].posX, entityarrow[i].posY, entityarrow[i].posZ);
        		        entityarrow[i].motionX = (double)(-MathHelper.sin(yawDeviation / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrow[i].rotationPitch / 180.0F * (float)Math.PI));
        		        entityarrow[i].motionZ = (double)(MathHelper.cos(yawDeviation / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrow[i].rotationPitch / 180.0F * (float)Math.PI));
        		        entityarrow[i].motionY = (double)(-MathHelper.sin(entityarrow[i].rotationPitch / 180.0F * (float)Math.PI));
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
                        if (isArrowUnlimited)
                        {
                            entityarrow[i].canBePickedUp = 2;
                        }
                        else
                        {
                            player.inventory.consumeInventoryItem(Items.arrow);
                        }
            			if (!worldIn.isRemote)
            			{
            				worldIn.spawnEntityInWorld(entityarrow[i]);
            			}
        			}
        			break;
        			
				case 2:
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
                		event.bow.damageItem(5, player);
                		worldIn.playSoundAtEntity(player, Dingcraft.MODID + ":" + "bow", 0.3F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + modifierCharge * 0.25F + 0.2F);           			
                		if (!isArrowUnlimited)
                        {
                            player.inventory.consumeInventoryItem(Items.arrow);
                        }
                		if (!worldIn.isRemote)
            			{
            				worldIn.spawnEntityInWorld(entityarrowvoid[i]);
            			}
        			}	
        			break;
        			
				default:
					int numFission = lvlInf;
        			EntityArrowVoid entityarrowFission = new EntityArrowVoid(worldIn, player, speed, numFission);
        			entityarrowFission.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
                    entityarrowFission.posX -= (double)(MathHelper.cos(entityarrowFission.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
                    entityarrowFission.posY -= 0.10000000149011612D;
                    entityarrowFission.posZ -= (double)(MathHelper.sin(entityarrowFission.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
                    entityarrowFission.setPosition(entityarrowFission.posX, entityarrowFission.posY, entityarrowFission.posZ);
                    entityarrowFission.motionX = (double)(-MathHelper.sin(entityarrowFission.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrowFission.rotationPitch / 180.0F * (float)Math.PI));
                    entityarrowFission.motionZ = (double)(MathHelper.cos(entityarrowFission.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrowFission.rotationPitch / 180.0F * (float)Math.PI));
                    entityarrowFission.motionY = (double)(-MathHelper.sin(entityarrowFission.rotationPitch / 180.0F * (float)Math.PI));
                    entityarrowFission.setThrowableHeading(entityarrowFission.motionX, entityarrowFission.motionY, entityarrowFission.motionZ, speed * 1.5F, 1.0F);
                    
        			if (lvlPower > 0)
                	{
                		entityarrowFission.setDamage(entityarrowFission.getDamage() + (double) (lvlPower) * 0.24D + 0.2D);
            		}
            		if (lvlPunch > 0)
            		{
            			entityarrowFission.setKnockbackStrength((int) (0.5D * lvlPunch));
            		}
            		event.bow.damageItem(20, player);
            		worldIn.playSoundAtEntity(player, Dingcraft.MODID + ":" + "bow", 0.3F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + modifierCharge * 0.25F + 0.2F);           			
            		if (!isArrowUnlimited)
                    {
            			for (int i = 0; i < Math.pow(2, numFission + 1) - 1; i++)
            			{
                            player.inventory.consumeInventoryItem(Items.arrow);
            			}
                    }
            		if (!worldIn.isRemote)
        			{
        				worldIn.spawnEntityInWorld(entityarrowFission);
        			}
					break;
				}
       		}
    	}
	}
}	
