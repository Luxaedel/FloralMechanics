package floralmechanics.containers.slots;

import floralmechanics.crafting.floral.FloralCraftingManager;
import floralmechanics.inventories.InventoryFloralCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SlotFloralCrafting extends Slot {
	private final InventoryFloralCrafting craftMatrix;
	private final EntityPlayer player;
	private int amountCrafted;
	
	public SlotFloralCrafting (EntityPlayer player, InventoryFloralCrafting craftMatrix, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super (inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
		this.craftMatrix = craftMatrix;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) this.amountCrafted += Math.min(amount, this.getStack().getCount());
		return super.decrStackSize(amount);
	}
	
	protected void onCrafting(ItemStack stack, int amount) {
		this.amountCrafted += amount;
		this.onCrafting(stack);
	}
	
	protected void onCrafting(ItemStack stack) {
		if (this.amountCrafted > 0) {
			stack.onCrafting(this.player.world, this.player, this.amountCrafted);
			net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(this.player, stack, this.craftMatrix);
		}
		
		amountCrafted = 0;
	}
	
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		this.onCrafting(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> slots = FloralCraftingManager.instance().getRemainingItems(this.craftMatrix, player.world);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
		
		for (int i = 0; i < slots.size(); i++) {
			ItemStack itemStack = this.craftMatrix.getStackInSlot(i);
			ItemStack itemStack1 = slots.get(i);
			
			if (!itemStack.isEmpty()) {
				this.craftMatrix.decrStackSize(i, 1);
				itemStack = this.craftMatrix.getStackInSlot(i);
			}
			
			if (!itemStack1.isEmpty()) {
				if (itemStack.isEmpty()) this.craftMatrix.setInventorySlotContents(i, itemStack1);
				else if (ItemStack.areItemsEqual(itemStack, itemStack1) && ItemStack.areItemStackTagsEqual(itemStack, itemStack1)) {
					itemStack1.grow(itemStack.getCount());
					this.craftMatrix.setInventorySlotContents(i, itemStack1);
				} else if (!this.player.inventory.addItemStackToInventory(itemStack1)) this.player.dropItem(itemStack1, false);
			}
		}
		
		return stack;
	}
}