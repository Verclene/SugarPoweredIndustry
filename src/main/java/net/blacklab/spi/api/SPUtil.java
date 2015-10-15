package net.blacklab.spi.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * SP関連の操作を簡単に行うためのユーティリティクラス
 *
 */
public class SPUtil {

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
