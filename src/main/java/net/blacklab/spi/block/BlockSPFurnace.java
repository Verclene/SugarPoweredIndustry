package net.blacklab.spi.block;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.common.GuiHandler;
import net.blacklab.spi.tile.TileEntitySPFurnace;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

public class BlockSPFurnace extends BlockContainer {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockSPFurnace() {
		super(new Material(MapColor.stoneColor));
		setCreativeTab(CreativeTabs.tabRedstone);
		setUnlocalizedName("spfurnace");
		setHardness(4.0f);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if(facing.getAxis() == Axis.Y){
			facing = EnumFacing.NORTH;
		}
		return super.getStateFromMeta(meta).withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySPFurnace();
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		state = state.withProperty(FACING, placer.getHorizontalFacing());
		worldIn.scheduleUpdate(pos, state.getBlock(), 1);
		return state;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote){
			playerIn.openGui(SugarPoweredIndustry.instanceSPI, GuiHandler.GUI_ID_SPFURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

}
