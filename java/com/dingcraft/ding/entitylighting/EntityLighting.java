package com.dingcraft.ding.entitylighting;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import com.dingcraft.ding.entity.EntityArrowTorch;
import com.google.common.collect.Lists;

public class EntityLighting
{
	protected List trackedEntity;
	protected int[] lightUpdateBlockQueue;
	
	private final EnumFacing[] enumFacingAry = EnumFacing.values();
	private final int facingCnt = enumFacingAry.length;
	
	public EntityLighting()
	{
		this.lightUpdateBlockQueue = new int[32768];
		this.trackedEntity = Lists.newArrayList();
	}
	
	@SubscribeEvent
	public void onEntityAdded(EntityJoinWorldEvent event)
	{
		//Only for testing. Entity registry will be implemented later.

		if(event.entity instanceof EntityArrowTorch)
		{
			LightSourceArrowTorch arrow = new LightSourceArrowTorch((EntityArrowTorch)event.entity);
			trackedEntity.add(arrow);
			this.checkLight(event.entity.worldObj, arrow.getBlockPos());
			System.out.println("Entity added.");
		}
	}
	
	@SubscribeEvent
	public void tick(ServerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START) return;
		int i = 0;
		LightSourceEntity lightEntity = null;
		while(i < trackedEntity.size())
		{
			lightEntity = (LightSourceEntity)this.trackedEntity.get(i);
			if(lightEntity.shouldBeRemoved())
			{
				trackedEntity.remove(i);
				this.checkLight(lightEntity.entity.worldObj, lightEntity.getBlockPos());
			}
			else
				i++;
		}
		int size = this.trackedEntity.size();
		World world;
		for(i = 0; i < size; i++)
		{
			lightEntity = (LightSourceEntity)this.trackedEntity.get(i);
			world = lightEntity.entity.worldObj;
			BlockPos blockPosOld = lightEntity.hasMoved();
			if(blockPosOld != null)
			{
				this.checkLight(world, lightEntity.getBlockPos());
				this.checkLight(world, blockPosOld);
			}
			else if(world.getLightFor(EnumSkyBlock.BLOCK, lightEntity.getBlockPos()) < lightEntity.getLightLevel())
				this.checkLight(world, lightEntity.getBlockPos());
		}
	}
	
	public void stop()
	{
		//empty the list and cancel all lighting
		BlockPos pos;
		LightSourceEntity entry;
		while(!this.trackedEntity.isEmpty())
		{
			entry =  (LightSourceEntity)this.trackedEntity.get(0);
			this.trackedEntity.remove(0);
			this.checkLight(entry.entity.worldObj, entry.getBlockPos());
		}
	}
	
	private boolean checkNeighborLight(World worldIn, BlockPos pos)
	{
		int lightLvlCurr = worldIn.getLightFor(EnumSkyBlock.BLOCK, pos);
		int lightLvlNew = 0;
		int l;
		BlockPos nextPos;
		for(int i = 0; i < facingCnt; i++)
		{
			nextPos = pos.offset(enumFacingAry[i]);
			l = worldIn.getLightFor(EnumSkyBlock.BLOCK, nextPos);
			lightLvlNew = Math.max(lightLvlNew, worldIn.getLightFor(EnumSkyBlock.BLOCK, pos.offset(enumFacingAry[i])));
		}
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
		return Math.abs(pos.getX() - reference.getX()) + Math.abs(pos.getY() - reference.getY()) + Math.abs(pos.getZ() - reference.getZ()) < 20;
	}
	
	public int getBlockLightEmitLvl(World worldIn, BlockPos pos)
	{
		int size = trackedEntity.size();
		LightSourceEntity entry;
		int lightLvl = 0;
		for(int i = 0; i < size; i++)
		{
			entry = (LightSourceEntity)trackedEntity.get(i);
			if(entry.getBlockPos().equals(pos) && entry.entity.worldObj == worldIn)
			{
				lightLvl = entry.getLightLevel();
				break;
			}
		}
		return Math.max(lightLvl, worldIn.getBlockState(pos).getBlock().getLightValue());
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
	
	public void checkLight(World world, BlockPos pos)
	{
		BlockPos blockPosCurr;
		
		int listCurr = 0;
		int listEnd = 0;
		int i, j;
		int compressedBlockPos;
		
		this.lightUpdateBlockQueue[listEnd++] = this.compressBlockPos(pos, pos);
		while(listCurr < listEnd)
		{
			blockPosCurr = this.decompressBlockPos(this.lightUpdateBlockQueue[listCurr++], pos);
			if(this.inRange(blockPosCurr, pos) && this.checkNeighborLight(world, blockPosCurr))
			{
				for(i = 0; i < facingCnt; i++)
				{
					compressedBlockPos = this.compressBlockPos(blockPosCurr.offset(enumFacingAry[i]), pos);
					for(j = listCurr; j < listEnd; j++)
						if(this.lightUpdateBlockQueue[j] == compressedBlockPos) break;
					if(j == listEnd)
						this.lightUpdateBlockQueue[listEnd++] = compressedBlockPos;
				}
			}
			if(listEnd > 30000)break;
		}
//		if(listEnd < 100)
			System.out.println(listEnd);
		
//		System.out.printf("list size = %d\n", listEnd);
	}
	
}
