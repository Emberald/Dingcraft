package simon.Dingcraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class DamageSourceDing extends DamageSource
{
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;
    private boolean damageIsAbsolute;
    private float hungerDamage = 0.3F;
    private boolean fireDamage;
    private boolean projectile;
    private boolean difficultyScaled;
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;
    
	protected DamageSourceDing(String par1Str) 
	{
		super(par1Str);
	}
	
	public static DamageSource causeVoidDamage(EntityArrowVoid par0Entity, Entity par1Entity)
	{
		return (new EntityDamageSourceIndirect("void", par0Entity, par1Entity)).setDamageBypassesArmor().setDamageAllowedInCreativeMode().setProjectile();
	}
	 
	@Override
    public IChatComponent getDeathMessage(EntityLivingBase par1EntityLivingBase)
    {
        EntityLivingBase entitylivingbase1 = par1EntityLivingBase.func_94060_bK();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entitylivingbase1 != null && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {par1EntityLivingBase.getDisplayName(), entitylivingbase1.getDisplayName()}): new ChatComponentTranslation(s, new Object[] {par1EntityLivingBase.getDisplayName()});
    }
}