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
	protected short[] lightUpdateBlockQueue;
	protected int[] lightUpdateBlockFlag;
	
	private final EnumFacing[] enumFacingAry = EnumFacing.values();
	private final int facingCnt = enumFacingAry.length;
	
	public EntityLighting()
	{
		this.lightUpdateBlockQueue = new short[8192];
		this.lightUpdateBlockFlag = new int[1024];
		this.trackedEntity = Lists.newArrayList();
	}
	
	@SubscribeEvent
	public void onEntityAdded(EntityJoinWorldEvent event)
	{
		//Only for testing.

		if(event.entity instanceof EntityArrowTorch)
		{
			LightSourceArrowTorch arrow = new LightSourceArrowTorch((EntityArrowTorch)event.entity);
			this.addEntity(arrow);
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
	
	/**
	 * The interface for light source entity registry.</br>
	 * 
	 * @param entry The light source entity to be registered.
	 * @return True if added to list, and false if the entry already exists.
	 */
	public boolean addEntity(LightSourceEntity entry)
	{
		if(this.trackedEntity.contains(entry))
			return false;
		this.trackedEntity.add(entry);
		this.checkLight(entry.entity.worldObj, entry.getBlockPos());
		return true;
	}
	
	/**
	 * The interface to remove an entity.</br>
	 * Note that returning false in method <b>shouldBeRemoved</b> can also remove an entity.
	 * @param entry The light source entity to be removed.
	 * @return True if successfully removed, and false if the entry does not exist.
	 */
	public boolean removeEntity(LightSourceEntity entry)
	{
		int size = this.trackedEntity.size();
		for(int i = 0; i < size; i++)
		{
			if((LightSourceEntity)this.trackedEntity.get(i) == entry)
			{
				this.trackedEntity.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public void stop()
	{
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
	
	private short compressBlockPos(BlockPos pos, BlockPos reference)
	{
		int x = pos.getX() - reference.getX() + 16;
		int y = pos.getY() - reference.getY() + 16;
		int z = pos.getZ() - reference.getZ() + 16;
		return (short)((x & 31) | ((y & 31) << 5) | ((z & 31) << 10));
	}
	
	private BlockPos decompressBlockPos(short compressedBlockPos, BlockPos reference)
	{
		int x = (compressedBlockPos & 31) - 16;
		int y = ((compressedBlockPos >> 5) & 31) - 16;
		int z = ((compressedBlockPos >> 10) & 31) - 16;
		return reference.add(x, y, z);
	}
	
	private void checkLight(World world, BlockPos pos)
	{
		BlockPos blockPosCurr;
		BlockPos blockPosNext;
		
		int listCurr = 0;
		int listEnd = 0;
		int i, j;
		short compressedBlockPos;
		
		this.lightUpdateBlockQueue[listEnd++] = this.compressBlockPos(pos, pos);
		while(listCurr < listEnd)
		{
			compressedBlockPos = this.lightUpdateBlockQueue[listCurr++];
			blockPosCurr = this.decompressBlockPos(compressedBlockPos, pos);
			this.lightUpdateBlockFlag[(compressedBlockPos >> 5)] &= (-2 << (compressedBlockPos & 31));
			if(this.checkNeighborLight(world, blockPosCurr))
			{
				for(i = 0; i < facingCnt; i++)
				{
					blockPosNext = blockPosCurr.offset(enumFacingAry[i]);
					if(this.inRange(blockPosNext, pos))
					{
						compressedBlockPos = this.compressBlockPos(blockPosNext, pos);
						if(((this.lightUpdateBlockFlag[(compressedBlockPos >> 5)] >> (compressedBlockPos & 31)) & 1) == 0)
						{
							this.lightUpdateBlockQueue[listEnd++] = compressedBlockPos;
							this.lightUpdateBlockFlag[(compressedBlockPos >> 5)] |= (1 << (compressedBlockPos & 31));
						}
					}
				}
			}
		}
//		if(listEnd > 100)
//			System.out.printf("list size = %d\n", listEnd);
	}
	
}
