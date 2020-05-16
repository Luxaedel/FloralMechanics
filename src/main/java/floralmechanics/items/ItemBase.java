package floralmechanics.items;

import floralmechanics.FloralMechanics;
import floralmechanics.init.ModItems;
import net.minecraft.item.Item;

public class ItemBase extends Item {
	public ItemBase(String name) {
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(FloralMechanics.FLORAL_MECHANICS_TAB);
		this.maxStackSize = 1;
		ModItems.ITEMS.add(this);
	}
}