package com.github.razorplay01.text_styles.mixin;

import com.github.razorplay01.text_styles.ModTemplate;
import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.StyleExtension;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.function.Function;

@Mixin(Style.Serializer.class)
public abstract class StyleSerializerMixin {
	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"))
	private static MapCodec<Style> modifyCodec(Function<RecordCodecBuilder.Instance<Style>, ? extends App<RecordCodecBuilder.Mu<Style>, Style>> builder, Operation<MapCodec<Style>> original) {
		return original.call(builder).mapResult(new MapCodec.ResultFunction<>() {

			@Override
			public <T> DataResult<Style> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<Style> a) {
				var list = input.get(ModTemplate.MOD_ID);
				if (list == null) return a;
				return a.flatMap(style -> {
					var result = TextStyles.LIST_CODEC.decode(ops, list);
					return result.map(pair -> ((StyleExtension) (Object) style).withStyles(pair.getFirst()));
				});
			}

			@Override
			public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, Style input, RecordBuilder<T> t) {
				var styles = ((StyleExtension) (Object) input).getStyles();
				if (styles != null && !styles.isEmpty()) {
					return t.add(ModTemplate.MOD_ID, TextStyles.LIST_CODEC.encodeStart(ops, new ArrayList<>(styles)));
				}
				return t;
			}
		});
	}
}
