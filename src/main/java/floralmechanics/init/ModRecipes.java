package floralmechanics.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
	public static void init() {
		GameRegistry.addSmelting(Items.APPLE, new ItemStack(Blocks.DOUBLE_PLANT, 32, 4), 5);
	}
}