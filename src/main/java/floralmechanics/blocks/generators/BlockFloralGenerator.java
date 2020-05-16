package floralmechanics.blocks.generators;

import java.util.Random;

import floralmechanics.FloralMechanics;
import floralmechanics.init.ModBlocks;
import floralmechanics.init.ModGuis;
import floralmechanics.tileentities.generators.TileEntityFloralGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFloralGenerator extends BlockGeneratorBase {
	public BlockFloralGenerator(String name, Material material) {
		super(name, material);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.FLORAL_GENERATOR);
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModBlocks.FLORAL_GENERATOR);
	}
	
	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		worldIn.setBlockState(pos, ModBlocks.FLORAL_GENERATOR.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(BURNING,  active), 3);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFloralGenerator();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(FloralMechanics.instance, ModGuis.GUI_FLORAL_GENERATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
			TileEntityFloralGenerator te = (TileEntityFloralGenerator)worldIn.getTileEntity(pos);
			te.updateSurroundings();
		}
		return true;
	}
	
	@Override
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
		TileEntityFloralGenerator te = (TileEntityFloralGenerator)worldIn.getTileEntity(pos);
		te.dropInventory(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
}