package floralmechanics;

import floralmechanics.proxy.CommonProxy;
import floralmechanics.tabs.TabFloralMechanics;
import floralmechanics.util.Constants;
import floralmechanics.util.handlers.RegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.MODVERSION, acceptedMinecraftVersions = Constants.MODMC_VERSION)
public class FloralMechanics {
	public static final CreativeTabs FLORAL_MECHANICS_TAB = new TabFloralMechanics("floralmechanicstab");
	
	@Instance
	public static FloralMechanics instance;
	
	@SidedProxy(clientSide = Constants.CLIENT_PROXY_CLASS, serverSide = Constants.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Constants.LOGGER.info("Pre-initializing...");
		RegistryHandler.preInitRegistries();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Constants.LOGGER.info("Initializing...");
		RegistryHandler.initRegistries();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Constants.LOGGER.info("Post-initializing...");
		RegistryHandler.postInitRegistries();
		
	}
}