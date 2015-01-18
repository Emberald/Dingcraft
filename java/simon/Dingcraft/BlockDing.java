package simon.Dingcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockDing extends Block
{
	private final String name = "dingBlock";
	
	public BlockDing()
	{
		//从rock处继承材料属性
		super(Material.rock);
		//设置名称
		setUnlocalizedName(name);
		//设置创造模式的标签页
		setCreativeTab(CreativeTabs.tabBlock);
		//注册方块
		GameRegistry.registerBlock(this, name);
    	setHardness(2.0f);
    	setResistance(10.0f);
    	setLightLevel(1.0f);
    	setStepSound(Block.soundTypeStone);
    	setHarvestLevel("pickaxe", 0);
    	
	}
	
	public String getName()
	{
		return name;
	}
}