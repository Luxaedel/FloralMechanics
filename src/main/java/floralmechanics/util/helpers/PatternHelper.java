package floralmechanics.util.helpers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import floralmechanics.init.ModBlocks;
import floralmechanics.util.Constants;
import floralmechanics.util.patterns.Pattern;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

public class PatternHelper {
	public static void setFloralPatternFromJson(JsonContext context, JsonObject json, Pattern pattern) {
    	//Pattern floralPattern = new Pattern();
    	
    	Map<Character, ItemStack> inputMap = Maps.newHashMap();
    	JsonObject floralKey = JsonUtils.getJsonObject(json, "floralKey");
        for (Entry<String, JsonElement> entry : floralKey.entrySet()) {
            if (entry.getKey().length() != 1)  throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))  throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            inputMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getItemStack(JsonUtils.getJsonObject(floralKey, entry.getKey()), context));
        }
        
        inputMap.put(' ', ItemStack.EMPTY);
    	
    	String[] floralPatternA = getPattern(json, "floralPattern");
    	Set<Character> keys = new HashSet<>(inputMap.keySet());
        keys.remove(' ');

        int x = 0;
        for (String line : floralPatternA) {
            for (char chr : line.toCharArray()) {
                ItemStack ing = inputMap.get(chr);
                if (ing == null) throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
                pattern.setPatternBlock(x++, ing);
                keys.remove(chr);
            }
        }

        if (!keys.isEmpty()) throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
    	
    	//return pattern;
    }
	
	public static String[] getPattern(JsonObject json, String key) {
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