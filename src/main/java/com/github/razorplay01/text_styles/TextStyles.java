package com.github.razorplay01.text_styles;

import com.github.razorplay01.text_styles.styles.BounceStyle;
import com.github.razorplay01.text_styles.styles.FadeStyle;
import com.github.razorplay01.text_styles.styles.GlitchStyle;
import com.github.razorplay01.text_styles.styles.GradientStyle;
import com.github.razorplay01.text_styles.styles.MarqueeStyle;
import com.github.razorplay01.text_styles.styles.PulseStyle;
import com.github.razorplay01.text_styles.styles.RainbowStyle;
import com.github.razorplay01.text_styles.styles.ShadowStyle;
import com.github.razorplay01.text_styles.styles.ShakeStyle;
import com.github.razorplay01.text_styles.styles.ShowAfterStyle;
import com.github.razorplay01.text_styles.styles.SwingStyle;
import com.github.razorplay01.text_styles.styles.TextStyle;
import com.github.razorplay01.text_styles.styles.TextStyle.TextStyleInstance;
import com.github.razorplay01.text_styles.styles.TurbulenceStyle;
import com.github.razorplay01.text_styles.styles.TypewriterStyle;
import com.github.razorplay01.text_styles.styles.WaveStyle;
import com.github.razorplay01.text_styles.styles.WobbleStyle;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
//? >=1.21.11 {
/*import net.minecraft.util.Util;
*///?}
//? <1.21.11 {
import net.minecraft.Util;
//?}

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Registry and default implementations for text styles.
 */
@SuppressWarnings("unused")
public final class TextStyles {
    private TextStyles() {
        /* This utility class should not be instantiated */
    }

    public static final ResourceLocation REGISTRY_ID = ModTemplate.id("text_styles");
    public static final ResourceKey<Registry<TextStyle>> REGISTRY_KEY = ResourceKey.createRegistryKey(REGISTRY_ID);
    public static final Registry<TextStyle> REGISTRY = new MappedRegistry<>(REGISTRY_KEY, Lifecycle.stable());

    public static void init() {
		ModTemplate.LOGGER.info("Initializing TextStyles!");
    }

    /**
     * Codec for serializing a single text style instance with its parameters.
     */
    public static final Codec<TextStyleInstance> CODEC = REGISTRY.byNameCodec()
            .dispatch(TextStyleInstance::getType, TextStyle::getCodec);

    /**
     * Codec for a list of text style instances.
     */
    public static final Codec<List<TextStyleInstance>> LIST_CODEC = /*? >1.21.3 >>+ '.'*//*ExtraCodecs.*/compactListCodec(CODEC);

    // Register built-in styles
    public static final TextStyle WOBBLE = register("wobble", new WobbleStyle());
    public static final TextStyle SHAKE = register("shake", new ShakeStyle());
    public static final TextStyle MARQUEE = register("marquee", new MarqueeStyle());
    public static final TextStyle TYPEWRITER = register("typewriter", new TypewriterStyle());
    public static final TextStyle SHOW_AFTER = register("show_after", new ShowAfterStyle());
    public static final TextStyle WAVE = register("wave", new WaveStyle());
    public static final TextStyle RAINBOW = register("rainbow", new RainbowStyle());
    public static final TextStyle BOUNCE = register("bounce", new BounceStyle());
    public static final TextStyle FADE = register("fade", new FadeStyle());
    public static final TextStyle GLITCH = register("glitch", new GlitchStyle());
    public static final TextStyle SWING = register("swing", new SwingStyle());
    public static final TextStyle PULSE = register("pulse", new PulseStyle());
    public static final TextStyle SHADOW = register("shadow", new ShadowStyle());
    public static final TextStyle GRADIENT = register("gradient", new GradientStyle());
    public static final TextStyle TURBULENCE = register("turbulence", new TurbulenceStyle());

    public static <T extends TextStyle> T register(String id, T style) {
        return Registry.register(REGISTRY, id(id), style);
    }

    public static ResourceLocation id(String path) {
        return ModTemplate.id(path);
    }

	//? <=1.21.3 && >1.17.1 {
	private static <E> Codec<List<E>> compactListCodec(Codec<E> codec) {
		//noinspection SequencedCollectionMethodCanBeUsed
		return Codec.either(codec.listOf(), codec)
				.xmap(either -> either.map(list -> list, List::of), list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list));
	}
	//? }

    /**
     * Utility to get the current time in milliseconds.
     */
    public static long getTimeMs() {
        return Util.getMillis();
    }

    /**
     * Generates an array of integers representing a looped range.
     */
    public static int[] rangeLoop(int min, int max) {
        int dist = Math.abs(max - min);
        if (dist == 0) return new int[]{min};

        if (min > max) {
            int i = min;
            min = max;
            max = i;
        }

        int[] arr = new int[dist * 2];
        for (int i = 0; i < arr.length; i++) {
            if (i < dist) {
                arr[i] = min + i;
            } else {
                arr[i] = max - (i - dist);
            }
        }
        return arr;
    }

    /**
     * Helper to parse parameters from a string.
     * Supports formats like "speed=2 distance=3", "speed=2,distance=3" or just "2" (as a default parameter).
     */
    public static Map<String, String> parseParams(String paramString) {
        Map<String, String> params = new HashMap<>();
        if (paramString == null || paramString.isEmpty()) return params;

        // Regex to match key=value or just value.
        // It handles spaces and commas as separators.
        Pattern pattern = Pattern.compile("([\\w.]+)(?:=([^,\\s>]+))?");
        Matcher matcher = pattern.matcher(paramString);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            if (value != null) {
                params.put(key, value);
            } else {
                if (!params.containsKey("value")) {
                    params.put("value", key);
                }
            }
        }
        return params;
    }
}
