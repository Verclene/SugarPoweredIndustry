package net.blacklab.spi.block;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.api.ISPObject;
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
			if(!worldIn.isBlockPowered(pos)) sendSPAround(worldIn, pos, state);
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
	
	public static int maxSendingSP = 300;

	private static final int XBOUND_LOOP[] = new int[]{-1, 0, 1, 0, 0, 0};
	private static final int YBOUND_LOOP[] = new int[]{ 0, 0, 0, 0,-1, 1};
	private static final int ZBOUND_LOOP[] = new int[]{ 0,-1, 0, 1, 0, 0};
	/**
	 * SPを周りに配信
	 * @param worldIn
	 * @param pos
	 * @param state
	 */
	public void sendSPAround(World worldIn, BlockPos pos, IBlockState state){
		TileEntity srcEntity = worldIn.getTileEntity(pos);
		if(srcEntity instanceof TileEntitySPGenerator){
			Map<TileEntity, Integer> targetTileEntities = new HashMap<TileEntity, Integer>();
			for(int i=0; i<6; i++){
				BlockPos targetPos = pos.add(XBOUND_LOOP[i], YBOUND_LOOP[i], ZBOUND_LOOP[i]);
				TileEntity dstEntity = worldIn.getTileEntity(targetPos);
				if(dstEntity instanceof ISPObject){
					int amountReceive = ((ISPObject) dstEntity).amountReceiveSPperUpdate((ISPObject) srcEntity);
					if(amountReceive>0) targetTileEntities.put(dstEntity, amountReceive);
				}
			}
			
			if(!targetTileEntities.isEmpty()){
				//SPを分配して配信
				int storedSP = ((TileEntitySPGenerator) srcEntity).getSP();
				int expectedTotalSendingSP = 0;
				for(Integer i:targetTileEntities.values()) expectedTotalSendingSP += i;
				double sendingRatio = 1.0D;
				if(expectedTotalSendingSP > maxSendingSP){
					sendingRatio = (double)maxSendingSP / (double)expectedTotalSendingSP;
					if(storedSP < maxSendingSP) sendingRatio *= (double)storedSP / (double)maxSendingSP;
				}else{
					if(storedSP < expectedTotalSendingSP) sendingRatio *= (double)storedSP / (double)expectedTotalSendingSP;
				}
				Iterator iterator = targetTileEntities.entrySet().iterator();
				while(iterator.hasNext()){
					Entry entry = (Entry) iterator.next();
					// カッコマシ☆モノグサキャストオオメ
					((ISPObject) srcEntity).sendSPToObject((int)(((Integer)entry.getValue()).doubleValue() * sendingRatio), ((ISPObject)entry.getKey()));
				}
			}
		}
	}

}
