package floralmechanics.crafting.floral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import floralmechanics.crafting.IFloralRecipeFactory;
import floralmechanics.util.Constants;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class FloralCraftingManager {
	private static FloralCraftingManager instance;
	
	private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public static final List<IFloralRecipe> FLORAL_RECIPES = new ArrayList<IFloralRecipe>();
	private final Map<ResourceLocation, IFloralRecipeFactory> factories = new HashMap<>();
	
	private final ModContainer mod;
	private final JsonContext ctx;
	
	public static FloralCraftingManager instance() {
        if (instance == null) instance = new FloralCraftingManager();
        return instance;
    }
	
	private FloralCraftingManager() {
		this.mod = Loader.instance().getIndexedModList().get(Constants.MODID);
    	this.ctx = new JsonContext(Constants.MODID);
    	
    	this.initFactories();
	}
    
    public void init() {
    	parseJsonRecipes();
    }
    
    private boolean parseJsonRecipes() {
    	return CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/floralrecipes", this::preprocess, this::process, true, true);
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
    
    private void initFactories() {
    	this.factories.put(new ResourceLocation(Constants.MODID, "shaped"), new FloralRecipeHandler());
    }
    
    private void register (JsonObject json) {
    	if (json == null || json.isJsonNull()) throw new JsonSyntaxException( "Json cannot be null" );
    	
    	String type = this.ctx.appendModId(JsonUtils.getString(json, "type"));
    	if (type.isEmpty()) throw new JsonSyntaxException( "Recipe type can not be an empty string" );
    	
    	IFloralRecipeFactory factory = this.factories.get(new ResourceLocation(type));
    	if (factory == null) throw new JsonSyntaxException( "Unknown recipe type: " + type );
    	
    	IFloralRecipe recipe = factory.parse(json, this.ctx);
    	if (recipe == null) Constants.LOGGER.error("Factory parsed a null recipe!");
    	else FLORAL_RECIPES.add(recipe);
    }
    
    public ItemStack findMatchingResult (InventoryCrafting matrix, World worldIn) {
    	for (IFloralRecipe recipe : FLORAL_RECIPES) {
    		if (recipe.matches(matrix, worldIn)) return recipe.getCraftingResult(matrix);
    	}
    	return ItemStack.EMPTY;
    }
    
    @Nullable
    public IFloralRecipe findMatchingRecipe (InventoryCrafting matrix, World worldIn) {
    	for (IFloralRecipe recipe : FLORAL_RECIPES) {
    		if (recipe.matches(matrix, worldIn)) return recipe;
    	}
    	return null;
    }
    
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting matrix, World worldIn) {
    	for (IFloralRecipe recipe : FLORAL_RECIPES) {
    		if (recipe.matches(matrix, worldIn)) return recipe.getRemainingItems(matrix);
    	}
    	
    	NonNullList<ItemStack> nonNullList = NonNullList.<ItemStack>withSize(matrix.getSizeInventory(), ItemStack.EMPTY);
    	
    	for (int i = 0; i < nonNullList.size(); i++) {
    		nonNullList.set(i, matrix.getStackInSlot(i));
    	}
    	
    	return nonNullList;
    }
}