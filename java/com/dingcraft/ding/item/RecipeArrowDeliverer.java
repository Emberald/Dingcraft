package com.dingcraft.ding.item;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeArrowDeliverer implements IRecipe
{
	private ItemStack craftOutput;
	private int itemToCarry;
	
	public boolean matches(InventoryCrafting pattern, World worldIn)
	{
		int i, size;
		size = pattern.getSizeInventory();
		int itemCount = 0, arrowCount = 0;
		int arrowPos = 0;
		ItemStack currItem;
		for(i = 0; i < size; i++)
		{
			currItem = pattern.getStackInSlot(i);
			if(currItem != null)
			{
				if(currItem.getItem() == DingItems.arrowDeliverer)
				{
					arrowCount++;
					arrowPos = i;
				}
				else if(currItem.getItem() == DingItems.arrowDelivererLoaded)
					return false;
				else
				{
					itemCount++;
					this.itemToCarry = i;
				}
			}
		}
		if(itemCount == 1 && arrowCount == 1)
		{
			this.craftOutput = new ItemStack(DingItems.arrowDelivererLoaded);
			NBTTagCompound itemTags = new NBTTagCompound();
			pattern.getStackInSlot(this.itemToCarry).writeToNBT(itemTags);
			this.craftOutput.setTagCompound(itemTags);
			return true;
		}
		else
			return false;
	}

	public ItemStack getCraftingResult(InventoryCrafting pattern)
	{
		return this.craftOutput.copy();
	}

	public int getRecipeSize()
	{
		return 7;
	}

	public ItemStack getRecipeOutput()
	{
		return this.craftOutput;
	}

	public ItemStack[] getRemainingItems(InventoryCrafting pattern)
	{
		pattern.setInventorySlotContents(this.itemToCarry, null);
		ItemStack[] remainingItems = new ItemStack[pattern.getSizeInventory()];
		int i;
		for(i = 0; i < remainingItems.length; i++)
			remainingItems[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(pattern.getStackInSlot(i));
		return remainingItems;
	}
}
