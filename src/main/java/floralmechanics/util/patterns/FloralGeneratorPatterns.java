package floralmechanics.util.patterns;

import floralmechanics.init.ModBlocks;

public class FloralGeneratorPatterns extends FloralPatterns {
	private static FloralGeneratorPatterns instance;
	
	public static FloralGeneratorPatterns instance() {
        if (instance == null) instance = new FloralGeneratorPatterns();
        return instance;
    }
	
	public FloralGeneratorPatterns() {
		
	}
	
	@Override
	protected void onRegisterValidPatternBlocks() {
		validPatternBlocks.add(ModBlocks.FLORAL_GENERATOR);
	}
}