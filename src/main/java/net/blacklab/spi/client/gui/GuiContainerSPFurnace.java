package net.blacklab.spi.client.gui;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.container.ContainerSPFurnace;
import net.blacklab.spi.container.ContainerSPGenerator;
import net.blacklab.spi.tile.TileEntitySPFurnace;
import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiContainerSPFurnace extends GuiContainer {
	
	public static final ResourceLocation SPFURNACE_GUI_TEXTURE = new ResourceLocation(SugarPoweredIndustry.MODID, "textures/gui/guispfurnace.png");
	private IInventory playerInventory;
	private IInventory furnaceInventory;

	public GuiContainerSPFurnace(EntityPlayer player, IInventory pInventory, IInventory gInventory) {
		super(new ContainerSPFurnace(player, pInventory, gInventory));
		playerInventory = pInventory;
		furnaceInventory = gInventory;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(SPFURNACE_GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		String s = furnaceInventory.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
//		fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

		if(furnaceInventory instanceof TileEntitySPFurnace){
			float sp = ((TileEntitySPFurnace) furnaceInventory).getSP();
			if(sp>0){
				// SP入力あり
				GlStateManager.pushMatrix();
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				mc.getTextureManager().bindTexture(SPFURNACE_GUI_TEXTURE);
				drawTexturedModalRect(39, 35, 176, 0, 14, 14);
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
			String ssp = String.format("SP:  %4.0f", sp);
			String sspm = String.format("(Max: %4.0f)", ((ISPObject)furnaceInventory).getMaxSP());
			fontRendererObj.drawString(ssp, 38, 65-fontRendererObj.FONT_HEIGHT / 2, 4210752);
			fontRendererObj.drawString(sspm, 38, 65+fontRendererObj.FONT_HEIGHT / 2, 4210752);
		}
	}

}
