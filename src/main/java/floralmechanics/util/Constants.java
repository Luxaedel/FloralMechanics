package floralmechanics.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
	public static final String MODID = "floralmechanics";
	public static final String MODNAME = "Floral Mechanics";
	public static final String MODVERSION = "0.0.1";
	public static final String MODMC_VERSION = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "floralmechanics.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "floralmechanics.proxy.CommonProxy";
	
	public static final int MAX_FLORAL_WIDTH = 5;
	public static final int MAX_FLORAL_DEPTH = 5;
	
	public static final int FLORAL_CRAFTING_WIDTH = 5;
	public static final int FLORAL_CRAFTING_HEIGHT = 5;
	
	public static final Logger LOGGER = LogManager.getLogger(MODID);
}
