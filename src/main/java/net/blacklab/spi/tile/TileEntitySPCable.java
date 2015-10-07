package net.blacklab.spi.tile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntitySPCable extends TileEntitySPObjectBase {
	
	public class SendingSPList {
		private CopyOnWriteArrayList<Map<ISPObject, Integer>> inputMap;
		
		public SendingSPList() {
			inputMap = new CopyOnWriteArrayList<Map<ISPObject,Integer>>();
		}
		
		public void putEntry(ISPObject object, Integer amount){
			Map<ISPObject, Integer> entry;
			if(inputMap.isEmpty()){
				inputMap.add(new ConcurrentHashMap<ISPObject, Integer>());
			}
			entry = inputMap.get(inputMap.size()-1);
			if(entry==null) entry = new ConcurrentHashMap<ISPObject, Integer>();
			if(entry.containsKey(object)){
				entry.put(object, entry.get(object) + amount);
			}else{
				entry.put(object, amount);
			}
		}
		
		public Map<ISPObject, Integer> getNewestEntry(){
			if(inputMap.isEmpty()) return null;
			return inputMap.remove(0);
		}
		
		public void prepEntry(){
			inputMap.add(new ConcurrentHashMap<ISPObject, Integer>());
		}
		
		public boolean isEmpty(){
			return inputMap.isEmpty();
		}
	}

	private int connection = 0;
	private SendingSPList sendingSPList;
	
	public TileEntitySPCable() {
		super();
		sendingSPList = new SendingSPList();
	}
	
	public SendingSPList getSendingList(){
		return sendingSPList;
	}

	@Override
	public int amountReceiveSPperUpdate(ISPObject spObject) {
		return 300;
	}

	@Override
	public boolean onReceiveSP(int value, ISPObject spObject) {
		sendingSPList.putEntry(spObject, value);
		return true;
	}

	@Override
	public int getMaxSP() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.readFromNBT(compound);
		connection = compound.getInteger("VSAPI_CABLE_CONNECTION");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ
		super.writeToNBT(compound);
		compound.setInteger("VSAPI_CABLE_CONNECTION", connection);
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

	public void setConnection(int c){
		connection = c;
	}
	
	public int getConnection(){
		return connection;
	}

}
