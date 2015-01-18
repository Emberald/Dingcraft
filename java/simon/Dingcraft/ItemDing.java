package simon.Dingcraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
 
public class ItemDing extends Item
{
	private final String name = "dingItem";

	public ItemDing()
	{
		//注册物品
		GameRegistry.registerItem(this, name);
		//设置名称
		setUnlocalizedName(name);
		//设置创造模式的标签页
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	public String getName()
	{
		return name;
	}
}