package floralmechanics.blocks.flowers;

import java.util.Random;

import javax.annotation.Nonnull;

import floralmechanics.FloralMechanics;
import floralmechanics.blocks.BlockBase;
import floralmechanics.init.ModBlocks;
import floralmechanics.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockFlowerBase extends BlockBush implements IPlantable {
	public BlockFlowerBase(String name, Material material) {
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(FloralMechanics.FLORAL_MECHANICS_TAB);
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState());
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos));
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }
	
	@Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
	
	@Nonnull
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		if (this == ModBlocks.AUSTRALEA) return EnumPlantType.Plains;
        return EnumPlantType.Plains;
    }
	
	@Override
    public boolean isReplaceable(IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return false;
    }
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return getDefaultState();
        return state;
	}
	
	@Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }
}