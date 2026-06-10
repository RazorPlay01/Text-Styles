package com.github.razorplay01.text_styles;

import com.github.razorplay01.text_styles.styles.TextStyle;
import com.github.razorplay01.text_styles.styles.VanillaStyleInstance;
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
					Style newStyle = current;

					if (instance != null) {
						if (instance instanceof VanillaStyleInstance v) {
							newStyle = current.applyTo(v.getVanillaStyle());
						} else {
							newStyle = ((StyleExtension) (Object) current).withStyle(instance);
						}
					}
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
		name = name.toLowerCase(Locale.ROOT).trim();

		Map<String, String> params = TextStyles.parseParams(paramString);

		// Alias de estilos custom del mod
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

		// === SOPORTE VANILLA + HEX ===
		Style vanillaStyle = getVanillaStyle(name, params);
		if (vanillaStyle != null) {
			return new VanillaStyleInstance(vanillaStyle);
		}

		// === ESTILOS CUSTOM DEL MOD ===
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

	private static Style getVanillaStyle(String name, Map<String, String> params) {
		// Soporte para §c, §l, etc.
		if (name.startsWith("§")) {
			name = name.substring(1);
		}

		// Código de un solo carácter (§l, §c, §r, etc.)
		if (name.length() == 1) {
			return switch (name.charAt(0)) {
				case '0' -> Style.EMPTY.withColor(0x000000);
				case '1' -> Style.EMPTY.withColor(0x0000AA);
				case '2' -> Style.EMPTY.withColor(0x00AA00);
				case '3' -> Style.EMPTY.withColor(0x00AAAA);
				case '4' -> Style.EMPTY.withColor(0xAA0000);
				case '5' -> Style.EMPTY.withColor(0xAA00AA);
				case '6' -> Style.EMPTY.withColor(0xFFAA00);
				case '7' -> Style.EMPTY.withColor(0xAAAAAA);
				case '8' -> Style.EMPTY.withColor(0x555555);
				case '9' -> Style.EMPTY.withColor(0x5555FF);
				case 'a' -> Style.EMPTY.withColor(0x55FF55);
				case 'b' -> Style.EMPTY.withColor(0x55FFFF);
				case 'c' -> Style.EMPTY.withColor(0xFF5555);
				case 'd' -> Style.EMPTY.withColor(0xFF55FF);
				case 'e' -> Style.EMPTY.withColor(0xFFFF55);
				case 'f' -> Style.EMPTY.withColor(0xFFFFFF);

				case 'l', 'L' -> Style.EMPTY.withBold(true);
				case 'o', 'O' -> Style.EMPTY.withItalic(true);
				case 'n', 'N' -> Style.EMPTY.withUnderlined(true);
				case 'm', 'M' -> Style.EMPTY.withStrikethrough(true);
				case 'k', 'K' -> Style.EMPTY.withObfuscated(true);
				case 'r', 'R' -> Style.EMPTY; // reset

				default -> null;
			};
		}

		// Colores HEX: <#ff0000> o < #FF00AA >
		if (name.startsWith("#") || (params.containsKey("value") && params.get("value").startsWith("#"))) {
			String hex = name.startsWith("#") ? name : params.get("value");
			try {
				int color = Integer.parseInt(hex.substring(1), 16);
				return Style.EMPTY.withColor(color);
			} catch (Exception ignored) {
				// []
			}
		}

		// Nombres completos
		return switch (name) {
			case "black" -> Style.EMPTY.withColor(0x000000);
			case "dark_blue" -> Style.EMPTY.withColor(0x0000AA);
			case "dark_green" -> Style.EMPTY.withColor(0x00AA00);
			case "dark_aqua" -> Style.EMPTY.withColor(0x00AAAA);
			case "dark_red" -> Style.EMPTY.withColor(0xAA0000);
			case "dark_purple" -> Style.EMPTY.withColor(0xAA00AA);
			case "gold", "orange" -> Style.EMPTY.withColor(0xFFAA00);
			case "gray" -> Style.EMPTY.withColor(0xAAAAAA);
			case "dark_gray" -> Style.EMPTY.withColor(0x555555);
			case "blue", "light_blue" -> Style.EMPTY.withColor(0x5555FF);
			case "green" -> Style.EMPTY.withColor(0x55FF55);
			case "aqua", "cyan" -> Style.EMPTY.withColor(0x55FFFF);
			case "red" -> Style.EMPTY.withColor(0xFF5555);
			case "light_purple", "pink", "magenta" -> Style.EMPTY.withColor(0xFF55FF);
			case "yellow" -> Style.EMPTY.withColor(0xFFFF55);
			case "white" -> Style.EMPTY.withColor(0xFFFFFF);

			case "bold", "b" -> Style.EMPTY.withBold(true);
			case "italic", "i", "oblique" -> Style.EMPTY.withItalic(true);
			case "underline", "u" -> Style.EMPTY.withUnderlined(true);
			case "strikethrough", "s", "strike" -> Style.EMPTY.withStrikethrough(true);
			case "obfuscated", "obf", "k" -> Style.EMPTY.withObfuscated(true);
			case "reset", "r" -> Style.EMPTY;

			default -> null;
		};
	}
}
