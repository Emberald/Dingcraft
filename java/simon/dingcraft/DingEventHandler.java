package simon.dingcraft;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import simon.dingcraft.entity.EntityArrowFission;

public class DingEventHandler
{	
	private static Random Rnd = new Random();
	@SubscribeEvent
	public void ArrowFire(ArrowLooseEvent event)
	{
		int lvlFlame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, event.bow);
		if(lvlFlame == 2)
		{
			event.setCanceled(true);
			boolean flag = event.entityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, event.bow) > 0;
			if (!(flag || event.entityPlayer.inventory.hasItem(Items.arrow)))
				return;
			float charge = event.charge / 20.0F;
			charge = charge * (charge + 2.0F) / 1.5F;
			if(charge < 0.2F)
				return;
			if(charge > 2.0F)
				charge = 2.0F;
			World world = event.entityPlayer.worldObj;
			EntityArrowFission arrow = new EntityArrowFission(world, event.entityPlayer, charge);
			if(charge == 2.0F)
				arrow.isCritical = true;
			int lvlPower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, event.bow);
			int lvlPunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, event.bow);
			if(lvlPower > 0)
                arrow.setDamage(arrow.getDamage() + (double)lvlPower * 0.5D + 0.5D);
			if(lvlPunch > 0)
	            arrow.setKnockbackStrength(lvlPunch);
			event.bow.damageItem(1, event.entityPlayer);
			world.playSoundAtEntity(event.entityPlayer, "random.bow", 1.0F, 1.0F / (Rnd.nextFloat() * 0.4F + 1.2F) + charge * 0.25F);
			if(flag)
				arrow.canBePickedUp = 2;
			else
				event.entityPlayer.inventory.consumeInventoryItem(Items.arrow);
			if(!world.isRemote)
				world.spawnEntityInWorld(arrow);
		}
		
		//		int lvlInf = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, event.bow);
//		int i;
//		float offsetX,offsetZ,offsetY;
//		double damage;
//		int knockback;
//		if(lvlInf <= 1 && lvlFlame <= 1)return;
//		EntityPlayer player = event.entityPlayer;
//		World worldIn = player.worldObj;
//		if (!(player.capabilities.isCreativeMode || lvlInf > 0 || player.inventory.hasItem(Items.arrow)))
//			return;
//		event.setCanceled(true);
//		boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, event.bow) > 0;
//		float charge = event.charge / 20.0F;
//		int lvlPunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, event.bow);
//		int lvlPower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, event.bow);
//		charge = charge * (charge + 2.0F) / 3.0F;	
//		if ((double) charge < 0.1D) return;
//		if (charge > 1.0F) charge = 1.0F;
//		charge *= 2.0F;
//		
//		{
//			EntityArrow arrow = new EntityArrow(worldIn, player, charge);
//			damage = arrow.getDamage() + (double)lvlPower * 0.24D + 0.2D;
//			knockback = (int) (MathHelper.sqrt_float((float) (2 * lvlPunch)));
//		}
//
//		int fireCount = 2*lvlInf - 1;
//		if(lvlInf==0)
//		{
//			if(!flag)
//				player.inventory.consumeInventoryItem(Items.arrow);
//			fireCount=1;
//		}
//		else if(lvlFlame == 3 || lvlFlame == 5)
//		{
//			fireCount = 1;
//		}
//    	event.bow.damageItem(fireCount, player);
//    	worldIn.playSoundAtEntity(player, Dingcraft.MODID + ":" + "bow", 0.3F, 1.0F / (Rnd.nextFloat() * 0.4F + 1.2F) + charge / 8.0F + 0.2F);
//    	
//		offsetX = - MathHelper.sin((player.rotationYaw + 90) / 180.0F * (float)Math.PI) * 0.2F;
//		offsetZ = MathHelper.cos((player.rotationYaw + 90) / 180.0F * (float)Math.PI) * 0.2F;
//
//    	
//		if (!worldIn.isRemote)
//		{
//			if(fireCount == 1)
//			{
//				if(lvlFlame >= 2)
//				{
//					EntityArrowVoid arrow = new EntityArrowVoid(worldIn, player, charge);
//					if (lvlPower > 0)
//		        		arrow.setDamage(damage);
//		    		if (lvlPunch > 0)
//		    			arrow.setKnockbackStrength(knockback);
//		    		if(flag)
//		    			arrow.canBePickedUp = 2;
//		    		if(lvlFlame == 3)
//		    			arrow.SetEnchLvl((short)lvlInf);
//		    		else if(lvlFlame == 4)
//		    			arrow.SetExplosive();
//		    		else if(lvlFlame == 5)
//		    		{
//		    			arrow.SetEnchLvl((short)lvlInf);
//		    			arrow.SetExplosive();
//		    		}
//		    		worldIn.spawnEntityInWorld(arrow);
//				}
//				else
//				{
//					EntityArrow arrow = new EntityArrow(worldIn, player, charge);
//					if (lvlPower > 0)
//		        		arrow.setDamage(damage);
//		    		if (lvlPunch > 0)
//		    			arrow.setKnockbackStrength(knockback);
//		    		if (lvlFlame == 1)
//		    			arrow.setFire(100);
//		    		if(flag)
//		    			arrow.canBePickedUp = 2;
//		    		worldIn.spawnEntityInWorld(arrow);
//				}
//				return;
//			}
//			for(i=0; i<fireCount; i++)
//			{
//				offsetY = 0.6F * (i % 2) - 0.3F;
//				if(lvlFlame >= 2)
//				{
//					EntityArrowVoid arrow = new EntityArrowVoid(worldIn, player, charge);
//					if (lvlPower > 0)
//		        		arrow.setDamage(damage);
//		    		if (lvlPunch > 0)
//		    			arrow.setKnockbackStrength(knockback);
//		    		if(flag)
//		    			arrow.canBePickedUp = 2;
//		    		if(lvlFlame == 4)
//		    			arrow.SetExplosive();
//		    		arrow.setPosition(arrow.posX + offsetX * (lvlInf - i - 1), arrow.posY + offsetY, arrow.posZ + offsetZ * (lvlInf - i));
//		    		worldIn.spawnEntityInWorld(arrow);
//				}
//				else
//				{
//					EntityArrow arrow = new EntityArrow(worldIn, player, charge);
//					if (lvlPower > 0)
//		        		arrow.setDamage(damage);
//		    		if (lvlPunch > 0)
//		    			arrow.setKnockbackStrength(knockback);
//		    		if (lvlFlame == 1)
//		    			arrow.setFire(100);
//		    		if(flag)
//		    			arrow.canBePickedUp = 2;
//		    		arrow.setPosition(arrow.posX + offsetX * (lvlInf - i - 1), arrow.posY + offsetY, arrow.posZ + offsetZ * (lvlInf - i));
//		    		worldIn.spawnEntityInWorld(arrow);
//				}
//			}
//		}
	}
	
}	
