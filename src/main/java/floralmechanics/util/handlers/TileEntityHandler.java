package floralmechanics.util.handlers;

import floralmechanics.tileentities.generators.TileEntityFloralGenerator;
import floralmechanics.tileentities.main.TileEntityFloralCraftingTable;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {
	public static void registerTileEntities() {
		//Register tile entities here
		GameRegistry.registerTileEntity(TileEntityFloralGenerator.class, "floral_generator");
		GameRegistry.registerTileEntity(TileEntityFloralCraftingTable.class, "floral_crafting_table");
	}
}