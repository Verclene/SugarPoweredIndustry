package net.blacklab.spi.api;

import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySPObjectBase extends TileEntity implements
		ISPObject {
	
	protected int sp = 0;
	
	@Override
	public int getSP() {
		// TODO 自動生成されたメソッド・スタブ
		return sp;
	}
	
	@Override
	public void setSP(int value) {
		if(value<=getMaxSP()){
			sp = value;
		}else{
			sp = getMaxSP();
		}
	}

	@Override
	public void addSP(int value) {
		// TODO 自動生成されたメソッド・スタブ
		if(value > (getMaxSP()-sp)){
			sp = getMaxSP();
		}
		sp += value;
	}

	@Override
	public void sendSPToObject(int value, ISPObject spObject) {
		if(sp<value) value = sp;
		if(spObject.onReceiveSP(value, this)){
			spObject.addSP(value);
			sp -= value;
		}
	}

	@Override
	public boolean onReceiveSP(int value, ISPObject spObject) {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}
	
}
