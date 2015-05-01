package com.dingcraft.ding.skill;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkillTimeManipulation 
{
	private static Timer timer;
	public static final float RATE_MIN = 0.05F;
	public static final float RATE_MAX = 128.0F;
	
	public SkillTimeManipulation()
	{
		if(timer == null)
		{
			Minecraft theMinecraft = Minecraft.getMinecraft();
			Field field = null;
			try 
			{
				field = Minecraft.class.getDeclaredField("field_71428_T"); //obfuscated field //TODO automatically switch between these two
			} 
			catch (Exception e1) 
			{
				try
				{
					field = Minecraft.class.getDeclaredField("timer"); //deobfuscated field, encountered during development
				}
				catch (Exception e2) {}
			}
			field.setAccessible(true);
	        try {
				this.timer = (Timer)field.get(theMinecraft);
			} catch (Exception e) {}	
		}
	}
	
	public void setTimeRate(float timeRate)
    {
		if(this.timer != null)
		{
			if(timeRate >= RATE_MIN && timeRate <= RATE_MAX)
			{
				timer.timerSpeed = timeRate;
			}
		}
    }
	
}
