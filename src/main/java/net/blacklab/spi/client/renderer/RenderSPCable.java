package net.blacklab.spi.client.renderer;

import net.blacklab.spi.block.BlockSPCable;
import net.blacklab.spi.client.model.ModelCable;
import net.blacklab.spi.tile.TileEntitySPCable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderSPCable extends TileEntitySpecialRenderer {
	
	private ModelCable modelCable = new ModelCable(); 
	private static final ResourceLocation CABLE_RESOURCE_LOCATION = new ResourceLocation("vespi","textures/model/spcable.png");
	private static final ResourceLocation CABLE_RESOURCE_LOCATION_S = new ResourceLocation("vespi","textures/model/spcable_s.png");

	public RenderSPCable() {
		super();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX,
			double posY, double posZ, float p_180535_8_, int p_180535_9_) {
		if(tileEntity instanceof TileEntitySPCable){
			p_180535_8_ = 0.0625F;
			GlStateManager.pushMatrix();
			if(((TileEntitySPCable) tileEntity).isSending){
				bindTexture(CABLE_RESOURCE_LOCATION_S);
			}else {
				bindTexture(CABLE_RESOURCE_LOCATION);
			}

			GlStateManager.translate((float)posX+0.5F, (float)posY-0.5F, (float)posZ+0.5F);

			modelCable.centerCube.render(p_180535_8_);
			
			int connection = BlockSPCable.getConnectedSide(getWorld(), tileEntity.getPos());
			if((connection & 1<<5) == 1<<5){
				modelCable.northCable.render(p_180535_8_);
			}
			if((connection & 1<<4) == 1<<4){
				modelCable.eastCable.render(p_180535_8_);
			}
			if((connection & 1<<3) == 1<<3){
				modelCable.southCable.render(p_180535_8_);
			}
			if((connection & 1<<2) == 1<<2){
				modelCable.westCable.render(p_180535_8_);
			}
			if((connection & 1<<1) == 1<<1){
				modelCable.downCable.render(p_180535_8_);
			}
			if((connection & 1) == 1){
				modelCable.upCable.render(p_180535_8_);
			}
			
			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, true);
		}
	}

}
