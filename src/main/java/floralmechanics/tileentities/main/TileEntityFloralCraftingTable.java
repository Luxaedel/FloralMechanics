package floralmechanics.tileentities.main;

import java.util.stream.Collectors;

import floralmechanics.init.ModBlocks;
import floralmechanics.tileentities.TileEntityBase;
import floralmechanics.util.Constants;
import floralmechanics.util.patterns.FloralCraftingPatterns;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TileEntityFloralCraftingTable extends TileEntityBase implements IInventory, ISidedInventory {
	private NonNullList<ItemStack> surroundingBlocks;
	private ItemStack result = ItemStack.EMPTY;
	private NonNullList<ItemStack> craftMatrix;
	
	public TileEntityFloralCraftingTable() {
		super ("Floral crafting table");
		this.surroundingBlocks = NonNullList.<ItemStack>withSize((Constants.FLORAL_CRAFTING_WIDTH * Constants.FLORAL_CRAFTING_HEIGHT), ItemStack.EMPTY);
		this.craftMatrix = NonNullList.<ItemStack>withSize(Constants.FLORAL_CRAFTING_WIDTH * Constants.FLORAL_CRAFTING_HEIGHT, ItemStack.EMPTY);
	}
	
//	@Override
//	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
//		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
//		return super.hasCapability(capability, facing);
//	}
//	
//	@Override
//	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
//		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this.craftMatrix;
//		return super.getCapability(capability, facing);
//	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		result = new ItemStack(compound.getCompoundTag("Result"));
		for (int i = 0; i < this.craftMatrix.size(); i++) {
			if (compound.hasKey("Craft" + i)) this.craftMatrix.set(i, new ItemStack(compound.getCompoundTag("Craft" + i)));
		}
		for (int i = 0; i < this.surroundingBlocks.size(); i++) {
			if (compound.hasKey("Surround" + i)) this.surroundingBlocks.set(i, new ItemStack(compound.getCompoundTag("Surround" + i)));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		if (!this.result.isEmpty()) {
			NBTTagCompound produce = new NBTTagCompound();
			this.result.writeToNBT(produce);
			compound.setTag("Result", produce);
		}
		
		for (int i = 0; i < this.craftMatrix.size(); i++) {
			if (!this.craftMatrix.get(i).isEmpty()) {
				NBTTagCompound craft = new NBTTagCompound();
				this.craftMatrix.get(i).writeToNBT(craft);
				compound.setTag("Craft" + i, craft);
			} else compound.removeTag("Craft" + i);
		}
		
		for (int i = 0; i < this.surroundingBlocks.size(); i++) {
			//if (!this.surroundingBlocks.get(i).isEmpty()) {
				NBTTagCompound Surround = new NBTTagCompound();
				this.surroundingBlocks.get(i).writeToNBT(Surround);
				compound.setTag("Surround" + i, Surround);
			//} else compound.removeTag("Surround" + i);
		}
		
		return compound;
	}
	
	@Override
	public int getSizeInventory() {
		return (Constants.FLORAL_CRAFTING_WIDTH * Constants.FLORAL_CRAFTING_HEIGHT) + 1;
	}
	
	@Override
	public boolean isEmpty() {
		return this.craftMatrix.stream().filter(c -> !c.isEmpty()).collect(Collectors.toList()).size() <= 0 && this.result.isEmpty();
	}
	
	@Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0) return this.result;
        else if (slot <= this.craftMatrix.size()) return this.craftMatrix.get(slot - 1);
        else return ItemStack.EMPTY;
    }
	
	@Override
	public ItemStack decrStackSize(int slot, int decrement) {
		if (slot == 0) {
			if (!this.result.isEmpty()) {
				for (int i = 1; i <= this.craftMatrix.size(); i++) {
					decrStackSize(i, 1);
				}
				
				if (this.result.getCount() <= decrement) {
					ItemStack craft = result;
					result = ItemStack.EMPTY;
					return craft;
				}
				
				ItemStack split = this.result.splitStack(decrement);
				
				if (result.getCount() <= 0) this.result = ItemStack.EMPTY;
				
				return split;
			} else return ItemStack.EMPTY;
		} else if (slot <= this.craftMatrix.size()) {
			if (this.craftMatrix.get(slot - 1) != ItemStack.EMPTY) {
				if (this.craftMatrix.get(slot - 1).getCount() <= decrement) {
					ItemStack ingredient = this.craftMatrix.get(slot - 1);
					this.craftMatrix.set(slot - 1, ItemStack.EMPTY);
					return ingredient;
				}
				
				ItemStack split = this.craftMatrix.get(slot - 1).splitStack(decrement);
				
				if (this.craftMatrix.get(slot - 1).getCount() <= 0) this.craftMatrix.set(slot - 1, ItemStack.EMPTY);
				
				return split;
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (slot == 0) {
			if (!this.result.isEmpty()) {
				for (int i = 1; i < this.craftMatrix.size(); i++) {
					decrStackSize(i, 1);
				}
				
				ItemStack craft = this.result;
				this.result = ItemStack.EMPTY;
				return craft;
			} else return ItemStack.EMPTY;
		} else if (slot <= this.craftMatrix.size()) {
			if (!this.craftMatrix.get(slot - 1).isEmpty()) {
				ItemStack ingredient = this.craftMatrix.get(slot - 1);
				this.craftMatrix.set(slot - 1, ItemStack.EMPTY);
				return ingredient;
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
		
	}
	
	@Override
	public boolean isItemValidForSlot (int slot, ItemStack stack) {
		return false;
	}
	
	@Override
    public int getInventoryStackLimit() {
        return 64;
    }
	
	@Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot == 0) {
            this.result = stack;
        } else if (slot <= this.craftMatrix.size()) {
            this.craftMatrix.set(slot - 1, stack);
        }
    }
	
	@Override
    public String getName() {
        return "container.floral";
    }
	
	@Override
    public boolean hasCustomName() {
        return true;
    }
	
	@Override
    public ITextComponent getDisplayName() {
        if (hasCustomName()) return new TextComponentString(getName());
        return new TextComponentTranslation(getName());
    }
	
	@Override
    public int[] getSlotsForFace(EnumFacing face) {
        return new int[] {};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing face) {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing face) {
        return false;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void setField(int id, int value) {
    	
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void clear() {
        result = ItemStack.EMPTY;
        for (int i = 0; i < this.craftMatrix.size(); i++) {
            this.craftMatrix.set(i, ItemStack.EMPTY);
        }
    }
    
    public NonNullList<ItemStack> getSurroundingBlocks() {
    	return this.surroundingBlocks;
    }
	
	public void updateSurroundings() {
		int blockCount = -1;
		int radiusX = ((Constants.MAX_FLORAL_WIDTH - 1) / 2);
		int radiusZ = ((Constants.MAX_FLORAL_DEPTH - 1) / 2);
		
		for (int x = -radiusX; x <= radiusX; x++) {
			for (int z = -radiusZ; z <= radiusZ; z++) {
				blockCount++;
				BlockPos blockPos = new BlockPos(this.pos.getX() + x, this.pos.getY(), this.pos.getZ() + z);
				Block radiusBlock = world.getBlockState(blockPos).getBlock();
				ItemStack radiusBlockStack = FloralCraftingPatterns.instance().isValidPatternBlock(radiusBlock) ? new ItemStack(radiusBlock, 1) : ItemStack.EMPTY;
				radiusBlockStack.setItemDamage(0);
				
				this.surroundingBlocks.set(blockCount, radiusBlockStack);
			}
		}
		
		this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
		this.markDirty();
	}
	
	public void dropInventory (World world, BlockPos pos) {
		for (int i = 0; i < this.craftMatrix.size(); i++) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.craftMatrix.get(i)));
		}
	}
}