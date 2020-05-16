package floralmechanics.containers.main;

import floralmechanics.containers.ContainerBase;
import floralmechanics.containers.slots.SlotFloralCrafting;
import floralmechanics.crafting.floral.FloralCraftingManager;
import floralmechanics.init.ModBlocks;
import floralmechanics.inventories.InventoryFloralCrafting;
import floralmechanics.inventories.InventoryFloralCraftingResult;
import floralmechanics.tileentities.main.TileEntityFloralCraftingTable;
import floralmechanics.util.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerFloralCraftingTable extends ContainerBase {
	private InventoryFloralCrafting craftMatrix;
	private InventoryFloralCraftingResult craftResult;
	protected World worldObj;
	protected BlockPos pos;
	
	public ContainerFloralCraftingTable(InventoryPlayer player, World worldIn, BlockPos pos, TileEntityFloralCraftingTable table) {
		super (player, table, 0, 43);
		this.worldObj = worldIn;
		this.pos = pos;
		this.craftMatrix = new InventoryFloralCrafting(this, table);
		this.craftResult = new InventoryFloralCraftingResult(table);
		addSlotToContainer(new SlotFloralCrafting(player.player, this.craftMatrix, this.craftResult, 0, 148, 60));
		
		for (int y = 0; y < Constants.FLORAL_CRAFTING_HEIGHT; ++y) {
			for (int x = 0; x < Constants.FLORAL_CRAFTING_WIDTH; ++x) {
				addSlotToContainer(new Slot(this.craftMatrix, x + y * Constants.FLORAL_CRAFTING_HEIGHT, 8 + x * 18, 24 + y * 18));
			}
		}
		
		onCraftMatrixChanged(this.craftMatrix);
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory matrix) {
		craftResult.setInventorySlotContents(0, FloralCraftingManager.instance().findMatchingResult(this.craftMatrix, this.worldObj));
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.te.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			
			int matrixSize = Constants.FLORAL_CRAFTING_WIDTH * Constants.FLORAL_CRAFTING_HEIGHT;
			if (slotNumber == 0) {
				if (!mergeItemStack(itemStack1, matrixSize + 1, matrixSize + 1 + 36, true)) return ItemStack.EMPTY;
				slot.onSlotChange(itemStack1, itemStack);
			} else if (slotNumber >= matrixSize + 1 && slotNumber < matrixSize + 1 + 27) {
				if (!mergeItemStack(itemStack1, matrixSize + 1 + 27, matrixSize + 1 + 36, false)) return ItemStack.EMPTY;
			} else if (slotNumber >= matrixSize + 1 + 27 && slotNumber < matrixSize + 1 + 36) {
				if (!mergeItemStack(itemStack1, matrixSize + 1, matrixSize + 1 + 27, false)) return ItemStack.EMPTY;
			} else if (!mergeItemStack(itemStack1, matrixSize + 1, matrixSize + 1 + 36, false)) return ItemStack.EMPTY;
			
			if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();
			
			if (itemStack1.getCount() == itemStack.getCount()) return ItemStack.EMPTY;
			
			slot.onTake(player, itemStack1);
		}
		
		return itemStack;
	}
}