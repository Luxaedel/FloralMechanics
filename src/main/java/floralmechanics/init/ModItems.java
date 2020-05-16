package floralmechanics.init;

import java.util.ArrayList;
import java.util.List;

import floralmechanics.blocks.flowers.BlockAustralea;
import floralmechanics.items.naturenuggets.ItemEnvironmentalExtractorMinor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class ModItems {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	
	//Environmental extractors
	public static final Item ENVIRONMENTAL_EXTRACTOR_MINOR = new ItemEnvironmentalExtractorMinor("environmental_extractor_minor");
}