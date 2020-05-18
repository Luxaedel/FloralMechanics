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
import floralmechanics.util.helpers.PatternHelper;
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
    	return CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/patterns/floralgeneratorpatterns", this::preprocess, this::process, true, true);
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
    	FloralGeneratorPattern floralPattern = new FloralGeneratorPattern(JsonUtils.getInt(json, "rftick"));
    	PatternHelper.setFloralPatternFromJson(context, json, floralPattern);
    	return floralPattern;
    }
    
    public int getRFTickFromPattern(NonNullList<ItemStack> blockList) {
		for (FloralGeneratorPattern pattern : FLORAL_GENERATOR_PATTERNS) {
			if(pattern.patternMatches(blockList)) return pattern.rfTick;
		}
		
		return 0;
	}
}