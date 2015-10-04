package net.blacklab.spi.block;

import java.util.Random;

import org.omg.CORBA.PRIVATE_MEMBER;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.client.gui.GuiHandler;
import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
			ItemStack stack = ((TileEntitySPGenerator) tEntity).getStackInSlot(0);
			if(stack!=null && stack.getItem()==Items.sugar){
				if(((TileEntitySPGenerator) tEntity).addSP(50))
					((TileEntitySPGenerator) tEntity).decrStackSize(0, 1);;
			}
		}
		worldIn.scheduleUpdate(pos, state.getBlock(), 2);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		worldIn.scheduleUpdate(pos, state.getBlock(), 2);
		return state;
	}

}
