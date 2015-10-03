package net.blacklab.spi.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerSPGenerator extends Container {
	
	private IInventory generatorInventory;

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO 自動生成されたメソッド・スタブ
		return generatorInventory.isUseableByPlayer(playerIn);
	}

}
