package net.blacklab.spi.tile;

import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TileEntitySPFurnace extends TileEntitySPObjectBase implements
		IInventory {
	
	private ItemStack burningStack[] = new ItemStack[2];
	private int burningTime = 0;
	private int cookTime = 0;

	@Override
	public String getName() {
		return "net.blacklab.spi.tile.spfurnace";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("SP Furnace");
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return burningStack[Math.min(1, Math.max(0, index))];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (burningStack[index] != null) {
			ItemStack itemstack;

			if (burningStack[index].stackSize <= count) {
				itemstack = burningStack[index];
				burningStack[index] = null;
				return itemstack;
			} else {
				itemstack = burningStack[index].splitStack(count);

				if (burningStack[index].stackSize == 0) {
					burningStack[index] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}

	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (burningStack[index] != null) {
			ItemStack itemstack = burningStack[index];
			burningStack[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		boolean flag = stack != null && stack.isItemEqual(burningStack[index]) && ItemStack.areItemStackTagsEqual(stack, burningStack[index]);
		burningStack[index] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		if (index == 0 && !flag) {
			burningTime = 1000;
			cookTime = 0;
			markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void clear() {
		burningStack[0] = null;
		burningStack[1] = null;
	}

	@Override
	public float getMaxSP() {
		return 5000f;
	}

	@Override
	public float amountReceiveSPperUpdate(ISPObject spObject) {
		return Math.min(300f, getMaxSP()-sp);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		sp = compound.getFloat("VSAPI_SP");
		
		burningStack[0] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("VESPI_FURNACE_SRC"));
		burningStack[1] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("VESPI_FURNACE_DST"));
		
		burningTime = compound.getInteger("VESPI_FURNACE_BURNTIME");
		cookTime = compound.getInteger("VESPI_FURNACE_COOKTIME");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setFloat("VSAPI_SP", sp);

		NBTTagCompound srcSlotTagCompound = new NBTTagCompound();
		if(burningStack[0]!=null) burningStack[0].writeToNBT(srcSlotTagCompound);
		compound.setTag("VESPI_FURNACE_SRC", srcSlotTagCompound);

		NBTTagCompound dstSlotTagCompound = new NBTTagCompound();
		if(burningStack[1]!=null) burningStack[1].writeToNBT(dstSlotTagCompound);
		compound.setTag("VESPI_FURNACE_DST", dstSlotTagCompound);
		
		compound.setInteger("VESPI_FURNACE_BURNTIME", burningTime);
		compound.setInteger("VESPI_FURNACE_COOKTIME", cookTime);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(pos, 0, tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
