package simon.dingcraft;

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
	@SubscribeEvent
	public void ArrowLooseEvent(ArrowLooseEvent event)
	{	
		ItemStack bow = event.bow;
		int lvlFlame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bow);

    	if (lvlFlame > 1)
    	{
			event.setCanceled(true);

    		int lvlInf = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, bow);
    		int lvlPunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, bow);
    		int lvlPower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, bow);

    		EntityPlayer player = event.entityPlayer;
    		World worldIn = player.worldObj;
    		int charge = event.charge;
    		
    		boolean flagInfArrow = player.capabilities.isCreativeMode || lvlInf > 0;
    		
    		if (flagInfArrow || player.inventory.hasItem(Items.arrow))
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
    			
    			EntityArrowVoid entityarrowvoid = new EntityArrowVoid(worldIn, player, modifierCharge * 2.0F);    					
    			
                if (lvlPower > 0)
            	{
                	entityarrowvoid.setDamage(entityarrowvoid.getDamage() + (double) (lvlPower) * 0.24D + 0.2D);
            	}
                
        		if (lvlPunch > 0)
        		{
        			entityarrowvoid.setKnockbackStrength((int) (MathHelper.sqrt_float((float) (2 * lvlPunch))));
        		}
	            	
    			worldIn.playSoundAtEntity(player,(Dingcraft.MODID + ":" + "bow"), 0.3F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + modifierCharge * 0.25F + 0.2F);
    			
    			bow.damageItem(1, player);
    			
    			
    			if (!worldIn.isRemote)
    			{
    				worldIn.spawnEntityInWorld(entityarrowvoid);
    			}
    			
    			if (lvlInf == 2)
    			{
    				EntityArrowVoid entityarrowvoid2 = new EntityArrowVoid(worldIn, player, modifierCharge * 2.0F, -1, 1);    					
    				EntityArrowVoid entityarrowvoid3 = new EntityArrowVoid(worldIn, player, modifierCharge * 2.0F, -1, 2);    					
    				EntityArrowVoid entityarrowvoid4 = new EntityArrowVoid(worldIn, player, modifierCharge * 2.0F, 1, 1);    					
    				EntityArrowVoid entityarrowvoid5 = new EntityArrowVoid(worldIn, player, modifierCharge * 2.0F, 1, 2); 
    				
    				entityarrowvoid2.setDamage(entityarrowvoid.getDamage());
    				entityarrowvoid3.setDamage(entityarrowvoid.getDamage());
    				entityarrowvoid4.setDamage(entityarrowvoid.getDamage());
    				entityarrowvoid5.setDamage(entityarrowvoid.getDamage());
    				
        			if (!worldIn.isRemote)
        			{
        				worldIn.spawnEntityInWorld(entityarrowvoid2);
        				worldIn.spawnEntityInWorld(entityarrowvoid3);
        				worldIn.spawnEntityInWorld(entityarrowvoid4);
        				worldIn.spawnEntityInWorld(entityarrowvoid5);
        			}
    			}
       		}
    	}
	}
}	
