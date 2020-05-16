package floralmechanics.crafting.floral;

import floralmechanics.util.Constants;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class FloralRecipeBase extends IForgeRegistryEntry.Impl<IFloralRecipe> implements IFloralRecipe {
	@Override
	public boolean canFit(int width, int height) {
		return width >= Constants.FLORAL_CRAFTING_WIDTH && height >= Constants.FLORAL_CRAFTING_HEIGHT;
	}
}