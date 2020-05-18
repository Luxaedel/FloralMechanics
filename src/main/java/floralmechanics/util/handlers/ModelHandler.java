package floralmechanics.util.handlers;

import floralmechanics.init.ModBlocks;
import floralmechanics.init.ModItems;
import floralmechanics.util.Constants;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value=Side.CLIENT, modid=Constants.MODID)
public class ModelHandler {
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		//Items
		ModelLoader.setCustomModelResourceLocation(ModItems.ENVIRONMENTAL_EXTRACTOR_MINOR, 0,
                new ModelResourceLocation(ModItems.ENVIRONMENTAL_EXTRACTOR_MINOR.getRegistryName(), "inventory"));
		
		//Blocks
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.FLORAL_GENERATOR), 0, 
				new ModelResourceLocation(Item.getItemFromBlock(ModBlocks.FLORAL_GENERATOR).getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.FLORAL_CRAFTING_TABLE), 0, 
				new ModelResourceLocation(Item.getItemFromBlock(ModBlocks.FLORAL_CRAFTING_TABLE).getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.AUSTRALEA), 0, 
				new ModelResourceLocation(Item.getItemFromBlock(ModBlocks.AUSTRALEA).getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.FLORAL_INFUSER), 0, 
				new ModelResourceLocation(Item.getItemFromBlock(ModBlocks.FLORAL_INFUSER).getRegistryName(), "inventory"));
	}
}