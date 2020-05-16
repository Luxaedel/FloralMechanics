package floralmechanics.util.handlers;

import floralmechanics.containers.generators.ContainerFloralGenerator;
import floralmechanics.containers.main.ContainerFloralCraftingTable;
import floralmechanics.guis.generators.GuiFloralGenerator;
import floralmechanics.guis.main.GuiFloralCraftingTable;
import floralmechanics.init.ModGuis;
import floralmechanics.tileentities.generators.TileEntityFloralGenerator;
import floralmechanics.tileentities.main.TileEntityFloralCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == ModGuis.GUI_FLORAL_GENERATOR) return new ContainerFloralGenerator(player.inventory, (TileEntityFloralGenerator)world.getTileEntity(new BlockPos(x, y, z)));
		else if (ID == ModGuis.GUI_FLORAL_CRAFTING_TABLE) return new ContainerFloralCraftingTable(player.inventory, world, new BlockPos(x, y, z), (TileEntityFloralCraftingTable)world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == ModGuis.GUI_FLORAL_GENERATOR) return new GuiFloralGenerator(player.inventory, (TileEntityFloralGenerator)world.getTileEntity(new BlockPos(x, y, z)));
		else if (ID == ModGuis.GUI_FLORAL_CRAFTING_TABLE) return new GuiFloralCraftingTable(player.inventory, world, new BlockPos(x, y, z), (TileEntityFloralCraftingTable)world.getTileEntity(new BlockPos(x, y, z)));
		return null;	
	}
}