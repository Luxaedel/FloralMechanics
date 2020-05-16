package floralmechanics.tileentities.generators;

import floralmechanics.blocks.generators.BlockFloralGenerator;
import floralmechanics.recipes.FloralGeneratorManager;
import floralmechanics.util.Constants;
import floralmechanics.util.patterns.FloralCraftingPatterns;
import floralmechanics.util.patterns.FloralGeneratorPatterns;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityFloralGenerator extends TileEntityGeneratorBase {
	private NonNullList<ItemStack> surroundingBlocks;
	private final int timeBetweenUpdates = 20;
	private int timeSinceLastUpdate = 0;
	
	public TileEntityFloralGenerator(String name, int maxPower, int maxIn, int maxOut, int energy) {
		super(name, maxPower, maxIn, maxOut, energy);
		surroundingBlocks = NonNullList.<ItemStack>withSize((Constants.MAX_FLORAL_WIDTH * Constants.MAX_FLORAL_DEPTH), ItemStack.EMPTY);
	}
	
	public TileEntityFloralGenerator() {
		this("Floral Generator", 1000000, 0, 0, 0);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this.inventory;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("Inventory", this.inventory.serializeNBT());
		compound.setInteger("TimeSinceLastUpdate", this.timeSinceLastUpdate);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
		this.timeSinceLastUpdate = compound.getInteger("TimeSinceLastUpdate");
	}
	
	@Override
	public void update() {
		if (!this.world.isRemote) {
			this.timeSinceLastUpdate++;
			if (this.timeSinceLastUpdate >= this.timeBetweenUpdates) {
				this.timeSinceLastUpdate = 0;
				//Don't update if the max amount of energy is stored, as this will just be a waste of resources
				if (this.getEnergyStored() < this.getMaxEnergyStored()) updateSurroundings();
			}
			
			internalReceiveEnergy(rfPerTick, false);
			
			transmitEnergy();
			this.markDirty();
		}
	}
	
	public void updateSurroundings() {
		boolean flag = this.isActive();
		
		int blockCount = -1;
		int radiusX = ((Constants.MAX_FLORAL_WIDTH - 1) / 2);
		int radiusZ = ((Constants.MAX_FLORAL_DEPTH - 1) / 2);
		
		for (int x = -radiusX; x <= radiusX; x++) {
			for (int z = -radiusZ; z <= radiusZ; z++) {
				blockCount++;
				BlockPos blockPos = new BlockPos(this.pos.getX() + x, this.pos.getY(), this.pos.getZ() + z);
				Block radiusBlock = world.getBlockState(blockPos).getBlock();
				ItemStack radiusBlockStack = FloralGeneratorPatterns.instance().isValidPatternBlock(radiusBlock) ? new ItemStack(radiusBlock, 1) : ItemStack.EMPTY;
				radiusBlockStack.setItemDamage(0);
				
				this.surroundingBlocks.set(blockCount, radiusBlockStack);
			}
		}
		
		this.rfPerTick = FloralGeneratorManager.instance().getRFTickFromPattern(this.surroundingBlocks);
		if (flag != this.isActive()) BlockFloralGenerator.setState(this.isActive(), this.world, this.pos);
		this.markDirty();
	}
	
	public void dropInventory (World world, BlockPos pos) {
		for (int i = 0; i < this.inventory.getSlots(); i++) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.inventory.getStackInSlot(i)));
		}
	}
}