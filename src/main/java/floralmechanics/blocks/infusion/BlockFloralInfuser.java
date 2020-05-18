package floralmechanics.blocks.infusion;

import java.util.Random;

import floralmechanics.blocks.BlockBase;
import floralmechanics.init.ModBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFloralInfuser extends BlockBase {
	public BlockFloralInfuser(String name, Material material) {
		super(name, material);
		setSoundType(SoundType.METAL);
		this.setDefaultState(this.blockState.getBaseState());
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.FLORAL_INFUSER);
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModBlocks.FLORAL_INFUSER);
	}
	
//	@Override
//	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//		if (!worldIn.isRemote) {
//			TileEntityFloralCraftingTable te = (TileEntityFloralCraftingTable)worldIn.getTileEntity(pos);
//			te.updateSurroundings();
//			playerIn.openGui(FloralMechanics.instance, ModGuis.GUI_FLORAL_CRAFTING_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
//		}
//		return true;
//	}
	
//	@Override
//	public TileEntity createNewTileEntity(World worldIn, int meta) {
//		return new TileEntityFloralCraftingTable();
//	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.byIndex(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
		return this.getDefaultState();
	}
}