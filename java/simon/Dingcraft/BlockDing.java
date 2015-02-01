package simon.Dingcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockDing extends Block
{
//	public static final Block.SoundType soundTypeDing = new Block.SoundType("ding", 0.5F, 1.2F);
	
	private final String name = "dingBlock";
	
	public BlockDing()
	{
		super(Material.rock);
	}
	
	public String getName()
	{
		return name;
	}
}