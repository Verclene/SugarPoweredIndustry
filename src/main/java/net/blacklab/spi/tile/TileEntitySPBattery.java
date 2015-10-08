package net.blacklab.spi.tile;

import net.blacklab.spi.api.ISPObject;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TileEntitySPBattery extends TileEntitySPGenerator {
	
	public static final int DEFAULT_CAPACITY = 5000 * 10;

	@Override
	public float getMaxSP() {
		// TODO 自動生成されたメソッド・スタブ
		return DEFAULT_CAPACITY;
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return "net.blacklab.spi.tile.spbattery";
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO 自動生成されたメソッド・スタブ
		return new ChatComponentText("SP Battery");
	}

	@Override
	public float amountReceiveSPperUpdate(ISPObject spObject) {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean onReceiveSP(float value, ISPObject spObject) {
		boolean flag = addSP(value);
		return flag;
	}

}
