package net.blacklab.spi.container;

import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSPGenerator extends Container {
	
	protected IInventory generatorInventory;
	protected EntityPlayer thePlayer;
	
	public ContainerSPGenerator(EntityPlayer player, IInventory playerInventory, IInventory gInventory) {
		thePlayer = player;
		generatorInventory = gInventory;
		
		int i,j;
		addSlotToContainer(new Slot(generatorInventory, 0, 62, 35));

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
		if(!thePlayer.worldObj.isRemote && generatorInventory instanceof TileEntitySPObjectBase){
			((TileEntitySPObjectBase)generatorInventory).getWorld().markBlockForUpdate(((TileEntitySPObjectBase)generatorInventory).getPos());
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO 自動生成されたメソッド・スタブ
		return generatorInventory.isUseableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		// TODO 自動生成されたメソッド・スタブ
		Slot srcSlot = (Slot) inventorySlots.get(index);
		ItemStack dstItemStack = null;
		
		if(srcSlot != null && srcSlot.getHasStack()){
			ItemStack srcItemStack = srcSlot.getStack();
			dstItemStack = srcItemStack.copy();
			if(index==0){
				if(!mergeItemStack(srcItemStack, 1, 37, true)) return null;
			}else{
				if(!mergeItemStack(srcItemStack, 0, 1, false)) return null;
			}
			
			if (srcItemStack.stackSize == 0){
				srcSlot.putStack((ItemStack)null);
			}else{
				srcSlot.onSlotChanged();
			}

			srcSlot.onPickupFromSlot(playerIn, srcItemStack);
		}
		
		return dstItemStack;
	}

}
