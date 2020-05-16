package floralmechanics.tileentities.generators;

import floralmechanics.power.EnergyStorageBase;
import floralmechanics.power.PowerTransmitter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGeneratorBase extends EnergyStorageBase implements ITickable {
	protected PowerTransmitter transmitter;
	
	protected int rfPerTick = 0, rfPerTickBase = 0, progressSpeed = 1;
	
	protected ItemStackHandler inventory = new ItemStackHandler(2) {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			TileEntityGeneratorBase.this.markDirty();
		};
	};
	
	public TileEntityGeneratorBase(String name, int maxPower, int maxIn, int maxOut, int energy) {
		super(name, maxPower, maxIn, maxOut, energy);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.rfPerTick = compound.getInteger("RFPerTick");
		this.rfPerTickBase = compound.getInteger("RFPerTickBase");
		this.progressSpeed = compound.getInteger("ProgressSpeed");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("RFPerTick", this.rfPerTick);
		compound.setInteger("RFPerTickBase", this.rfPerTickBase);
		compound.setInteger("ProgressSpeed", (short)this.progressSpeed);
		return compound;
	}
	
	protected boolean transmitEnergy() {
		if (transmitter == null) transmitter = new PowerTransmitter(this.pos);
		transmitter.transmitEnergy(world, this);
		return true;
	}
	
	protected boolean isActive() {
		return this.rfPerTick > 0;
	}
	
	public int getRFPerTick() {
		return this.rfPerTick;
	}
	
	protected void burnFuel() {
		//Stub
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public boolean shouldRefresh(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}