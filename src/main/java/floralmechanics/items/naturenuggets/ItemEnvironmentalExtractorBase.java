package floralmechanics.items.naturenuggets;

import java.util.List;
import java.util.Random;

import floralmechanics.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemEnvironmentalExtractorBase extends ItemBase {
	protected final Random RANDOM = new Random();
	protected final int efficiencyTickDecreasePerLevel = 5;
	protected int dropOddsPerAction = 0, blockBreakOdds = 0, guaranteedDrops = 0, baseExtractDuration = 0;
	
	public ItemEnvironmentalExtractorBase(String name, int dropOddsPerAction, int blockBreakOdds, int baseExtractDuration) {
		super(name);
		this.guaranteedDrops = Math.floorDiv(dropOddsPerAction, 100);
		this.dropOddsPerAction = dropOddsPerAction - (this.guaranteedDrops * 100);
		this.blockBreakOdds = blockBreakOdds;
		this.baseExtractDuration = baseExtractDuration;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return Math.max(10, this.baseExtractDuration - (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) * this.efficiencyTickDecreasePerLevel));
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		return stack;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.FORTUNE) return true;
		else if (enchantment == Enchantments.EFFICIENCY) return true;
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count) {
		if (count <= 1 && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			World worldIn = player.world;
			if (!worldIn.isRemote) {
				Vec3d vec3d = player.getPositionVector().add(0,  player.eyeHeight, 0);
				Vec3d vec3d1 = player.getLookVec();
				//Vec3d vec3d2 = vec3d.scale(5);// vec3d1 * 5;
				Vec3d vec3d2 = vec3d.add(vec3d1.x * 5, vec3d1.y * 5, vec3d1.z * 5);
				RayTraceResult rayTrace = worldIn.rayTraceBlocks(vec3d, vec3d2, false, false, true);
				if (rayTrace != null) {
					BlockPos pos = rayTrace.getBlockPos();
					Block block = worldIn.getBlockState(pos).getBlock();
					if (!block.hasTileEntity(worldIn.getBlockState(pos))) {
						NonNullList<ItemStack> drops = NonNullList.<ItemStack>create();
						block.getDrops(drops, worldIn, pos, worldIn.getBlockState(pos), Math.floorDiv(EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack), 2));
						for (ItemStack dropStack : drops) {
							int total = this.RANDOM.nextInt(100) < this.dropOddsPerAction ? this.guaranteedDrops + 1 : this.guaranteedDrops;
							dropStack.setCount(total);
							if (!player.addItemStackToInventory(dropStack)) {
								worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), dropStack));
							}
						}
						if (this.RANDOM.nextInt(100) < this.blockBreakOdds) worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), worldIn.isRemote ? 11 : 3);
					}
				}
			}
		}
	}
}