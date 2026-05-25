package com.github.razorplay01.text_styles;

import com.github.razorplay01.text_styles.styles.TextStyle;
import com.github.razorplay01.text_styles.util.StyleExtension;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.resources./*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class TextStyleParser {
    private TextStyleParser() {
        /* This utility class should not be instantiated */
    }


    private static final Pattern TAG_PATTERN = Pattern.compile(
            "<([a-zA-Z0-9_]+)(?:\\s+([^>]+))?>|</([a-zA-Z0-9_]+)>",
            Pattern.CASE_INSENSITIVE
    );

    private static final ThreadLocal<Boolean> PARSING = ThreadLocal.withInitial(() -> false);

    /**
     * Parses a string into a MutableComponent, applying text styles defined in tags.
     *
     * @param text The string to parse.
     * @return A component with applied styles.
     */
    public static MutableComponent parse(String text) {
        if (text == null || !text.contains("<") || PARSING.get()) {
            return Component.literal(text != null ? text : "");
        }

        PARSING.set(true);
        try {
            MutableComponent root = Component.empty();
            Deque<Style> styleStack = new ArrayDeque<>();
            styleStack.push(Style.EMPTY);

            Matcher matcher = TAG_PATTERN.matcher(text);
            int lastEnd = 0;

            while (matcher.find()) {
                if (matcher.start() > lastEnd) {
                    String plain = text.substring(lastEnd, matcher.start());
                    root.append(MutableComponent.create(PlainTextContents.create(plain))
                            .withStyle(styleStack.peek()));
                }

                String tag = matcher.group(1);
                String params = matcher.group(2);
                String closing = matcher.group(3);

                if (tag != null) {
                    TextStyle.TextStyleInstance instance = getStyleInstance(tag, params);
                    Style current = styleStack.peek();
                    Style newStyle = instance != null
                            ? ((StyleExtension) (Object) current).withStyle(instance)
                            : current;
                    styleStack.push(newStyle);
                } else if (closing != null && styleStack.size() > 1) {
                    styleStack.pop();
                }

                lastEnd = matcher.end();
            }

            if (lastEnd < text.length()) {
                root.append(MutableComponent.create(PlainTextContents.create(text.substring(lastEnd)))
                        .withStyle(styleStack.peek()));
            }

            return root;
        } finally {
            PARSING.set(false);
        }
    }

    private static TextStyle.TextStyleInstance getStyleInstance(String name, String paramString) {
        if (name == null) return null;
        name = name.toLowerCase(Locale.ROOT);

        Map<String, String> params = TextStyles.parseParams(paramString);

        // Style name aliases
        name = switch (name) {
            case "wb" -> "wobble";
            case "sh" -> "shake";
            case "wv" -> "wave";
            case "rb" -> "rainbow";
            case "bn" -> "bounce";
            case "pl" -> "pulse";
            case "fd" -> "fade";
            case "sw" -> "swing";
            case "gl" -> "glitch";
            case "tb" -> "turbulence";
            case "gr" -> "gradient";
            case "tw" -> "typewriter";
            case "mq" -> "marquee";
            case "sd" -> "shadow";
            case "sa" -> "show_after";
            default -> name;
        };

        // Special handling for old unified typewriter effects
        switch (name) {
            case "typewriter_drop" -> {
                if (!params.containsKey("land") && !params.containsKey("l")) params.put("land", "-4");
                name = "typewriter";
            }
            case "typewriter_rise" -> {
                if (!params.containsKey("land") && !params.containsKey("l")) params.put("land", "4");
                name = "typewriter";
            }
            case "typewriter_snap" -> {
                if (!params.containsKey("snap") && !params.containsKey("sn")) params.put("snap", "6");
                name = "typewriter";
            }
            default -> {
                // []
            }
        }

		/*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */ id = ModTemplate.id(name);
        //? >1.21.1{
		TextStyle style = TextStyles.REGISTRY.get(id)
                .map(Holder.Reference::value)
                .orElse(null);
		//?}
		//? <=1.21.1{
		/*TextStyle style = TextStyles.REGISTRY.get(id);
		*///?}

        if (style != null) {
            return style.createInstance(params);
        }

        return null;
    }

    /**
     * Parses a component's content if it contains style tags.
     */
    public static Component parseComponent(Component component) {
        if (component == null) return null;
        String str = component.getString();
        if (!str.contains("<")) return component;

        MutableComponent parsed = parse(str);
        if (!component.getStyle().isEmpty()) {
            parsed.setStyle(component.getStyle());
        }
        return parsed;
    }
}
