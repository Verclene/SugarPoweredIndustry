package net.blacklab.spi.block;

import net.blacklab.spi.api.ConstUtil;
import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.tile.TileEntitySPCable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSPCable extends BlockContainer {
	
	public static final PropertyBool NORTH_CONNECTED = PropertyBool.create("north");
	public static final PropertyBool EAST_CONNECTED = PropertyBool.create("east");
	public static final PropertyBool SOUTH_CONNECTED = PropertyBool.create("south");
	public static final PropertyBool WEST_CONNECTED = PropertyBool.create("west");
	public static final PropertyBool UP_CONNECTED = PropertyBool.create("up");
	public static final PropertyBool DOWN_CONNECTED = PropertyBool.create("down");

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos,
			IBlockState state, Block neighborBlock) {
		for(int mode=0; mode<6; mode++){
			BlockPos targetPos = pos.add(ConstUtil.XBOUND_LOOP[mode], ConstUtil.YBOUND_LOOP[mode], ConstUtil.ZBOUND_LOOP[mode]);
			boolean flag = false;
			if(worldIn.getTileEntity(targetPos) instanceof ISPObject) flag = true;
				
			switch (mode) {
			case 0:
				worldIn.setBlockState(pos, state.withProperty(SOUTH_CONNECTED, Boolean.valueOf(flag)));
				break;
			case 1:
				worldIn.setBlockState(pos, state.withProperty(EAST_CONNECTED, Boolean.valueOf(flag)));
				break;
			case 2:
				worldIn.setBlockState(pos, state.withProperty(NORTH_CONNECTED, Boolean.valueOf(flag)));
				break;
			case 3:
				worldIn.setBlockState(pos, state.withProperty(WEST_CONNECTED, Boolean.valueOf(flag)));
				break;
			case 4:
				worldIn.setBlockState(pos, state.withProperty(UP_CONNECTED, Boolean.valueOf(flag)));
				break;
			case 5:
				worldIn.setBlockState(pos, state.withProperty(DOWN_CONNECTED, Boolean.valueOf(flag)));
				break;
			}
		}
	}

	public BlockSPCable() {
		super(Material.glass);
		setUnlocalizedName("spcable");
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public int getRenderType() {
		// TODO 自動生成されたメソッド・スタブ
		return 3;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO 自動生成されたメソッド・スタブ
		return new TileEntitySPCable();
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

}
