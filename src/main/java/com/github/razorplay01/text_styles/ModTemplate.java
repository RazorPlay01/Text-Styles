package com.github.razorplay01.text_styles;

import com.github.razorplay01.text_styles.platform.Platform;

import net.minecraft.resources./*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import com.github.razorplay01.text_styles.platform.fabric.FabricPlatform;
//?} neoforge {
/*import com.github.razorplay01.text_styles.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import com.github.razorplay01.text_styles.platform.forge.ForgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class ModTemplate {

	public static final String MOD_ID = /*$ mod_id*/ "text_styles";
	public static final String MOD_VERSION = /*$ mod_version*/ "1.2.2";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Text Styles";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, ModTemplate.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
		TextStyles.init();
	}

	public static void onInitializeClient() {
		LOGGER.info("Initializing {} Client on {}", MOD_ID, ModTemplate.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?} forge {
		/*return new ForgePlatform();
		 *///?}
	}

	public static /*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */ id(String path) {
		//? > 1.19.2 {
		return /*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */.fromNamespaceAndPath(MOD_ID, path);
		 //?} <= 1.19.2 {
		/*return new ResourceLocation(MOD_ID, path);
		*///?}
	}

	public static /*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */ id(String namespace, String path) {
		//? > 1.19.2 {
		return /*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */.fromNamespaceAndPath(namespace, path);
		 //?} <= 1.19.2 {
		/*return new ResourceLocation(namespace, path);
		*///?}
	}
}
