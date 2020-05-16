package floralmechanics.crafting.floral;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import floralmechanics.crafting.IFloralRecipeFactory;
import floralmechanics.util.Constants;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.JsonContext;

public class FloralRecipeHandler implements IFloralRecipeFactory {
	@Override
	public IFloralRecipe parse(JsonObject json, JsonContext ctx) {
		String type = JsonUtils.getString(json, "type");
		if (type.contains("shaped")) return FloralShapedRecipe.fromJson(ctx, json);
		else throw new JsonSyntaxException( "Floral Mechanics was given a custom recipe that it does not know how to handle!\n" + "Type should be '" + Constants.MODID + ":shaped', got '" + type + "'!" );
	}
}