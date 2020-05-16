package floralmechanics.util.patterns;

import floralmechanics.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Pattern {
	private NonNullList<ItemStack> patternList;
	
	public Pattern () {
		this.patternList = NonNullList.<ItemStack>withSize((Constants.MAX_FLORAL_WIDTH * Constants.MAX_FLORAL_DEPTH), ItemStack.EMPTY);
	}
	
	public void setPatternBlock(int index, ItemStack block) {
		this.patternList.set(index, block);
	}
	
	public boolean patternMatches(NonNullList<ItemStack> blockList) {
		for (int i = 0; i < blockList.size(); i++) {
			ItemStack itemStack = blockList.get(i);
			ItemStack patternStack = this.patternList.get(i);
			if (!(ItemStack.areItemStacksEqual(itemStack, patternStack) && ItemStack.areItemStackTagsEqual(itemStack, patternStack))) return false;
		}
		return true;
		
		//return this.patternList.equals(blockList);
	}
	
	public void setMainPatternBlock(int focalPoint, ItemStack block) {
		setPatternBlock(focalPoint, block);
	}
}