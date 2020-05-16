package floralmechanics.init;

import java.util.ArrayList;
import java.util.List;

import floralmechanics.blocks.generators.BlockFloralGenerator;
import floralmechanics.blocks.main.BlockFloralCraftingTable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	//Blocks
	public static final Block FLORAL_CRAFTING_TABLE = new BlockFloralCraftingTable("floral_crafting_table", Material.WOOD);
	
	//Tile entities
	public static final Block FLORAL_GENERATOR = new BlockFloralGenerator("floral_generator", Material.IRON);
}