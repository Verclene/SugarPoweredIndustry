package net.blacklab.spi.proxy;

import net.blacklab.spi.client.renderer.RenderSPCable;
import net.blacklab.spi.tile.TileEntitySPCable;
import net.minecraftforge.fml.client.registry.ClientRegistry;


public class SPIClientProxy extends SPICommonProxy {

	@Override
	public void registerTileEntity() {
		// TODO 自動生成されたメソッド・スタブ
		super.registerTileEntity();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySPCable.class, new RenderSPCable());
	}

}
