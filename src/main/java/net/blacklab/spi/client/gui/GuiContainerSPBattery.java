package net.blacklab.spi.client.gui;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.api.ISPObject;
import net.blacklab.spi.container.ContainerSPGenerator;
import net.blacklab.spi.tile.TileEntitySPGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiContainerSPBattery extends GuiContainer {
	public static final ResourceLocation SPBATTERY_GUI_TEXTURE = new ResourceLocation(SugarPoweredIndustry.MODID, "textures/gui/guispgenerator.png");
	private IInventory playerInventory;
	private IInventory batteryInventory;

	public GuiContainerSPBattery(EntityPlayer player, IInventory pInventory, IInventory gInventory) {
		super(new ContainerSPGenerator(player, pInventory, gInventory));
		playerInventory = pInventory;
		batteryInventory = gInventory;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(SPBATTERY_GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = batteryInventory.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

		if(batteryInventory instanceof TileEntitySPGenerator){
			String ssp = String.format("SP:  %4.0f", ((ISPObject)batteryInventory).getSP());
			String sspm = String.format("(Max: %4.0f)", ((ISPObject)batteryInventory).getMaxSP());
			fontRendererObj.drawString(ssp, 98, 44-fontRendererObj.FONT_HEIGHT / 2, 4210752);
			fontRendererObj.drawString(sspm, 98, 44+fontRendererObj.FONT_HEIGHT / 2, 4210752);
		}
	}
}
