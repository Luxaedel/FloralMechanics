package floralmechanics.inventories;

import floralmechanics.tileentities.main.TileEntityFloralCraftingTable;
import floralmechanics.util.Constants;
import floralmechanics.util.patterns.Pattern;
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryFloralCrafting extends InventoryCrafting {
	private TileEntityFloralCraftingTable table;
	private Container container;
	
	public InventoryFloralCrafting(Container container, TileEntityFloralCraftingTable table) {
		super(container, Constants.FLORAL_CRAFTING_WIDTH, Constants.FLORAL_CRAFTING_HEIGHT);
		this.container = container;
		this.table = table;
	}
	
	@Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= getSizeInventory() ? ItemStack.EMPTY : this.table.getStackInSlot(slot + 1);
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < Constants.FLORAL_CRAFTING_WIDTH) {
            int x = row + column * Constants.FLORAL_CRAFTING_HEIGHT;
            return getStackInSlot(x);
        } else  return ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        ItemStack stack = this.table.getStackInSlot(slot + 1);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= decrement) {
                itemstack = stack.copy();
                this.table.setInventorySlotContents(slot + 1, ItemStack.EMPTY);
                container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = stack.splitStack(decrement);
                if (stack.getCount() == 0) {
                	this.table.setInventorySlotContents(slot + 1, ItemStack.EMPTY);
                }
                container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
    	this.table.setInventorySlotContents(slot + 1, itemstack);
        container.onCraftMatrixChanged(this);
    }
    
    public NonNullList<ItemStack> getSurroundingBlocks() {
    	return this.table.getSurroundingBlocks();
    }
}