package simon.dingcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockDing extends Block
{
//	public static final Block.SoundType soundTypeDing = new Block.SoundType("ding", 0.5F, 1.2F);

	public static final String name = "dingBlock";

	public BlockDing()
	{
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(this.name);
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setLightLevel(0.3f);
		this.setHarvestLevel("pickaxe", 0);
//		this.setStepSound(BlockDing.soundTypeDing);
	}

//	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
//	{
//		if(!worldIn.isRemote && entityIn instanceof EntityArrowVoid)
//		{
//			EntityArrowVoid arrow = (EntityArrowVoid)entityIn;
//			worldIn.setBlockToAir(pos);
//			worldIn.createExplosion(arrow, arrow.posX, arrow.posY, arrow.posZ, 2.0F, true);
//			arrow.setDead();
//		}
//	}
}