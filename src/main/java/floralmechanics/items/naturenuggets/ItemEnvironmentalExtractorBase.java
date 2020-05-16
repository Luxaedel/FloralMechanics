package floralmechanics.items.naturenuggets;

import java.util.List;

import floralmechanics.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
	public ItemEnvironmentalExtractorBase(String name) {
		super(name);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 60;
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		return stack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
		//TODO: Possibly, instead of targeting a specific block, select X blocks in Y radius around the player
		//TODO: and harvest those. With right click extraction like this it might as well be a bog-standard tool
		
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			if (!worldIn.isRemote && timeLeft <= getMaxItemUseDuration(stack) - 25) {
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
						block.getDrops(drops, worldIn, pos, worldIn.getBlockState(pos), 0);
						for (ItemStack dropStack : drops) {
							if (!player.addItemStackToInventory(dropStack)) {
								worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), dropStack));
							}
						}
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), worldIn.isRemote ? 11 : 3);
					
					}
				}
			}
		}
	}
}