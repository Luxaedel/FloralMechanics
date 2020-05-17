package floralmechanics.blocks.flowers;

import java.util.Random;

import floralmechanics.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAustralea extends BlockFlowerBase {
	public BlockAustralea(String name, Material material) {
		super(name, material);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.AUSTRALEA);
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModBlocks.AUSTRALEA);
	}
	
	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == null || block.isReplaceable(worldIn, pos)) {
            IBlockState ceilingBlockState = worldIn.getBlockState(pos.up());
            Block ceilingBlock = ceilingBlockState.getBlock();

            if (ceilingBlock != null && ceilingBlock != Blocks.AIR) {
            	return true;
            }
        }
        return false;
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) {//Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.{
            IBlockState ceilingBlockState = worldIn.getBlockState(pos.up());
            Block ceilingBlock = ceilingBlockState.getBlock();
            return ceilingBlock != null && ceilingBlock != Blocks.AIR;
        }
        return true;
    }
}