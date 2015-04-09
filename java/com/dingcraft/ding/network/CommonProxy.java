package simon.dingcraft.network;

import simon.dingcraft.Dingcraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy
{
	public void register()
	{
		//Forge event handler registry
		MinecraftForge.EVENT_BUS.register(Dingcraft.handler);
		//FML event handler registry
		FMLCommonHandler.instance().bus().register(Dingcraft.handler);
	}
}
