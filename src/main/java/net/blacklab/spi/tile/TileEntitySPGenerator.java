package net.blacklab.spi.tile;

import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.blacklab.spi.block.BlockSPGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TileEntitySPGenerator extends TileEntitySPObjectBase implements IInventory {
	
	protected ItemStack itemStackToConsume;

	@Override
	public int getMaxSP() {
		// TODO 自動生成されたメソッド・スタブ
		return 5000;
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return "net.blacklab.spi.tile.spgenerator";
	}

	@Override
	public int amountReceiveSPperUpdate(ISPObject spObject) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean onReceiveSP(int value, ISPObject spObject) {
		if(!BlockSPGenerator.sendSPAround(value, worldObj, getPos(), worldObj.getBlockState(getPos()), spObject)){
			addSP(value);
		}
		return true;
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO 自動生成されたメソッド・スタブ
		return new ChatComponentText("SP Generator");
	}

	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return itemStackToConsume;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO 自動生成されたメソッド・スタブ
		if (itemStackToConsume != null){
			ItemStack itemstack;

			if (itemStackToConsume.stackSize <= count){
				itemstack = itemStackToConsume;
				itemStackToConsume = null;
				markDirty();
				return itemstack;
			}else{
				itemstack = itemStackToConsume.splitStack(count);

				if (itemStackToConsume.stackSize == 0){
					itemStackToConsume = null;
				}

				markDirty();
				return itemstack;
			}
		}else{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return itemStackToConsume;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		itemStackToConsume = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
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
		// TODO 自動生成されたメソッド・スタブ
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
		// TODO 自動生成されたメソッド・スタブ
		itemStackToConsume = null;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.readFromNBT(compound);

		sp = compound.getInteger("VSAPI_SP");
		NBTTagCompound tagCompound = compound.getCompoundTag("VSAPI_SLOT");
		itemStackToConsume = ItemStack.loadItemStackFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.writeToNBT(compound);

		compound.setInteger("VSAPI_SP", sp);
		NBTTagCompound tagCompound = new NBTTagCompound();
		if(itemStackToConsume!=null) itemStackToConsume.writeToNBT(tagCompound);
		compound.setTag("VSAPI_SLOT", tagCompound);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(pos, 0, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

}
