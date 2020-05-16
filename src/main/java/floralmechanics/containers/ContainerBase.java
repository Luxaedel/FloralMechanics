package floralmechanics.containers;

import floralmechanics.tileentities.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {
	protected TileEntityBase te;
	
	protected int slotCount;
	
	public ContainerBase(InventoryPlayer playerInv, TileEntityBase te, int playerInvOffX, int playerInvOffY) {
		this.te = te;
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + playerInvOffX + x * 18, 84 + playerInvOffY + y * 18));
			}
		}
		
		for (int x = 0; x < 9; ++x) {
			this.addSlotToContainer(new Slot(playerInv, x, 8 + playerInvOffX + x * 18, 142 + playerInvOffY));
		}
	}
	
	public ContainerBase(InventoryPlayer playerInv, TileEntityBase te) {
		this(playerInv, te, 0, 0);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.te.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(fromSlot);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack = slot.getStack();
			previous = itemStack.copy();
			
			if (fromSlot < 36) {
				if (!this.mergeItemStack(itemStack, 36, slotCount, false)) return ItemStack.EMPTY;
			} else {
				if (!this.mergeItemStack(itemStack, 0, 36, false)) return ItemStack.EMPTY;
			}
			
			if (itemStack.getCount() == 0) slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();
			
			if (itemStack.getCount() == previous.getCount()) return ItemStack.EMPTY;
			slot.onTake(playerIn, itemStack);
		}
		return previous;
	}
	
	protected Slot addSlotToContainer(Slot slotIn) {
		slotCount++;
		return super.addSlotToContainer(slotIn);
	}
}