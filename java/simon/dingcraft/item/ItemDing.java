package simon.dingcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemDing extends Item
{
	public static final String name = "dingItem";
	public ItemDing()
	{
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setUnlocalizedName(this.name);
	}
}