package com.Dingcraft;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Dingcraft.MODID, name = Dingcraft.MODNAME, version = Dingcraft.VERSION)
public class Dingcraft
{
    public static final String MODID = "ding";
    public static final String MODNAME = "Dingcraft";
    public static final String VERSION = "0.2.0";
    
//    @SidedProxy(clientSide = MODID + ".ClientProxy", serverSide = MODID + ".CommonProxy")
//	public static CommonProxy proxy;
    
    public static BlockDing dingBlock;
    public static ItemDing dingItem;
    public static ItemWandDing dingWand;

    EventHandlerBow handler = new EventHandlerBow();
    
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
    	EntityRegistry.registerModEntity(EntityArrowVoid.class, "ArrowVoid", 1, this, 128, 3, true );
   }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {	
    	//Forge event handler registry
    	MinecraftForge.EVENT_BUS.register(handler);
    	//FML event handler registry
    	FMLCommonHandler.instance().bus().register(handler);
    	
    	//Renderers
    	// Model classes are all client-side only, so we must register them on the client side
    	if (event.getSide() == Side.CLIENT)
    	{
	    	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	    			
	    	//blocks' items
	    	renderItem.getItemModelMesher().register(Item.getItemFromBlock(dingBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + dingBlock.getName(), "inventory"));
	    			
	    	//items
	    	renderItem.getItemModelMesher().register(dingItem, 0, new ModelResourceLocation(MODID + ":" + dingItem.getName(), "inventory"));
	    	renderItem.getItemModelMesher().register(dingWand, 0, new ModelResourceLocation(MODID + ":" + dingWand.getName(), "inventory"));
	    			
	    	//entities
	    	RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
	    	RenderingRegistry.registerEntityRenderingHandler(EntityArrowVoid.class, new RenderArrowVoid(renderManager));
    	}
    }
}