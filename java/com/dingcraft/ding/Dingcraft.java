package simon.dingcraft;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import simon.dingcraft.block.BlockDing;
import simon.dingcraft.entity.EntityArrowFission;
import simon.dingcraft.entity.EntityArrowTorch;
import simon.dingcraft.entity.EntityArrowVoid;
import simon.dingcraft.item.ItemDing;
import simon.dingcraft.item.ItemWandDing;
import simon.dingcraft.network.CommonProxy;

@Mod(modid = Dingcraft.MODID, name = Dingcraft.MODNAME, version = Dingcraft.VERSION)
public class Dingcraft
{
	public static final String MODID = "ding";
	public static final String MODNAME = "Dingcraft";
	public static final String VERSION = "0.2.0";

	@SidedProxy(clientSide = "simon.dingcraft.network.ClientProxy", serverSide = "simon.dingcraft.network.CommonProxy")
	public static CommonProxy proxy;

	@Instance(Dingcraft.MODID)
	public static Dingcraft instance;

	public static BlockDing dingBlock = new BlockDing();
	public static ItemDing dingItem = new ItemDing();
	public static ItemWandDing dingWand = new ItemWandDing();

	public static DingEventHandler handler = new DingEventHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		//blocks
		GameRegistry.registerBlock(Dingcraft.dingBlock, BlockDing.name);
		//items
		GameRegistry.registerItem(Dingcraft.dingItem, ItemDing.name);
		GameRegistry.registerItem(Dingcraft.dingWand, ItemWandDing.name);    	
		//entity
		EntityRegistry.registerModEntity(EntityArrowFission.class, "FissionArrow", 1, Dingcraft.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityArrowVoid.class, "VoidArrow", 2, Dingcraft.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityArrowTorch.class, "TorchArrow", 3, Dingcraft.instance, 64, 10, true);
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