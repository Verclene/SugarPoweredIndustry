package net.blacklab.spi.block;

import java.util.Random;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.SPUtil;
import net.blacklab.spi.common.GuiHandler;
import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSPGenerator extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockSPGenerator() {
		super(new Material(MapColor.stoneColor));
		setUnlocalizedName("spgenerator");
		setHardness(2.0f);
		setCreativeTab(CreativeTabs.tabRedstone);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	protected BlockState createBlockState() {
		// TODO 自動生成されたメソッド・スタブ
		return new BlockState(this, FACING);
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
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

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote){
			playerIn.openGui(SugarPoweredIndustry.instanceSPI, GuiHandler.GUI_ID_SPGENERATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state,
			Random rand) {
		TileEntity tEntity = worldIn.getTileEntity(pos);
		if(tEntity instanceof TileEntitySPGenerator){
			// 砂糖の消費、SP生産
			ItemStack stack = ((TileEntitySPGenerator) tEntity).getStackInSlot(0);
			if(stack!=null && stack.getItem()==Items.sugar){
				if(((TileEntitySPGenerator) tEntity).addSP(50))
					((TileEntitySPGenerator) tEntity).decrStackSize(0, 1);;
			}

			//SPの配信
			if(((TileEntitySPGenerator) tEntity).getSP() > 0 && !worldIn.isBlockPowered(pos))
				SPUtil.sendSPAround(Math.min(((TileEntitySPGenerator) tEntity).getSP(), sendingSPperUpdate), worldIn, pos, state, (ISPObject) tEntity);
		}
		worldIn.scheduleUpdate(pos, state.getBlock(), 1);
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

	public int sendingSPperUpdate = 300;

}
