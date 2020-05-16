package floralmechanics.util.patterns;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class FloralPatterns {
	private static FloralPatterns instance;
	protected static ArrayList<Block> validPatternBlocks = new ArrayList<Block>();
	
	public static FloralPatterns instance() {
        if (instance == null) instance = new FloralPatterns();
        return instance;
    }
	
	public FloralPatterns() {
		
	}
	
	public void register() {
		registerValidPatternBlocks();
	}
	
	private void registerValidPatternBlocks() {
		validPatternBlocks.add(Blocks.RED_FLOWER);
		validPatternBlocks.add(Blocks.YELLOW_FLOWER);
		onRegisterValidPatternBlocks();
	}
	
	protected void onRegisterValidPatternBlocks() {
		//Stub
	}
	
	public boolean isValidPatternBlock(Block block) {
		return validPatternBlocks.contains(block);
	}
}