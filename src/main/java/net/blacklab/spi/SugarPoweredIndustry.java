package net.blacklab.spi;

import net.blacklab.spi.common.GuiHandler;
import net.blacklab.spi.common.Init;
import net.blacklab.spi.proxy.SPICommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid=SugarPoweredIndustry.MODID, version=SugarPoweredIndustry.VERSION)
public class SugarPoweredIndustry {
	public static final String MODID = "vespi";
	public static final String VERSION = "SI1 Build 4";
	
	public static final int VERSION_CODE = 1;
	
	@SidedProxy(serverSide="net.blacklab.spi.proxy.SPICommonProxy",clientSide="net.blacklab.spi.proxy.SPIClientProxy")
	public static SPICommonProxy proxy;
	
	@Instance
	public static SugarPoweredIndustry instanceSPI;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Init.registerBlocks();
		if(event.getSide()==Side.CLIENT) Init.registerModels();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		NetworkRegistry.INSTANCE.registerGuiHandler(instanceSPI, new GuiHandler());
		proxy.registerTileEntity();
	}

}
