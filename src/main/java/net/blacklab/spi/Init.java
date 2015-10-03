package net.blacklab.spi;

import net.blacklab.spi.block.BlockSPGenerator;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Init {
	
	public static BlockSPGenerator blockSPGenerator;
	
	public static void registerBlocks() {
		blockSPGenerator = new BlockSPGenerator();
		GameRegistry.registerBlock(blockSPGenerator, "spgenerator");
	}
	
	public static void registerModels(){
		ModelLoader.setCustomModelResourceLocation(
				Item.getItemFromBlock(blockSPGenerator), 0, new ModelResourceLocation(SugarPoweredIndustry.MODID + ":spgenerator", "inventory"));
	}

}
