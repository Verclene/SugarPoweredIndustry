package net.blacklab.spi.block;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.blacklab.spi.api.ConstUtil;
import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.SPUtil;
import net.blacklab.spi.tile.TileEntitySPCable;
import net.blacklab.spi.tile.TileEntitySPCable.SendingSPList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSPCable extends BlockContainer {
	
	public static final PropertyBool NORTH_CONNECTED = PropertyBool.create("north");
	public static final PropertyBool EAST_CONNECTED = PropertyBool.create("east");
	public static final PropertyBool SOUTH_CONNECTED = PropertyBool.create("south");
	public static final PropertyBool WEST_CONNECTED = PropertyBool.create("west");
	public static final PropertyBool UP_CONNECTED = PropertyBool.create("up");
	public static final PropertyBool DOWN_CONNECTED = PropertyBool.create("down");
	
	public static final int CONNECTION_CHANGED_EVENT = 0;
	public static final int SENDING_CHANGED_EVENT = 1;

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos,
			IBlockState state, Block neighborBlock) {
		if(state.getBlock() instanceof BlockSPCable){
			int connectionbit = getConnectedSide(worldIn, pos);
			worldIn.addBlockEvent(pos, state.getBlock(), CONNECTION_CHANGED_EVENT, connectionbit);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state,
			Random rand) {
		TileEntity tEntity = worldIn.getTileEntity(pos);
		if(tEntity instanceof TileEntitySPCable){
			// SPの伝達
			SendingSPList spList = ((TileEntitySPCable) tEntity).getSendingList();
			if(!spList.isEmpty()){
				spList.prepEntry();
				Map<ISPObject, Float> iMap = spList.getNewestEntry();
				if(iMap!=null){
					Iterator<Entry<ISPObject, Float>> iterator = iMap.entrySet().iterator();
					while(iterator.hasNext()){
						Entry<ISPObject, Float> entry = iterator.next();
						((TileEntitySPCable) tEntity).addSP(entry.getValue());
						if(!SPUtil.sendSPAround(entry.getValue(), worldIn, pos, state, entry.getKey())){
							SPUtil.sendSPAround(entry.getValue(), worldIn, pos, state, (ISPObject) tEntity);
						}else{
							if(!((TileEntitySPCable)tEntity).isSending){
								((TileEntitySPCable) tEntity).isSending = true;
								worldIn.markBlockForUpdate(pos);
							}
						}
					}
				}
				// 残ってしまった？
				if(((TileEntitySPCable) tEntity).getSP()>0){
					spList.putEntry((ISPObject) tEntity, ((TileEntitySPCable) tEntity).getSP());
					((TileEntitySPCable) tEntity).setSP(0);
				}
			}else{
				if(((TileEntitySPCable)tEntity).isSending){
					((TileEntitySPCable) tEntity).isSending = false;
					worldIn.markBlockForUpdate(pos);
				}
			}
		}
		worldIn.scheduleUpdate(pos, state.getBlock(), 1);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		worldIn.scheduleUpdate(pos, state.getBlock(), 1);
		return state;
	}

	
	public static int getConnectedSide(World worldIn, BlockPos pos){
		int connectionbit = 0;
		for(int mode=0; mode<6; mode++){
			BlockPos targetPos = pos.add(ConstUtil.XBOUND_LOOP[mode], ConstUtil.YBOUND_LOOP[mode], ConstUtil.ZBOUND_LOOP[mode]);
			boolean flag = false;
			if(worldIn.getTileEntity(targetPos) instanceof ISPObject) flag = true;
				
			switch (mode) {
			case 0:
//				worldIn.setBlockState(pos, state.withProperty(SOUTH_CONNECTED, Boolean.valueOf(flag)));
				connectionbit |= (flag?1:0)<<5;
				break;
			case 1:
//				worldIn.setBlockState(pos, state.withProperty(EAST_CONNECTED, Boolean.valueOf(flag)));
				connectionbit |= (flag?1:0)<<4;
				break;
			case 2:
//				worldIn.setBlockState(pos, state.withProperty(NORTH_CONNECTED, Boolean.valueOf(flag)));
				connectionbit |= (flag?1:0)<<3;
				break;
			case 3:
//				worldIn.setBlockState(pos, state.withProperty(WEST_CONNECTED, Boolean.valueOf(flag)));
				connectionbit |= (flag?1:0)<<2;
				break;
			case 4:
//				worldIn.setBlockState(pos, state.withProperty(UP_CONNECTED, Boolean.valueOf(flag)));
				connectionbit |= (flag?1:0)<<1;
				break;
			case 5:
//				worldIn.setBlockState(pos, state.withProperty(DOWN_CONNECTED, Boolean.valueOf(flag)));
				connectionbit |= (flag?1:0);
				break;
			}
		}
		return connectionbit;
	}

	public BlockSPCable() {
		super(Material.glass);
		setUnlocalizedName("spcable");
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos,
			EnumFacing side) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
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
