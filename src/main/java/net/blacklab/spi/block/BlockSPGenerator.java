package net.blacklab.spi.block;

import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSPGenerator extends BlockContainer {

	public BlockSPGenerator() {
		super(Material.rock);
		setUnlocalizedName("spgenerator");
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySPGenerator();
	}

	@Override
	public int getRenderType() {
		// TODO 自動生成されたメソッド・スタブ
		return 3;
	}

}
