package floralmechanics.containers.generators;

import floralmechanics.containers.ContainerBase;
import floralmechanics.tileentities.generators.TileEntityGeneratorBase;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerGeneratorBase extends ContainerBase {
	public ContainerGeneratorBase(InventoryPlayer playerInv, TileEntityGeneratorBase te) {
		super(playerInv, te);
	}
}