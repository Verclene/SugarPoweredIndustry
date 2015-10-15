package net.blacklab.spi.container;

import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerSPFurnace extends Container {
	
	private IInventory furnaceInventory;
	private EntityPlayer thePlayer;
	
	public ContainerSPFurnace(EntityPlayer player, IInventory playerInventory, IInventory fInventory) {
		furnaceInventory = fInventory;
		thePlayer = player;
		
		addSlotToContainer(new Slot(furnaceInventory, 0, 56, 34));
		addSlotToContainer(new SlotFurnaceOutput(thePlayer, furnaceInventory, 1, 116, 34));

		int i, j;
		for (i = 0; i < 3; ++i){
			for (j = 0; j < 9; ++j){
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i){
			addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if(!thePlayer.worldObj.isRemote && furnaceInventory instanceof TileEntitySPObjectBase){
			((TileEntitySPObjectBase)furnaceInventory).getWorld().markBlockForUpdate(((TileEntitySPObjectBase)furnaceInventory).getPos());
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return furnaceInventory.isUseableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 1){
				if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null){
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				} else if (index >= 2 && index < 29) {
					if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
						return null;
					}
				} else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
 	}
}
