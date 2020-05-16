package floralmechanics.inventories;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryBase extends ItemStackHandler {
	public InventoryBase(int size) {
		this.stacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (isItemValid(slot, stack)) {
			return super.insertItem(slot, stack, simulate);
		}
		return stack;
	}
}