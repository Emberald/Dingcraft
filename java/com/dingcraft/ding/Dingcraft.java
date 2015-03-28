package com.dingcraft.ding;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.dingcraft.ding.block.BlockDing;
import com.dingcraft.ding.entity.EntityArrowVoid;
import com.dingcraft.ding.item.ItemDing;
import com.dingcraft.ding.item.ItemWandDing;
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
    
    public static BlockDing dingBlock;
    public static ItemDing dingItem;
    public static ItemWandDing dingWand;

    EventHandlerBow handlerBow = new EventHandlerBow();
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	//blocks
    	dingBlock = new BlockDing();
		GameRegistry.registerBlock(dingBlock.setCreativeTab(CreativeTabs.tabBlock).setUnlocalizedName(dingBlock.getName()).setHardness(2.0f).setResistance(10.0f).setLightLevel(0.3f)/*.setStepSound(BlockDing.soundTypeDing)*/, dingBlock.getName());
		dingBlock.setHarvestLevel("pickaxe", 0);
    	
    	//items
		dingItem = new ItemDing();
		GameRegistry.registerItem(dingItem.setCreativeTab(CreativeTabs.tabMaterials).setUnlocalizedName(dingItem.getName()), dingItem.getName());
		dingWand = new ItemWandDing();
		GameRegistry.registerItem(dingWand.setCreativeTab(CreativeTabs.tabCombat).setUnlocalizedName(dingWand.getName()).setMaxDamage(30).setMaxStackSize(1), dingWand.getName());    	
    	
		//entity
    	EntityRegistry.registerModEntity(EntityArrowVoid.class, "ArrowVoid", 1, this, 128, 3, true);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {	
		//Forge event handler registry
    	MinecraftForge.EVENT_BUS.register(handlerBow);
    	//FML event handler registry
    	FMLCommonHandler.instance().bus().register(handlerBow);
    	
    	//Renderers
    	// Model classes are all client-side only, so we must register them on the client side
    	proxy.registerRenderers();
    }
}