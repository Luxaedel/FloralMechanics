package floralmechanics.guis.generators;

import floralmechanics.containers.generators.ContainerFloralGenerator;
import floralmechanics.tileentities.generators.TileEntityFloralGenerator;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFloralGenerator extends GuiGeneratorBase {
	public GuiFloralGenerator (InventoryPlayer playerInv, TileEntityFloralGenerator te) {
		super (playerInv, te, new ContainerFloralGenerator(playerInv, te), "floral/floral_generator");
	}
}