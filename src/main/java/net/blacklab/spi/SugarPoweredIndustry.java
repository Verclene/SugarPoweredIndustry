package net.blacklab.spi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid=SugarPoweredIndustry.MODID, version=SugarPoweredIndustry.VERSION)
public class SugarPoweredIndustry {
	public static final String MODID = "vespi";
	public static final String VERSION = "SI1 Build 1";
	
	public static final int VERSION_CODE = 1;
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event){
		Init.registerBlocks();
		if(event.getSide()==Side.CLIENT) Init.registerModels();
	}

}
