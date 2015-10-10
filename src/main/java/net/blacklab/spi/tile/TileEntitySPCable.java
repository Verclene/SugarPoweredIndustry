package net.blacklab.spi.tile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.blacklab.spi.block.BlockSPCable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntitySPCable extends TileEntitySPObjectBase {
	
	public class SendingSPList {
		private CopyOnWriteArrayList<Map<ISPObject, Float>> inputMap;
		
		public SendingSPList() {
			inputMap = new CopyOnWriteArrayList<Map<ISPObject,Float>>();
		}
		
		public void putEntry(ISPObject object, Float amount){
			Map<ISPObject, Float> entry;
			if(inputMap.isEmpty()){
				inputMap.add(new ConcurrentHashMap<ISPObject, Float>());
			}
			entry = inputMap.get(inputMap.size()-1);
			if(entry==null) entry = new ConcurrentHashMap<ISPObject, Float>();
			if(entry.containsKey(object)){
				entry.put(object, entry.get(object) + amount);
			}else{
				entry.put(object, amount);
			}
		}
		
		public Map<ISPObject, Float> getNewestEntry(){
			if(inputMap.isEmpty()) return null;
			return inputMap.remove(0);
		}
		
		public void prepEntry(){
			if(inputMap.size()<=1) inputMap.add(new ConcurrentHashMap<ISPObject, Float>());
		}
		
		public boolean isEmpty(){
			boolean flag = inputMap.isEmpty();
			if(!flag) flag |= inputMap.get(0)==null ? true : inputMap.get(0).isEmpty();
			return flag;
		}
	}

	private int connection = 0;
	private SendingSPList sendingSPList;
	
	public boolean isSending = false;
	
	public TileEntitySPCable() {
		super();
		sendingSPList = new SendingSPList();
	}
	
	public SendingSPList getSendingList(){
		return sendingSPList;
	}

	@Override
	public float amountReceiveSPperUpdate(ISPObject spObject) {
		return 300;
	}

	@Override
	public boolean onReceiveSP(float value, ISPObject spObject) {
		sendingSPList.putEntry(spObject, value);
		return true;
	}

	@Override
	public float getMaxSP() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.readFromNBT(compound);
		connection = compound.getInteger("VESPI_CABLE_CONNECTION");
		isSending = compound.getBoolean("VESPI_CABLE_ISSENDING");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.writeToNBT(compound);
		compound.setInteger("VESPI_CABLE_CONNECTION", connection);
		compound.setBoolean("VESPI_CABLE_ISSENDING", isSending);
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

	@Override
	public boolean receiveClientEvent(int id, int type) {
		if(id==BlockSPCable.SENDING_CHANGED_EVENT){
			isSending = type==1;
			return true;
		}
		return super.receiveClientEvent(id, type);
	}

	public void setConnection(int c){
		connection = c;
	}
	
	public int getConnection(){
		return connection;
	}

}
