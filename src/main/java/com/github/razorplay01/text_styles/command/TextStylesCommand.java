package com.github.razorplay01.text_styles.command;

import com.github.razorplay01.text_styles.TextStyleParser;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/**
 * Command to showcase all available text styles.
 */
public class TextStylesCommand {
	private TextStylesCommand() {
		/* This utility class should not be instantiated */
	}


	public static void register(final CommandDispatcher<CommandSourceStack> dispatcher,
	                            final CommandBuildContext buildContext) {
		dispatcher.register(Commands.literal("textstyles")
				.then(Commands.literal("showcase")
						.executes(context -> showcase(context.getSource()))
				)
		);
	}

	private static int showcase(CommandSourceStack source) {
		source.sendSystemMessage(Component.literal("§6--- Text Styles Showcase ---"));

		show(source, "Wobble", "<wobble>Vertical Wobble Effect</wobble>");
		show(source, "Shake", "<shake>Random Shake Effect</shake>");
		show(source, "Wave", "<wave amplitude=4>Smooth Wave Pattern</wave>");
		show(source, "Rainbow", "<rainbow frequency=0.01>Dynamic Rainbow Colors</rainbow>");
		show(source, "Bounce", "<bounce>Bouncing Characters!</bounce>");
		show(source, "Pulse", "<pulse intensity=0.5 color=FF0000>Pulsing Scale & Color</pulse>");
		show(source, "Fade", "<fade min=0.2 speed=0.01>Fading Ghost Text</fade>");
		show(source, "Swing", "<swing amplitude=20>Swinging Rotation</swing>");
		show(source, "Glitch", "<glitch intensity=2>System Glitch Effect</glitch>");
		show(source, "Turbulence", "<turbulence intensity=2>Turbulent Movement</turbulence>");
		show(source, "Gradient", "<gradient color1=FF0000 color2=0000FF>Animated Color Gradient</gradient>");
		show(source, "Typewriter", "<typewriter delay=100>Classic typewriter reveal...</typewriter>");
		show(source, "Typewriter Drop", "<typewriter_drop>Text dropping in from above</typewriter_drop>");
		show(source, "Marquee", "<marquee>Scrolling Marquee Text</marquee>");

		source.sendSystemMessage(Component.literal("§6----------------------------"));
		return 1;
	}

	private static void show(CommandSourceStack source, String name, String tag) {
		source.sendSystemMessage(Component.literal("§e" + name + ": ").append(TextStyleParser.parse(tag)));
	}
}
