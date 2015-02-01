package simon.Dingcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemWandDing extends Item
{
	private final String name = "dingWand";

	public ItemWandDing() {}
	
	@Override
	public boolean hitEntity(ItemStack par1ItemStack,EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		par1ItemStack.damageItem(1, par3EntityLivingBase);
		if (par3EntityLivingBase.worldObj.isRemote)
		{
		return true;
		}
		float Angle = (par3EntityLivingBase.rotationYaw/ 180F) * 3.141593F;
		float x = 0.2f * -MathHelper.sin(Angle);
		float y = 0.5f;
		float z = 0.2f * MathHelper.cos(Angle);
		par2EntityLivingBase.setVelocity(x, y, z);
		return true;
	}
	
	public String getName()
	{
		return name;
	}
}
