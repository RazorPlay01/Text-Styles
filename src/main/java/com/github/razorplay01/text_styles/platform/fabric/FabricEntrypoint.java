package com.github.razorplay01.text_styles.platform.fabric;

//? fabric {

import com.github.razorplay01.text_styles.ModTemplate;
import com.github.razorplay01.text_styles.command.TextStylesCommand;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ModTemplate.onInitialize();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			TextStylesCommand.register(dispatcher, registryAccess);
		});
	}
}
//?}
