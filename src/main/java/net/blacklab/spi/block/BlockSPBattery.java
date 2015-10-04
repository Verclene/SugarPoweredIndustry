package net.blacklab.spi.block;

import java.util.Random;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.common.GuiHandler;
import net.blacklab.spi.tile.TileEntitySPBattery;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSPBattery extends BlockSPGenerator {
	
	public BlockSPBattery() {
		super();
		setUnlocalizedName("spbattery");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySPBattery();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote){
			playerIn.openGui(SugarPoweredIndustry.instanceSPI, GuiHandler.GUI_ID_SPBATTERY, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state,
			Random rand) {
//		worldIn.scheduleUpdate(pos, state.getBlock(), 2);
	}

}
