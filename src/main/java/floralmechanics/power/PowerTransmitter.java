package floralmechanics.power;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class PowerTransmitter {
	private BlockPos pos;
	
	public PowerTransmitter (BlockPos pos) {
		this.pos = pos;
	}
	
	public void transmitEnergy (World world, EnergyStorageBase storageBase) {
		int powerToTransmit = Math.min(storageBase.getMaxEnergyExtract(), storageBase.getEnergyStored());
		if (powerToTransmit > 0) {
			EnergyStorageBase[] handlers = new EnergyStorageBase[6];
			int[] wantedSide = new int[6];
			int accepted = 0;
			for (EnumFacing neighbour : EnumFacing.VALUES) {
				TileEntity te = world.getTileEntity(pos.offset(neighbour));
				EnumFacing from = neighbour.getOpposite();
				
				if (te == null) continue;
				
				EnergyStorageBase handler = null;
				if (te.hasCapability(CapabilityEnergy.ENERGY, from)) {
					handler = (EnergyStorageBase)te.getCapability(CapabilityEnergy.ENERGY, from);
					if (!handler.canReceive()) handler = null;
				}
				
				if (handler != null) {
					handlers[from.ordinal()] = handler;
					int wanted = handler.receiveEnergy(powerToTransmit, true);
					wantedSide[from.ordinal()] = wanted;
					accepted += wanted;
				}
			}
			
			if (accepted > 0) {
				for (EnumFacing from : EnumFacing.VALUES) {
					EnergyStorageBase handler = handlers[from.ordinal()];
					int wanted = wantedSide[from.ordinal()];
					if (handler == null || wanted == 0) continue;
					
					int transmit = Math.max(Math.min(storageBase.getEnergyStored(), wanted), wanted * accepted / Math.min(powerToTransmit, (handler.getMaxEnergyStored() - handler.getEnergyStored())));
					int received = Math.min(transmit, handler.receiveEnergy(transmit, false));
					storageBase.internalExtractEnergy(received, false);
					if (storageBase.getEnergyStored() <= 0) break;
				}
			}
		}
	}
}