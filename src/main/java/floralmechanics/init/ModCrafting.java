package floralmechanics.init;

import floralmechanics.util.patterns.FloralCraftingPatterns;
import floralmechanics.util.patterns.FloralGeneratorPatterns;

public class ModCrafting {
	public static void init() {
		FloralGeneratorPatterns.instance().register();
		FloralCraftingPatterns.instance().register();
	}
}