/**
 * Licence LGPL see: {@code text_effects LICENSE}
 **/

package com.github.razorplay01.text_styles.mixin;

import com.github.razorplay01.text_styles.styles.TextStyle;
import com.github.razorplay01.text_styles.util.StyleExtension;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources./*? >= 1.21.11 {*/ Identifier /*?} else { */ /*ResourceLocation *//*?} */;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mixin(Style.class)
public abstract class StyleMixin implements StyleExtension {

	@Shadow
	private static <T> Style checkEmptyAfterChange(Style newStyle, T previous, T next) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private TextColor color;
	//? >1.21.3 {
	@Shadow
	@Final
			//? > 1.21.11 >>
	private Integer shadowColor;
	//? }
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private Boolean bold;
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private Boolean italic;
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private Boolean underlined;
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private Boolean strikethrough;
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private Boolean obfuscated;
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private ClickEvent clickEvent;
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private HoverEvent hoverEvent;
	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private String insertion;

	@Shadow
	@Final
			/*? > 1.21.11 || <=1.16.5 >>*/ private /*? >= 1.21.10 {*/ net.minecraft.network.chat.FontDescription/*? } else {*/ /*/^? >= 1.21.11 {^/ Identifier /^?} else { ^/ /^ResourceLocation ^//^?} ^/ *//*? }*/ font;
	@Unique
	private List<TextStyle.TextStyleInstance> textStyles = null;

	@Override
	public Collection<TextStyle.TextStyleInstance> getStyles() {
		return textStyles != null ? Collections.unmodifiableList(textStyles) : null;
	}

	@Definition(id = "bold", field = "Lnet/minecraft/network/chat/Style;bold:Ljava/lang/Boolean;")
	@Expression("this.bold == ?")
	@WrapOperation(method = "equals", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private boolean includeEffectsInEquals(Object left, Object right, Operation<Boolean> original, @Local(name = "style") Style style) {
		return original.call(left, right) && Objects.equals(((StyleExtension) (Object) style).getStyles(), textStyles);
	}

	@ModifyReturnValue(method = "hashCode", at = @At(value = "TAIL"))
	private int includeEffectsInHashCode(int original) {
		return original * 31 + Objects.hashCode(textStyles);
	}


	@Inject(method = "toString", at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;"))
	private void includeEffectsInToString(CallbackInfoReturnable<String> cir, @Local(/*? >= 26 {*/ name = "result"/*? }*/) StringBuilder result) {
		if (textStyles != null) {
			if (result.length() > 1) {
				result.append(',');
			}
			result.append("styles=").append(textStyles);
		}
	}

	@SuppressWarnings("DataFlowIssue")
	@ModifyReturnValue(method = "applyTo", at = @At("TAIL"))
	private Style includeEffectsInApplyTo(Style original, @Local(argsOnly = true/*? >= 26 {*/, name = "other" /*? }*/) Style other) {
		if (original != (Style) (Object) this) {
			if (textStyles != null) {
				((StyleMixin) (Object) original).setTextStyles(textStyles);
			} else {
				((StyleMixin) (Object) original).setTextStyles(((StyleExtension) (Object) other).getStyles());
			}
		}
		return original;
	}

	@Unique
	private void setTextStyles(Collection<TextStyle.TextStyleInstance> styles) {
		this.textStyles = styles != null ? new ArrayList<>(styles) : null;
	}

	@Override
	public Style withStyle(TextStyle.TextStyleInstance style) {
		Style copy = copy();
		List<TextStyle.TextStyleInstance> newStyles = textStyles != null ? new ArrayList<>(textStyles) : new ArrayList<>();
		newStyles.add(style);
		((StyleMixin) (Object) copy).setTextStyles(newStyles);
		return copy;
	}

	@Override
	public Style withStyles(Collection<TextStyle.TextStyleInstance> styles) {
		Style copy = copy();
		((StyleMixin) (Object) copy).setTextStyles(styles);
		return checkEmptyAfterChange(copy, this.textStyles, ((StyleExtension) (Object) copy).getStyles());
	}

	@Unique
	private Style copy() {
		return new Style(this.color, /*? >1.21.3 >>*/this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
	}

	@ModifyReturnValue(method = {"applyFormats", "applyFormat", "applyLegacyFormat"}, at = @At("TAIL"))
	private Style includeEffectsInApplyFormats(Style original) {
		((StyleMixin) (Object) original).setTextStyles(textStyles);
		return original;
	}

	@ModifyArg(method = {
			"withFont", "withBold", "withClickEvent", "withColor(I)Lnet/minecraft/network/chat/Style;",
			"withColor(Lnet/minecraft/network/chat/TextColor;)Lnet/minecraft/network/chat/Style;",
			"withColor(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/Style;",
			"withHoverEvent", "withInsertion", "withItalic", "withObfuscated",
			/*? >1.21.3  >>*/"withShadowColor",
			"withStrikethrough", "withUnderlined"}, at = @At(value =
			"INVOKE", target = "Lnet/minecraft/network/chat/Style;checkEmptyAfterChange(Lnet/minecraft/network/chat/Style;Ljava/lang/Object;Ljava/lang/Object;)Lnet/minecraft/network/chat/Style;"
	))
	private Style includeEffectsInWith(Style newStyle) {
		((StyleMixin) (Object) newStyle).setTextStyles(textStyles);
		return newStyle;
	}
}
