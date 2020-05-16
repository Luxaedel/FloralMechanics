package floralmechanics.containers.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSpecific extends SlotItemHandler {
	ItemStack specificStack;
	
	int maxItemsInSlot = 64;
	
	public SlotSpecific(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemStack specificStack) {
		super(itemHandler, index, xPosition, yPosition);
		this.specificStack = specificStack;
	}
	
	public SlotSpecific(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemStack specificStack, int maxItemsInSlot) {
		this(itemHandler, index, xPosition, yPosition, specificStack);
		this.maxItemsInSlot = maxItemsInSlot;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == specificStack.getItem();
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return maxItemsInSlot;
	}
}