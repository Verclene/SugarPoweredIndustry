package net.blacklab.spi.common;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.block.BlockSPBattery;
import net.blacklab.spi.block.BlockSPCable;
import net.blacklab.spi.block.BlockSPGenerator;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Init {

	public static BlockSPCable blockSPCable;

	public static BlockSPGenerator blockSPGenerator;
	public static BlockSPBattery blockSPBattery;

	public static void registerBlocks() {
		// Block
		blockSPCable = new BlockSPCable();
		GameRegistry.registerBlock(blockSPCable, "spcable");

		blockSPGenerator = new BlockSPGenerator();
		GameRegistry.registerBlock(blockSPGenerator, "spgenerator");

		blockSPBattery = new BlockSPBattery();
		GameRegistry.registerBlock(blockSPBattery, "spbattery");
	}

	public static void registerModels(){
		ModelLoader.setCustomModelResourceLocation(
				Item.getItemFromBlock(blockSPGenerator), 0, new ModelResourceLocation(SugarPoweredIndustry.MODID + ":spgenerator", "inventory"));

		ModelLoader.setCustomModelResourceLocation(
				Item.getItemFromBlock(blockSPBattery), 0, new ModelResourceLocation(SugarPoweredIndustry.MODID + ":spbattery", "inventory"));

		ModelLoader.setCustomModelResourceLocation(
				Item.getItemFromBlock(blockSPCable), 0, new ModelResourceLocation(SugarPoweredIndustry.MODID + ":spcable", "inventory"));
	}

}
