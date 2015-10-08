package net.blacklab.spi.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.api.ConstUtil;
import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.api.TileEntitySPObjectBase;
import net.blacklab.spi.common.GuiHandler;
import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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
			// 砂糖の消費、SP生産
			ItemStack stack = ((TileEntitySPGenerator) tEntity).getStackInSlot(0);
			if(stack!=null && stack.getItem()==Items.sugar){
				if(((TileEntitySPGenerator) tEntity).addSP(50))
					((TileEntitySPGenerator) tEntity).decrStackSize(0, 1);;
			}
			
			//SPの配信
			if(((TileEntitySPGenerator) tEntity).getSP() > 0 && !worldIn.isBlockPowered(pos))
				sendSPAround(Math.min(((TileEntitySPGenerator) tEntity).getSP(), sendingSPperUpdate), worldIn, pos, state, (ISPObject) tEntity);
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
	
	public int sendingSPperUpdate = 300;

	/**
	 * SPを周りに配信
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @return 送信先が見つかったかどうか
	 */
	public static boolean sendSPAround(float value, World worldIn, BlockPos pos, IBlockState state, ISPObject receivedIspObject){
		TileEntity srcEntity = worldIn.getTileEntity(pos);
		if(srcEntity instanceof TileEntitySPObjectBase){
			List<TileEntity> targetTileEntities = new ArrayList<TileEntity>();
			for(int i=0; i<6; i++){
				BlockPos targetPos = pos.add(ConstUtil.XBOUND_LOOP[i], ConstUtil.YBOUND_LOOP[i], ConstUtil.ZBOUND_LOOP[i]);
				TileEntity dstEntity = worldIn.getTileEntity(targetPos);
				if(dstEntity instanceof ISPObject && !dstEntity.equals(receivedIspObject)){
					float amountReceive = ((ISPObject) dstEntity).amountReceiveSPperUpdate((ISPObject) srcEntity);
					if(amountReceive>0) targetTileEntities.add(dstEntity);
				}
			}
			
			if(!targetTileEntities.isEmpty()){
				// SPを配信する。
				float sendingSPperTile = (float)value / (float)targetTileEntities.size();
				for(TileEntity entity : targetTileEntities){
					ISPObject targetObject = (ISPObject) entity;
					float actualAmount = Math.min(sendingSPperTile, targetObject.amountReceiveSPperUpdate((ISPObject) srcEntity));
					boolean flag = ((TileEntitySPObjectBase) srcEntity).sendSPToObject(actualAmount, targetObject);
					if(!flag) ((TileEntitySPObjectBase) srcEntity).addSP(actualAmount);
				}
				return true;
			}
		}
		return false;
	}

}
