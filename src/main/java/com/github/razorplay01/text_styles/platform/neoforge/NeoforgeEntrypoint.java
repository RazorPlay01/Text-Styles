package com.github.razorplay01.text_styles.platform.neoforge;

//? neoforge {

import com.github.razorplay01.text_styles.ModTemplate;
import com.github.razorplay01.text_styles.command.TextStylesCommand;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(ModTemplate.MOD_ID)
public class NeoforgeEntrypoint {

	public NeoforgeEntrypoint() {
		ModTemplate.onInitialize();
		NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> TextStylesCommand.register(event.getDispatcher(), event.getBuildContext()));
	}
}
//?}
