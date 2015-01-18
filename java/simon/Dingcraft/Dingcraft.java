package simon.Dingcraft;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Dingcraft.MODID, name = Dingcraft.MODNAME, version = Dingcraft.VERSION)
public class Dingcraft
{
    public static final String MODID = "ding";
    public static final String MODNAME = "Dingcraft";
    public static final String VERSION = "0.1.0";
    
    public static Block dingBlock;
    public static Item dingItem;
    
    DingcraftEventHandler handler = new DingcraftEventHandler();
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	//event handler registry
    	FMLCommonHandler.instance().bus().register(handler);
    	MinecraftForge.EVENT_BUS.register(handler);
    
    	//blocks
    	dingBlock = new BlockDing();
    	//items
    	dingItem = new ItemDing();
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {	
    	//recipes
    	GameRegistry.addRecipe(new ItemStack(Blocks.obsidian), new Object[]{
    	"AAA",
    	"AAA",
    	"AAA",
    	'A', Items.cookie
    	});
    	GameRegistry.addRecipe(new ItemStack(Items.apple, 4), new Object[]{
    	"AA",
    	"AA",
    	'A', Blocks.cobblestone
    	});
    	GameRegistry.addRecipe(new ItemStack(Items.dye, 2, 15), new Object[]{
    	"AB ",
    	"AAC",
    	"A  ",
    	'A', Items.cookie, 'B', Blocks.dirt, 'C', new ItemStack(Items.dye, 1, 1)
    	});
    
    	GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 1), new Object[]
    	{
    	new ItemStack(Items.dye, 1, 1), Items.redstone
    	});
    
    	GameRegistry.addSmelting(new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 11), 0.1F);
    	
    	//register renders
    	if(event.getSide() == Side.CLIENT)
    	{
    	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    	//blocks
    	renderItem.getItemModelMesher().register(Item.getItemFromBlock(dingBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ((BlockDing) dingBlock).getName(), "inventory"));
    	//items
    	renderItem.getItemModelMesher().register(dingItem, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ((ItemDing) dingItem).getName(), "inventory"));
    	}
    }
}