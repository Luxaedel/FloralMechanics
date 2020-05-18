package floralmechanics.recipes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import floralmechanics.util.Constants;
import floralmechanics.util.helpers.PatternHelper;
import floralmechanics.util.patterns.FloralGeneratorPattern;
import floralmechanics.util.patterns.FloralInfusionPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class FloralInfusionManager {
	private static FloralInfusionManager instance;
	
	private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public static final List<FloralInfusionPattern> FLORAL_INFUSION_PATTERNS = new ArrayList<FloralInfusionPattern>();
	
	private final ModContainer mod;
	private final JsonContext ctx;
	
	public static FloralInfusionManager instance() {
        if (instance == null) instance = new FloralInfusionManager();
        return instance;
    }
	
	private FloralInfusionManager() {
		this.mod = Loader.instance().getIndexedModList().get(Constants.MODID);
    	this.ctx = new JsonContext(Constants.MODID);
	}
    
    public void init() {
    	parseJsonRecipes();
    }
    
    private boolean parseJsonRecipes() {
    	return CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/patterns/floralinfusionpatterns", this::preprocess, this::process, true, true);
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
    	
    	FloralInfusionPattern pattern = this.fromJson(this.ctx, json);
    	if (pattern == null) Constants.LOGGER.error("Factory parsed a null recipe!");
    	else FLORAL_INFUSION_PATTERNS.add(pattern);
    }
    
    private FloralInfusionPattern fromJson(JsonContext context, JsonObject json) {
    	FloralInfusionPattern floralPattern = new FloralInfusionPattern();
    	PatternHelper.setFloralPatternFromJson(context, json, floralPattern);
    	return floralPattern;
    }
}