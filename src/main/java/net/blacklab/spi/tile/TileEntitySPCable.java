package net.blacklab.spi.tile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.blacklab.spi.api.ConstUtil;
import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class TileEntitySPCable extends TileEntitySPObjectBase {

	@Override
	public int amountReceiveSPperUpdate(ISPObject spObject) {
		// 周囲の設置状況を見て、総量を計算
		BlockPos pos = getPos();
		int amount = 0;
		for(int i=0; i<6; i++){
			BlockPos targetPos = pos.add(ConstUtil.XBOUND_LOOP[i], ConstUtil.YBOUND_LOOP[i], ConstUtil.ZBOUND_LOOP[i]);
			TileEntity tEntity = worldObj.getTileEntity(targetPos);
			if(tEntity instanceof ISPObject && !tEntity.equals(spObject)){
				amount += ((ISPObject)tEntity).amountReceiveSPperUpdate(this);
			}
		}
		return amount;
	}

	@Override
	public boolean onReceiveSP(int value, ISPObject spObject) {
		int expectedSP = amountReceiveSPperUpdate(spObject);
		Map<TileEntity, Integer> targetEntities = new HashMap<TileEntity, Integer>();
		for(int i=0; i<6; i++){
			BlockPos targetPos = pos.add(ConstUtil.XBOUND_LOOP[i], ConstUtil.YBOUND_LOOP[i], ConstUtil.ZBOUND_LOOP[i]);
			TileEntity tEntity = worldObj.getTileEntity(targetPos);
			if(tEntity instanceof ISPObject && !tEntity.equals(spObject)){
				targetEntities.put(tEntity, ((ISPObject)tEntity).amountReceiveSPperUpdate(this));
			}
		}
		if(targetEntities.isEmpty()) return false;
		setSP(Math.min(value, expectedSP));

		double sendingRatio = 1.0D;
		if(value < expectedSP) sendingRatio = (double)value / (double)expectedSP;
		Iterator iterator = targetEntities.entrySet().iterator();
		while(iterator.hasNext()){
			Entry entry = (Entry) iterator.next();
			// カッコマシ☆モノグサキャストオオメ
			sendSPToObject((int)(((Integer)entry.getValue()).doubleValue() * sendingRatio), ((ISPObject)entry.getKey()));
		}
		if(value > expectedSP) spObject.addSP(value - expectedSP);
		return true;
	}

	@Override
	public int getMaxSP() {
		return Integer.MAX_VALUE;
	}

}
