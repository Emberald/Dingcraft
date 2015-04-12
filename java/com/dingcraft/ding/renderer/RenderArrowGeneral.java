package com.dingcraft.ding.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.dingcraft.ding.entity.EntityArrowBase;

@SideOnly(Side.CLIENT)
public class RenderArrowGeneral extends Render
{
	/* This renderer is intended to do render for all subclasses of EntityArrowGeneral.
	 * If you want this renderer to do render for your subclass:
	 *  1. Register your subclass along with this renderer when the mod is initialized.
	 *     (e.g. RenderingRegistry.registerEntityRenderingHandler(<your class>, new RenderArrowGeneral(renderManager));
	 *  2. Implement method getTexture() which returns a proper resource location for the arrow texture.
	 * If you want to write another renderer for your subclass, it is not necessary to make your renderer a subclass of this renderer,
	 *   and in method getTexture() you can simply return null.
	 */
	public RenderArrowGeneral(RenderManager renderManager)
	{
		super(renderManager);
	}

	public void doRender(Entity entity, double x, double y, double z, float p_180551_8_, float partialTicks)
	{
		this.bindEntityTexture(entity);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		byte b0 = 0;
		float f2 = 0.0F;
		float f3 = 0.5F;
		float f4 = (float)(0 + b0 * 10) / 32.0F;
		float f5 = (float)(5 + b0 * 10) / 32.0F;
		float f6 = 0.0F;
		float f7 = 0.15625F;
		float f8 = (float)(5 + b0 * 10) / 32.0F;
		float f9 = (float)(10 + b0 * 10) / 32.0F;
		float f10 = 0.05625F;
		GlStateManager.enableRescaleNormal();
		float f11 = (float)((EntityArrowBase)entity).arrowShake - partialTicks;

		if (f11 > 0.0F)
		{
			float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
			GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
		}

		GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(f10, f10, f10);
		GlStateManager.translate(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f8);
		worldrenderer.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f8);
		worldrenderer.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f9);
		worldrenderer.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f9);
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f8);
		worldrenderer.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f8);
		worldrenderer.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f9);
		worldrenderer.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f9);
		tessellator.draw();

		for (int i = 0; i < 4; ++i)
		{
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			worldrenderer.startDrawingQuads();
			worldrenderer.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)f2, (double)f4);
			worldrenderer.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)f3, (double)f4);
			worldrenderer.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)f3, (double)f5);
			worldrenderer.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)f2, (double)f5);
			tessellator.draw();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, p_180551_8_, partialTicks);
	}

	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return ((EntityArrowBase)entity).arrowTextures;
	}

}