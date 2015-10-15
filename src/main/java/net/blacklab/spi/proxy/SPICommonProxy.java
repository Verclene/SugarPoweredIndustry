package net.blacklab.spi.proxy;

import net.blacklab.spi.tile.TileEntitySPBattery;
import net.blacklab.spi.tile.TileEntitySPCable;
import net.blacklab.spi.tile.TileEntitySPFurnace;
import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SPICommonProxy {
	public void registerTileEntity(){
		GameRegistry.registerTileEntity(TileEntitySPGenerator.class, "tilespgenerator");
		GameRegistry.registerTileEntity(TileEntitySPBattery.class, "tilespbattery");
		GameRegistry.registerTileEntity(TileEntitySPCable.class, "tilespcable");
		GameRegistry.registerTileEntity(TileEntitySPFurnace.class, "tilespfurnace");
	}
}
