package floralmechanics.crafting.floral;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IFloralRecipe extends IForgeRegistryEntry<IFloralRecipe> {
	boolean matches(InventoryCrafting inventory, World world);
	
	default ItemStack getCraftingResult(InventoryCrafting inventory) {
		return getRecipeOutput();
	}
	
	boolean canFit(int width, int height);
	
	ItemStack getRecipeOutput();
	
	default NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventory) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inventory);
	}
	
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.create();
	}
	
	default boolean isShapedRecipe() {
		return false;
	}
	
	default int getWidth() {
		throw new UnsupportedOperationException();
	}
	
	default int getHeight() {
		throw new UnsupportedOperationException();
	}
}