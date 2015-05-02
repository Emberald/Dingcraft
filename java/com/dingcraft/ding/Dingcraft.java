package com.dingcraft.ding;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.dingcraft.ding.block.DingBlocks;
import com.dingcraft.ding.entity.EntityArrowFission;
import com.dingcraft.ding.entity.EntityArrowSniper;
import com.dingcraft.ding.entity.EntityArrowTorch;
import com.dingcraft.ding.entity.EntityArrowVoid;
import com.dingcraft.ding.entitylighting.EntityLighting;
import com.dingcraft.ding.eventhandler.EventHandlerBow;
import com.dingcraft.ding.eventhandler.EventHandlerPlayerDrops;
import com.dingcraft.ding.item.DingItems;
import com.dingcraft.ding.proxy.CommonProxy;

@Mod(modid = Dingcraft.MODID, name = Dingcraft.MODNAME, version = Dingcraft.VERSION)
public class Dingcraft
{
	public static final String MODID = "ding";
	public static final String MODNAME = "Dingcraft";
	public static final String VERSION = "0.2.0";

	@SidedProxy(clientSide = "com.dingcraft.ding.proxy.ClientProxy", serverSide = "com.dingcraft.ding.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(Dingcraft.MODID)
	public static Dingcraft instance;
	
	public static final DingcraftEntity[] entities = new DingcraftEntity[] {
		new DingcraftEntity(EntityArrowFission.class, "FissionArrow"),
		new DingcraftEntity(EntityArrowVoid.class, "VoidArrow"),
		new DingcraftEntity(EntityArrowTorch.class, "TorchArrow"),
		new DingcraftEntity(EntityArrowSniper.class, "SniperArrow")
	};
	
	public static EventHandlerBow handlerBow = new EventHandlerBow();
	public static EventHandlerPlayerDrops handlerDrops = new EventHandlerPlayerDrops();
	@SideOnly(Side.CLIENT)
	public static EntityLighting entityLighting;

	public static final CreativeTabs tabSkills = new CreativeTabs("tabSkills") {
	    @SideOnly(Side.CLIENT)
	    public Item getTabIconItem() {
	        return Items.nether_star;
	    }
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		DingBlocks b;
		DingItems i;
		proxy.registerBlockAndItem();
		proxy.registerEntity();
		proxy.registerRecipe();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerHandler();
		proxy.registerRenderer();
	}
	
	@EventHandler
	public void stop(FMLServerStoppingEvent event)
	{
		DingItems.pocketWatch.resetTimeRate();
	}
	
	
	public static class DingcraftEntity
	{
		public final Class<? extends Entity> entityClass;
		public final String name;
		public final int trackingRange;
		public final int updateFrequency;
		public final boolean sendsVelocityUpdates;
		
		public DingcraftEntity(Class<? extends Entity> entityClass)
		{
			this(entityClass, getName(entityClass));
		}
		
		public DingcraftEntity(Class<? extends Entity> entityClass, String name)
		{
			this(entityClass, name, 64, 10, true);
		}

		public DingcraftEntity(Class<? extends Entity> entityClass, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
		{
			this.entityClass = entityClass;
			this.name = name;
			this.trackingRange = trackingRange;
			this.updateFrequency = updateFrequency;
			this.sendsVelocityUpdates = sendsVelocityUpdates;
		}
		
		static final String[] keyWords = {"EntityArrow", "Entity"};
		
		public static String getName(Class<? extends Entity> entityClass)
		{
			String name = entityClass.getSimpleName();
			for(String keyWord : keyWords)
			{
				if(name.contains(keyWord))
				{
					name = name.substring(name.indexOf(keyWord));
					break;
				}				
			}
			return name;
		}
	}
	
}
