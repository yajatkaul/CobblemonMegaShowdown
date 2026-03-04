package com.github.yajatkaul.mega_showdown.api.lilycobble.pokemon;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

/**
 * It is not safe to use the PokemonProperties codec in datapacks because it gets validated too early and will drop all datapack-specific
 * data (such as species, forms, etc).
 * <p>
 * This is a safe string-wrapper that can be converted into PokemonProperties on demand.
 */
public record PokemonPropertiesSupplier(String underlying) implements Supplier<PokemonProperties> {
    public static final Codec<PokemonPropertiesSupplier> CODEC = Codec.STRING.xmap(PokemonPropertiesSupplier::new, PokemonPropertiesSupplier::underlying);
    public static final StreamCodec<ByteBuf, PokemonPropertiesSupplier> PACKET_CODEC = ByteBufCodecs.STRING_UTF8.map(PokemonPropertiesSupplier::new, PokemonPropertiesSupplier::underlying);

    public PokemonPropertiesSupplier(PokemonProperties properties) {
        this(properties.asString(" "));
    }

    public PokemonProperties get (String delimiter, String assigner) {
        return PokemonProperties.Companion.parse(this.underlying, delimiter, assigner);
    }

    public PokemonProperties get (String delimiter) {
        return PokemonProperties.Companion.parse(this.underlying, delimiter);
    }

    @Override
    public PokemonProperties get () {
        return PokemonProperties.Companion.parse(this.underlying);
    }
}
