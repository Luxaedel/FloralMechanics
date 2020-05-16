package floralmechanics.containers.generators;

import floralmechanics.tileentities.generators.TileEntityFloralGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.IItemHandler;

public class ContainerFloralGenerator extends ContainerGeneratorBase {
	IItemHandler handler = null;
	
	public ContainerFloralGenerator(InventoryPlayer playerInv, TileEntityFloralGenerator te) {
		super(playerInv, te);
	}
}