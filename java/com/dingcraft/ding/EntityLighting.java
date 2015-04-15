package com.dingcraft.ding;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import com.dingcraft.ding.entity.EntityArrowTorch;
import com.google.common.collect.Lists;

public class EntityLighting
{
	protected List trackedEntity;
	protected int[] lightUpdateBlockList;
	
	private final EnumFacing[] enumFacingAry = EnumFacing.values();
	private final int facingCnt = enumFacingAry.length;
	
	public EntityLighting()
	{
		this.lightUpdateBlockList = new int[32768];
		this.trackedEntity = Lists.newArrayList();
	}
	
	@SubscribeEvent
	public void onEntityAdded(EntityJoinWorldEvent event)
	{
		//Only for testing. Entity registry will be implemented later.
		

		if(event.entity instanceof EntityArrowTorch)
		{
			trackedEntity.add(new LightTrackedEntity(event.entity, 14));
			System.out.println("Entity added.");
		}
	}
	
	@SubscribeEvent
	public void tick(ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END) return;
		int i = 0;
		LightTrackedEntity lightEntity = null;
		//remove dead entities
		while(i < trackedEntity.size())
		{
			lightEntity = (LightTrackedEntity)trackedEntity.get(i);
			if(lightEntity.entity.isDead)
			{
				trackedEntity.remove(i);
				this.checkLight(lightEntity.entity.worldObj, lightEntity.lightBlockPos);
			}
			else
				i++;
		}
		//update lighted blocks
		int size = trackedEntity.size();
		for(i = 0; i < size; i++)
		{
			lightEntity = (LightTrackedEntity)trackedEntity.get(i);
			BlockPos newPos = lightEntity.getCurrBlockPos();
			if(!newPos.equals(lightEntity.lightBlockPos))
			{
				BlockPos oldPos = lightEntity.lightBlockPos;
				lightEntity.lightBlockPos = newPos;
				this.checkLight(lightEntity.entity.worldObj, lightEntity.lightBlockPos);
				this.checkLight(lightEntity.entity.worldObj, oldPos);
			}
		}
	}
	
	private boolean checkNeighborLight(World worldIn, BlockPos pos)
	{
		int lightLvlCurr = worldIn.getLightFor(EnumSkyBlock.BLOCK, pos);
		int lightLvlNew = 0;
		for(int i = 0; i < facingCnt; i++)
			lightLvlNew = Math.max(lightLvlNew, worldIn.getLightFor(EnumSkyBlock.BLOCK, pos.offset(enumFacingAry[i])));
		lightLvlNew -= Math.max(1, worldIn.getBlockState(pos).getBlock().getLightOpacity());
		lightLvlNew = Math.max(lightLvlNew, this.getBlockLightEmitLvl(worldIn, pos));
		if(lightLvlNew != lightLvlCurr)
		{
			worldIn.setLightFor(EnumSkyBlock.BLOCK, pos, lightLvlNew);
			return true;
		}
		else
			return false;
	}
		
	private boolean inRange(BlockPos pos, BlockPos reference)
	{
		return Math.abs(pos.getX() - reference.getX()) + Math.abs(pos.getY() - reference.getY()) + Math.abs(pos.getZ() - reference.getZ()) < 16;
	}
	
	private int getBlockLightEmitLvl(World worldIn, BlockPos pos)
	{
		int size = trackedEntity.size();
		LightTrackedEntity lightEntity;
		for(int i = 0; i < size; i++)
		{
			lightEntity = (LightTrackedEntity)trackedEntity.get(i);
			if(lightEntity.lightBlockPos.equals(pos) && lightEntity.entity.worldObj == worldIn)
				return lightEntity.lightLevel;
		}
		return worldIn.getBlockState(pos).getBlock().getLightValue();
	}
	
	private int compressBlockPos(BlockPos pos, BlockPos reference)
	{
		int x = pos.getX() - reference.getX() + 128;
		int y = pos.getY() - reference.getY() + 128;
		int z = pos.getZ() - reference.getZ() + 128;
		return (x & 255) | ((y & 255) << 8) | ((z & 255) << 16);
	}
	
	private BlockPos decompressBlockPos(int compressedBlockPos, BlockPos reference)
	{
		int x = (compressedBlockPos & 255) - 128;
		int y = ((compressedBlockPos >> 8) & 255) - 128;
		int z = ((compressedBlockPos >> 16) & 255) - 128;
		return reference.add(x, y, z);
	}
	
	private void checkLight(World world, BlockPos pos)
	{
		BlockPos blockPosCurr;
		
		int listCurr = 0;
		int listEnd = 0;
		int i;
		
		this.lightUpdateBlockList[listEnd++] = this.compressBlockPos(pos, pos);
		while(listCurr < listEnd)
		{
			blockPosCurr = this.decompressBlockPos(this.lightUpdateBlockList[listCurr++], pos);
			if(this.inRange(blockPosCurr, pos) && this.checkNeighborLight(world, blockPosCurr))
			{
				for(i = 0; i < facingCnt; i++)
						this.lightUpdateBlockList[listEnd++] = this.compressBlockPos(blockPosCurr.offset(enumFacingAry[i]), pos);
			}
			if(listEnd > 30000)break;
		}
//		System.out.printf("list size = %d\n", listEnd);
	}
	
	protected class LightTrackedEntity
	{
		public Entity entity;
		public BlockPos lightBlockPos;
		public int lightLevel;
		
		public LightTrackedEntity(Entity entity, int lightLevel)
		{
			this.entity = entity;
			this.lightBlockPos = new BlockPos(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
			this.lightLevel = lightLevel & 15;
		}
		
		public BlockPos getCurrBlockPos()
		{
			return new BlockPos(MathHelper.floor_double(this.entity.posX), MathHelper.floor_double(this.entity.posY), MathHelper.floor_double(this.entity.posZ));
		}
	}
}
