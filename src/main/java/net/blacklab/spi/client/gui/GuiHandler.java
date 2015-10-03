package net.blacklab.spi.client.gui;

import net.blacklab.spi.container.ContainerSPGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int GUI_ID_SPGENERATOR = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		Object object = null;
		switch (ID) {
		case GUI_ID_SPGENERATOR:
			object = new ContainerSPGenerator(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
			break;

		default:
			break;
		}
		return object;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		Object object = null;
		switch (ID) {
		case GUI_ID_SPGENERATOR:
			object = new GuiContainerSPGenerator(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
			break;

		default:
			break;
		}
		return object;
	}

}
