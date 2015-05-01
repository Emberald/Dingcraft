package com.dingcraft.ding;

import java.util.ArrayList;

import net.minecraft.block.Block;
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

import com.dingcraft.ding.block.BlockDing;
import com.dingcraft.ding.block.BlockMeteor;
import com.dingcraft.ding.block.BlockPhoton;
import com.dingcraft.ding.entitylighting.EntityLighting;
import com.dingcraft.ding.eventhandler.EventHandlerBow;
import com.dingcraft.ding.eventhandler.EventHandlerPlayerDrops;
import com.dingcraft.ding.item.ItemDing;
import com.dingcraft.ding.item.ItemPocketWatch;
import com.dingcraft.ding.item.ItemWandDing;
import com.dingcraft.ding.proxy.CommonProxy;
import com.dingcraft.ding.skill.SkillOmnipunch;

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

	public static ArrayList<Block> listBlock = new ArrayList();
	public static BlockDing dingBlock = new BlockDing();
	public static BlockPhoton photonBlock = new BlockPhoton();
	public static BlockMeteor meteorBlock = new BlockMeteor();

	public static ArrayList<Item> listItem = new ArrayList();
	public static ItemDing dingItem = new ItemDing();
	public static ItemWandDing dingWand = new ItemWandDing();
	public static ItemPocketWatch pocketWatch = new ItemPocketWatch();
	public static SkillOmnipunch omnipunch = new SkillOmnipunch();
//	public static ItemFlashLight flashLight = new ItemFlashLight();

	public static EventHandlerBow handlerBow = new EventHandlerBow();
	public static EventHandlerPlayerDrops handlerDrops = new EventHandlerPlayerDrops();
	@SideOnly(Side.CLIENT)
	public static EntityLighting entityLighting;

	public static CreativeTabs tabSkills = new CreativeTabs("tabSkills") {
	    @SideOnly(Side.CLIENT)
	    public Item getTabIconItem() {
	        return Items.nether_star;
	    }
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		listBlock.add(dingBlock);
		listBlock.add(photonBlock);
//		listBlock.add(meteorBlock);
		
		listItem.add(dingItem);
		listItem.add(dingWand);
		listItem.add(pocketWatch);
//		listItem.add(omnipunch);
//		listItem.add(flashLight);
		
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
		this.pocketWatch.resetTimeRate();
	}

}
