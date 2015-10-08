package net.blacklab.spi.api;

import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySPObjectBase extends TileEntity implements
		ISPObject {
	
	protected float sp = 0;
	
	@Override
	public float getSP() {
		return sp;
	}
	
	@Override
	public void setSP(float value) {
		if(value<=getMaxSP()){
			sp = value;
		}else{
			sp = getMaxSP();
		}
	}

	@Override
	public boolean addSP(float value) {
		// TODO 自動生成されたメソッド・スタブ
		if(sp>=getMaxSP()){
			sp = getMaxSP();
			return false;
		}
		if(value > (getMaxSP()-sp)){
			sp = getMaxSP();
			return true;
		}
		sp += value;
		return true;
	}

	@Override
	public float amountReceiveSPperUpdate(ISPObject spObject) {
		if(sp>=getMaxSP()) return 0;
		return 50;
	}

	@Override
	public boolean sendSPToObject(float value, ISPObject spObject) {
		if(sp<value) value = sp;
		if(spObject.onReceiveSP(value, this)){
			sp -= value;
			return true;
		}
		return false;
	}

	@Override
	public boolean onReceiveSP(float value, ISPObject spObject) {
		return addSP(value);
	}

}
