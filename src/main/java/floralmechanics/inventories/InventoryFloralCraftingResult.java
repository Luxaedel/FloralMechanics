package floralmechanics.inventories;

import floralmechanics.tileentities.main.TileEntityFloralCraftingTable;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;

public class InventoryFloralCraftingResult extends InventoryCraftResult {
	private TileEntityFloralCraftingTable table;
	
	public InventoryFloralCraftingResult(TileEntityFloralCraftingTable table) {
		this.table = table;
	}
	
	@Override
    public ItemStack getStackInSlot(int par1) {
        return this.table.getStackInSlot(0);
    }
	
	@Override
    public ItemStack decrStackSize(int par1, int par2) {
        ItemStack stack = this.table.getStackInSlot(0);
        if (!stack.isEmpty()) {
        	this.table.setInventorySlotContents(0, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
    	this.table.setInventorySlotContents(0, par2ItemStack);
    }
}