package net.blacklab.spi.client.gui;

import net.blacklab.spi.SugarPoweredIndustry;
import net.blacklab.spi.container.ContainerSPGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiContainerSPGenerator extends GuiContainer {
	
	public static final ResourceLocation SPGENERATOR_GUI_TEXTURE = new ResourceLocation(SugarPoweredIndustry.MODID, "textures/gui/guispgenerator.png");
	private IInventory playerInventory;
	private IInventory generatorInventory;

	public GuiContainerSPGenerator(IInventory pInventory, IInventory gInventory) {
		super(new ContainerSPGenerator(pInventory, gInventory));
		playerInventory = pInventory;
		generatorInventory = gInventory;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(SPGENERATOR_GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
