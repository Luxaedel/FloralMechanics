package floralmechanics.util.handlers;

import floralmechanics.FloralMechanics;
import floralmechanics.crafting.floral.FloralCraftingManager;
import floralmechanics.init.ModBlocks;
import floralmechanics.init.ModCrafting;
import floralmechanics.init.ModItems;
import floralmechanics.init.ModRecipes;
import floralmechanics.recipes.FloralGeneratorManager;
import floralmechanics.util.Constants;
import floralmechanics.world.ModWorldGen;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class RegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
		TileEntityHandler.registerTileEntities();
	}
	
	public static void preInitRegistries() {
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
		RenderHandler.registerCustomMeshesAndStates();
		//FloralCraftingManager.instance().init();
		ModCrafting.init();
	}
	
	public static void initRegistries() {
		ModRecipes.init();
		FloralCraftingManager.instance().init();
		FloralGeneratorManager.instance().init();
		NetworkRegistry.INSTANCE.registerGuiHandler(FloralMechanics.instance, new GuiHandler());
	}
	
	public static void postInitRegistries() {
		
	}
	
//	@SubscribeEvent
//	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
//		FloralCraftingManager.instance().init();
//	}
}
