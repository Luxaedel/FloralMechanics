package floralmechanics.recipes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import floralmechanics.init.ModBlocks;
import floralmechanics.util.Constants;
import floralmechanics.util.patterns.FloralGeneratorPattern;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class FloralGeneratorManager {
	private static FloralGeneratorManager instance;
	
	private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public static final List<FloralGeneratorPattern> FLORAL_GENERATOR_PATTERNS = new ArrayList<FloralGeneratorPattern>();
	
	private final ModContainer mod;
	private final JsonContext ctx;
	
	public static FloralGeneratorManager instance() {
        if (instance == null) instance = new FloralGeneratorManager();
        return instance;
    }
	
	private FloralGeneratorManager() {
		this.mod = Loader.instance().getIndexedModList().get(Constants.MODID);
    	this.ctx = new JsonContext(Constants.MODID);
	}
    
    public void init() {
    	parseJsonRecipes();
    }
    
    private boolean parseJsonRecipes() {
    	return CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/floralgeneratorpatterns", this::preprocess, this::process, true, true);
    }
    
    private boolean preprocess(final Path root) {
    	return true;
    }
    
    private boolean process(final Path root, final Path file) {
    	String relative = root.relativize(file).toString();
    	if (!"json".contentEquals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_")) return true;
    	
    	String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
    	ResourceLocation resourceLocation = new ResourceLocation(Constants.MODID, name);
		BufferedReader bufferedReader = null;
		
		try {
			try {
				bufferedReader = Files.newBufferedReader(file);
				JsonObject json = JsonUtils.fromJson(GSON, bufferedReader, JsonObject.class);
				if (json.has("conditions") && !CraftingHelper.processConditions(JsonUtils.getJsonArray(json, "conditions"), this.ctx)) return true;
                //this.register(name, parseRecipeJson(this.ctx, (JsonObject)JsonUtils.fromJson(GSON, bufferedReader, JsonObject.class)));
                this.register(json);
			} catch (JsonParseException jsonparseexception) {
                Constants.LOGGER.error("Parsing error loading recipe " + resourceLocation, (Throwable)jsonparseexception);
                return false;
            } catch (IOException ioexception) {
            	Constants.LOGGER.error("Couldn't read recipe " + resourceLocation + " from " + file, (Throwable)ioexception);
                return false;
            }
		} finally {
			IOUtils.closeQuietly((Reader)bufferedReader);
		}
		
		return true;
    }
    
    private void register (JsonObject json) {
    	if (json == null || json.isJsonNull()) throw new JsonSyntaxException( "Json cannot be null" );
    	
    	String type = this.ctx.appendModId(JsonUtils.getString(json, "type"));
    	if (type.isEmpty()) throw new JsonSyntaxException( "Recipe type can not be an empty string" );
    	
    	FloralGeneratorPattern pattern = this.fromJson(this.ctx, json);
    	if (pattern == null) Constants.LOGGER.error("Factory parsed a null recipe!");
    	else FLORAL_GENERATOR_PATTERNS.add(pattern);
    }
    
    private FloralGeneratorPattern fromJson(JsonContext context, JsonObject json) {
    	int rfTick = JsonUtils.getInt(json, "rftick");
    	FloralGeneratorPattern floralPattern = new FloralGeneratorPattern(rfTick);
    	
    	Map<Character, ItemStack> inputMap = Maps.newHashMap();
    	JsonObject floralKey = JsonUtils.getJsonObject(json, "floralKey");
        for (Entry<String, JsonElement> entry : floralKey.entrySet()) {
            if (entry.getKey().length() != 1)  throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))  throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            inputMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getItemStack(JsonUtils.getJsonObject(floralKey, entry.getKey()), context));
        }
        
        inputMap.put(' ', ItemStack.EMPTY);
    	
        JsonArray patternJ = JsonUtils.getJsonArray(json, "floralPattern");
    	if (patternJ.size() == 0) throw new JsonSyntaxException("Invalid floral pattern: empty floral pattern not allowed");
    	
    	String[] pattern = new String[patternJ.size()];
        for (int x = 0; x < pattern.length; ++x) {
            String line = JsonUtils.getString(patternJ.get(x), "floralPattern[" + x + "]");
            //Is this first check necessary? May want to allow users to have uneven patterns! --Would affect 'center' as well
            if (x > 0 && pattern[0].length() != line.length())  throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
            else if (x > 0 && (pattern[0].length() > Constants.MAX_FLORAL_WIDTH || line.length() > Constants.MAX_FLORAL_DEPTH)) throw new JsonSyntaxException("Invalid pattern: Width may not exceed: " + Constants.MAX_FLORAL_WIDTH + " and height may not exceed: " + Constants.MAX_FLORAL_DEPTH + ".");
            pattern[x] = line;
        }
    	
    	int center = (((Constants.MAX_FLORAL_WIDTH * Constants.MAX_FLORAL_DEPTH) - 1) / 2);
    	
    	Set<Character> keys = new HashSet<>(inputMap.keySet());
        keys.remove(' ');

        int x = 0;
        for (String line : pattern) {
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
    
    public int getRFTickFromPattern(NonNullList<ItemStack> blockList) {
		for (FloralGeneratorPattern pattern : FLORAL_GENERATOR_PATTERNS) {
			if(pattern.patternMatches(blockList)) return pattern.rfTick;
		}
		
		return 0;
	}
}