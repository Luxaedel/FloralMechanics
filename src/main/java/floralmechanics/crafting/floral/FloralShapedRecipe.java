package floralmechanics.crafting.floral;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import floralmechanics.init.ModBlocks;
import floralmechanics.inventories.InventoryFloralCrafting;
import floralmechanics.util.Constants;
import floralmechanics.util.patterns.Pattern;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.JsonContext;

public class FloralShapedRecipe extends FloralRecipeBase {
	protected int width;
	protected int height;
	protected NonNullList<Ingredient> input = null;
	protected Pattern floralPattern;
	protected ItemStack output;
	protected boolean mirrored = true;
	
	public FloralShapedRecipe(@Nonnull ItemStack result, ShapedPrimer primer, Pattern floralPattern) {
		this.output = result.copy();
		this.width = primer.width;
		this.height = primer.height;
		this.input = primer.input;
		this.mirrored = primer.mirrored;
		this.floralPattern = floralPattern;
	}
	
	public ItemStack getRecipeOutput() {
		return this.output.copy();
	}
	
	public boolean matches(InventoryCrafting matrix, World world) {
		boolean craftPattMatch = false;
		
		for (int i = 0; i <= Constants.FLORAL_CRAFTING_WIDTH - this.width; i++) {
			for (int j = 0; j <= Constants.FLORAL_CRAFTING_HEIGHT - this.height; ++j) {
				if (this.mirrored && this.checkMatch(matrix, i, j, true)) craftPattMatch = true;
				if (this.checkMatch(matrix, i, j, false)) craftPattMatch = true;
			}
		}
		
		InventoryFloralCrafting floralMatrix = (InventoryFloralCrafting)matrix;
		if (craftPattMatch) {
			craftPattMatch = this.floralPattern.patternMatches(floralMatrix.getSurroundingBlocks());
			return craftPattMatch;
		}
		return false;
	}
	
	protected boolean checkMatch(InventoryCrafting matrix, int startX, int startY, boolean mirrored) {
		for (int x = 0; x < Constants.FLORAL_CRAFTING_WIDTH; x++) {
			for (int y = 0; y < Constants.FLORAL_CRAFTING_HEIGHT; y++) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;
				
				if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
					if (mirrored) target = this.input.get(this.width - subX - 1 + subY * this.width);
					else target = this.input.get(subX + subY * this.width);
				}
				
				if (!target.apply(matrix.getStackInRowAndColumn(x, y))) return false;
			}
		}
		
		return true;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.input;
	}
	
	public void setMirrored(boolean mirrored) {
        this.mirrored = mirrored;
    }

    @Override
    public boolean isShapedRecipe() {
        return true;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
    
    public static FloralShapedRecipe fromJson(JsonContext context, JsonObject json) {
    	Map<Character, Ingredient> ingMap = Maps.newHashMap();
        for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet()) {
            if (entry.getKey().length() != 1)  throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))  throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
        }
        
        ingMap.put(' ', Ingredient.EMPTY);
        
        String[] pattern = getPattern(json, "pattern");
        
        ShapedPrimer primer = new ShapedPrimer();
        primer.width = pattern[0].length();
        primer.height = pattern.length;
        primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);
        
        Set<Character> keys = new HashSet<>(ingMap.keySet());
        keys.remove(' ');

        int x = 0;
        for (String line : pattern) {
            for (char chr : line.toCharArray()) {
                Ingredient ing = ingMap.get(chr);
                if (ing == null) throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
                primer.input.set(x++, ing);
                keys.remove(chr);
            }
        }

        if (!keys.isEmpty()) throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);

        ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
        Pattern floralPattern = getFloralPatternFromJson(context, json);
        return new FloralShapedRecipe(result, primer, floralPattern);
    }
    
    private static Pattern getFloralPatternFromJson(JsonContext context, JsonObject json) {
    	Pattern floralPattern = new Pattern();
    	
    	Map<Character, ItemStack> inputMap = Maps.newHashMap();
    	JsonObject floralKey = JsonUtils.getJsonObject(json, "floralKey");
        for (Entry<String, JsonElement> entry : floralKey.entrySet()) {
            if (entry.getKey().length() != 1)  throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))  throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            inputMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getItemStack(JsonUtils.getJsonObject(floralKey, entry.getKey()), context));
        }
        
        inputMap.put(' ', ItemStack.EMPTY);
    	
    	String[] floralPatternA = getPattern(json, "floralPattern");
    	
    	int center = (((Constants.FLORAL_CRAFTING_WIDTH * Constants.FLORAL_CRAFTING_HEIGHT) - 1) / 2);
    	
    	Set<Character> keys = new HashSet<>(inputMap.keySet());
        keys.remove(' ');

        int x = 0;
        floralPattern.setMainPatternBlock(center, new ItemStack(ModBlocks.FLORAL_CRAFTING_TABLE));
        for (String line : floralPatternA) {
            for (char chr : line.toCharArray()) {
                ItemStack ing = inputMap.get(chr);
                if (ing == null) throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
                floralPattern.setPatternBlock(x++, ing);
                keys.remove(chr);
            }
        }

        if (!keys.isEmpty()) throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
    	
    	return floralPattern;
    }
    
    private static String[] getPattern(JsonObject json, String key) {
    	JsonArray patternJ = JsonUtils.getJsonArray(json, key);
    	if (patternJ.size() == 0) throw new JsonSyntaxException("Invalid floral pattern: empty floral pattern not allowed");
    	
    	String[] pattern = new String[patternJ.size()];
        for (int x = 0; x < pattern.length; ++x) {
            String line = JsonUtils.getString(patternJ.get(x), key + "[" + x + "]");
            if (x > 0 && pattern[0].length() != line.length())  throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
            pattern[x] = line;
        }
        
        return pattern;
    }
}