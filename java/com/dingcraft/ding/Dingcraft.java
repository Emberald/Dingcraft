package com.dingcraft.ding;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.dingcraft.ding.block.BlockDing;
import com.dingcraft.ding.block.BlockMeteor;
import com.dingcraft.ding.block.BlockPhoton;
import com.dingcraft.ding.entity.EntityArrowFission;
import com.dingcraft.ding.entity.EntityArrowTorch;
import com.dingcraft.ding.entity.EntityArrowVoid;
import com.dingcraft.ding.entity.EntityOmnipunch;
import com.dingcraft.ding.item.ItemDing;
import com.dingcraft.ding.item.ItemWandDing;
import com.dingcraft.ding.item.skill.SkillOmnipunch;
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

	public static BlockDing dingBlock = new BlockDing();
	public static BlockPhoton photonBlock = new BlockPhoton();
	public static BlockMeteor meteorBlock = new BlockMeteor();

	public static ItemDing dingItem = new ItemDing();
	public static ItemWandDing dingWand = new ItemWandDing();
	public static SkillOmnipunch omnipunch = new SkillOmnipunch();

	public static EventHandlerBow handler = new EventHandlerBow();

	public static CreativeTabs tabSkills = new CreativeTabs("tabSkills") {
	    @SideOnly(Side.CLIENT)
	    public Item getTabIconItem() {
	        return Items.nether_star;
	    }
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		//blocks
		GameRegistry.registerBlock(Dingcraft.dingBlock, BlockDing.name);
		GameRegistry.registerBlock(Dingcraft.photonBlock, BlockPhoton.name);
//		GameRegistry.registerBlock(Dingcraft.meteorBlock, BlockMeteor.name);
		//items
		GameRegistry.registerItem(Dingcraft.dingItem, ItemDing.name);
		GameRegistry.registerItem(Dingcraft.dingWand, ItemWandDing.name);    	
//		GameRegistry.registerItem(Dingcraft.omnipunch, SkillOmnipunch.name);    	
		//entity
		EntityRegistry.registerModEntity(EntityArrowFission.class, "FissionArrow", 1, Dingcraft.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityArrowVoid.class, "VoidArrow", 2, Dingcraft.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityArrowTorch.class, "TorchArrow", 3, Dingcraft.instance, 64, 10, true);
//		EntityRegistry.registerModEntity(EntityOmnipunch.class, "Omnipunch", 4, Dingcraft.instance, 64, 10, true);
		//tile entity
		GameRegistry.registerTileEntity(com.dingcraft.ding.tileentity.TileEntityPhoton.class, "PhotonTileEntity");
		//craft and smelt
		GameRegistry.addRecipe(new ItemStack(Dingcraft.dingBlock),"AAA","AAA","AAA",'A',new ItemStack(Dingcraft.dingItem));
		GameRegistry.addRecipe(new ItemStack(Dingcraft.dingItem,9),"A",'A',Dingcraft.dingBlock);
		GameRegistry.addSmelting(new ItemStack(Items.gold_ingot),new ItemStack(Dingcraft.dingItem), 4F);
		GameRegistry.addRecipe(new ItemStack(Dingcraft.dingWand),"A","B","B",'A',new ItemStack(Dingcraft.dingItem),'B',Items.stick);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.register();
	}
	
}