package com.dingcraft.ding;

import net.minecraft.creativetab.CreativeTabs;
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
import com.dingcraft.ding.entity.DingEntities;
import com.dingcraft.ding.entitylighting.EntityLighting;
import com.dingcraft.ding.eventhandler.EventHandlerBow;
import com.dingcraft.ding.eventhandler.EventHandlerPlayerDrops;
import com.dingcraft.ding.item.DingItems;
import com.dingcraft.ding.proxy.CommonProxy;
import com.dingcraft.ding.tileentity.DingTileEntities;

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
		DingBlocks.registerAll();
		DingItems.registerAll();
		DingEntities.registerAll();
		DingTileEntities.registerAll();
		DingItems.registerRecipes();
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
}
