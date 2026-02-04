package com.github.yajatkaul.mega_showdown.api.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public record BattleEffect(
        BattleEffectType type,
        Integer tickInterval,
        String name,
        ResourceLocation id
) {
    public static final Codec<BattleEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BattleEffectType.CODEC.fieldOf("type").forGetter(BattleEffect::type),
            Codec.INT.fieldOf("tickInterval").forGetter(BattleEffect::tickInterval),
            Codec.STRING.fieldOf("name").forGetter(BattleEffect::name),
            ResourceLocation.CODEC.fieldOf("id").forGetter(BattleEffect::id)
    ).apply(instance, BattleEffect::new));

    public enum BattleEffectType implements StringRepresentable {
        HAZARD,
        WEATHER;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }

        public static final Codec<BattleEffectType> CODEC =
                StringRepresentable.fromEnum((BattleEffectType::values));
    }
}
