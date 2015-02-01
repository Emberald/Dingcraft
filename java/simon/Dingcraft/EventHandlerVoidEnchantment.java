package simon.Dingcraft;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerVoidEnchantment extends Item
{
	@SubscribeEvent
	public void ArrowLooseEvent(ArrowLooseEvent event)
	{	
		ItemStack bow = event.bow;
		
		int lvlInf = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, bow);
    	if (lvlInf > 1)
    	{
    		event.setCanceled(true);

    		EntityPlayer player = event.entityPlayer;
    		World worldIn = player.worldObj;
    		int charge = event.charge;
    		
    		boolean flagInfArrow = player.capabilities.isCreativeMode || lvlInf > 0;    		
    		
    		if (flagInfArrow || player.inventory.hasItem(Items.arrow))
    		{	
    			float modifierCharge = charge / 20.0F;
    			modifierCharge = (modifierCharge * modifierCharge + modifierCharge * 2.0F) / 3.0F + 0.05F;	
    			if ((double) modifierCharge < 0.1D)
    			{
    				return;
    			}
    			if (modifierCharge > 1.0F)
    			{
    				modifierCharge = 1.0F;
    			}
	                
    			EntityArrowVoid entityarrowvoid = new EntityArrowVoid(worldIn, player, modifierCharge * 2.0F);
    			
    			int lvlPower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, bow);
                if (lvlPower > 0)
            	{
            		entityarrowvoid.setDamage(entityarrowvoid.getDamage() + (double) (lvlPower) * 0.24D + 0.2D);
            	}
                
    			int lvlPunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, bow);
        		if (lvlPunch > 0)
        		{
        			entityarrowvoid.setKnockbackStrength((int) (MathHelper.sqrt_float((float) (2 * lvlPunch))));
        		}

        		int lvlFlame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bow);
    			if (lvlFlame > 0)
    			{
    				entityarrowvoid.setFire(20);
    			}
	            	
    			worldIn.playSoundAtEntity(player,(Dingcraft.MODID + ":" + "bow"), 0.3F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + modifierCharge * 0.25F + 0.2F);
    			
    			bow.damageItem(1, player);
    			
    			if (!worldIn.isRemote)
    			{
    				worldIn.spawnEntityInWorld(entityarrowvoid);
    			}
    			
       		}
    	}
	}
}	
