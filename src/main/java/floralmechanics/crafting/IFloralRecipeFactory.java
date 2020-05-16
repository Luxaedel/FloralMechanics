package floralmechanics.crafting;

import com.google.gson.JsonObject;

import floralmechanics.crafting.floral.IFloralRecipe;
import net.minecraftforge.common.crafting.JsonContext;

public interface IFloralRecipeFactory {
	IFloralRecipe parse(JsonObject json, JsonContext ctx);
}