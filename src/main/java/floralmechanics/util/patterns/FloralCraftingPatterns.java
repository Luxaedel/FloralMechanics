package floralmechanics.util.patterns;

import floralmechanics.init.ModBlocks;

public class FloralCraftingPatterns extends FloralPatterns {
private static FloralCraftingPatterns instance;
	
	public static FloralCraftingPatterns instance() {
        if (instance == null) instance = new FloralCraftingPatterns();
        return instance;
    }
	
	public FloralCraftingPatterns() {
		
	}
	
	@Override
	protected void onRegisterValidPatternBlocks() {
		validPatternBlocks.add(ModBlocks.FLORAL_CRAFTING_TABLE);
	}
}