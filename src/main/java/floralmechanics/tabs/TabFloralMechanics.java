package floralmechanics.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TabFloralMechanics extends CreativeTabs {
	public TabFloralMechanics(String label) {
		super("floralmechanicstab");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(Items.CHORUS_FRUIT_POPPED);
	}
	
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> itemStacks) {
		super.displayAllRelevantItems(itemStacks);
	}
}