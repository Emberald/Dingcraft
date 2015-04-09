package simon.dingcraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import simon.dingcraft.Dingcraft;
import simon.dingcraft.block.BlockDing;
import simon.dingcraft.entity.EntityArrowFission;
import simon.dingcraft.entity.EntityArrowTorch;
import simon.dingcraft.entity.EntityArrowVoid;
import simon.dingcraft.item.ItemDing;
import simon.dingcraft.item.ItemWandDing;
import simon.dingcraft.renderer.RenderArrowGeneral;

public class ClientProxy extends CommonProxy
{
	public void register()
	{
		super.register();
		//renderers are client side only, so register here.
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//blocks' items
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Dingcraft.dingBlock), 0, new ModelResourceLocation(Dingcraft.MODID + ":" + BlockDing.name, "inventory"));
		//items
		renderItem.getItemModelMesher().register(Dingcraft.dingItem, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ItemDing.name, "inventory"));
		renderItem.getItemModelMesher().register(Dingcraft.dingWand, 0, new ModelResourceLocation(Dingcraft.MODID + ":" + ItemWandDing.name, "inventory"));
		//entities
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowFission.class, new RenderArrowGeneral(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowVoid.class, new RenderArrowGeneral(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowTorch.class, new RenderArrowGeneral(renderManager));
	}
}
