package com.github.yajatkaul.mega_showdown.api.codec;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.components.PokemonStorge;
import com.github.yajatkaul.mega_showdown.utils.PlayerUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;
import java.util.Optional;

public record DuFusion(
        List<String> fusions1,
        List<String> fusions2,
        List<String> pokemons1,
        AspectConditions pokemon_1_aspect_conditions,
        List<String> pokemons2,
        AspectConditions pokemon_2_aspect_conditions,
        List<String> mainPokemons,
        AspectConditions pokemon_main_aspect_conditions,
        Optional<ResourceLocation> effect1,
        Optional<ResourceLocation> effect2
) {
    public static final Codec<DuFusion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("fusions1").forGetter(DuFusion::fusions1),
            Codec.STRING.listOf().fieldOf("fusions2").forGetter(DuFusion::fusions2),
            Codec.STRING.listOf().fieldOf("pokemons1").forGetter(DuFusion::pokemons1),
            AspectConditions.CODEC.fieldOf("pokemon_1_aspect_conditions").forGetter(DuFusion::pokemon_1_aspect_conditions),
            Codec.STRING.listOf().fieldOf("pokemons2").forGetter(DuFusion::pokemons2),
            AspectConditions.CODEC.fieldOf("pokemon_2_aspect_conditions").forGetter(DuFusion::pokemon_1_aspect_conditions),
            Codec.STRING.listOf().fieldOf("main_pokemons").forGetter(DuFusion::mainPokemons),
            AspectConditions.CODEC.fieldOf("pokemon_main_aspect_conditions").forGetter(DuFusion::pokemon_1_aspect_conditions),
            ResourceLocation.CODEC.optionalFieldOf("effect_for_fusion1").forGetter(DuFusion::effect1),
            ResourceLocation.CODEC.optionalFieldOf("effect_for_fusion2").forGetter(DuFusion::effect2)
    ).apply(instance, DuFusion::new));

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        String namespace = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();

        if (level.isClientSide) {
            return InteractionResultHolder.pass(stack);
        }

        EntityHitResult hitResult = PlayerUtils.getEntityLookingAt(player, 4.5f);
        Entity entity = null;
        if (hitResult != null) {
            entity = hitResult.getEntity();
        }

        RegistryAccess registryAccess = level.registryAccess();
        PokemonStorge pokemonStorge = stack.getOrDefault(MegaShowdownDataComponents.POKEMON_STORAGE.get(), PokemonStorge.defaultStorage());
        Pokemon pokemonStored = pokemonStorge.getPokemon(registryAccess);

        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty((ServerPlayer) player);

        if (entity instanceof PokemonEntity pokemonEntity) {
            Pokemon pokemon = pokemonEntity.getPokemon();

            if (pokemonEntity.isBattling() ||
                    pokemon.getOwnerPlayer() != player ||
                    pokemon.getPersistentData().contains("form_changing") ||
                    pokemonEntity.getTethering() != null) {
                return InteractionResultHolder.pass(stack);
            }

            if (mainPokemons.contains(pokemon.getSpecies().getName()) && checkEnabled(pokemon)) {
                if (pokemonStored != null) {
                    player.displayClientMessage(Component.translatable("message.mega_showdown.already_fused")
                            .withStyle(ChatFormatting.RED), true);
                    return InteractionResultHolder.pass(stack);
                }

                Pokemon pokemonInside =
                        Pokemon.Companion.loadFromNBT(
                                level.registryAccess(),
                                pokemon.getPersistentData().getCompound("fusion_pokemon")
                        );
                playerPartyStore.add(pokemonInside);

                if (pokemons1.contains(pokemonInside.getSpecies().getName()) &&
                        pokemon_1_aspect_conditions.validate_revert(pokemon)) {
                    Effect.getEffect(effect1.get()).revertEffects(pokemon, pokemon_1_aspect_conditions.aspectRevert().aspects(), null);
                } else if (pokemon_2_aspect_conditions.validate_revert(pokemon)) {
                    Effect.getEffect(effect2.get()).revertEffects(pokemon, pokemon_2_aspect_conditions.aspectRevert().aspects(), null);
                } else {
                    return InteractionResultHolder.pass(stack);
                }

                pokemon.setTradeable(true);

                stack.remove(MegaShowdownDataComponents.POKEMON_STORAGE.get());
                stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.mega_showdown." + namespace + ".inactive"));
            } else if (pokemonStored != null && mainPokemons.contains(pokemon.getSpecies().getName())) {
                pokemon.setTradeable(false);

                CompoundTag otherPokemonNbt = pokemonStored.saveToNBT(level.registryAccess(), new CompoundTag());
                pokemon.getPersistentData().put("fusion_pokemon", otherPokemonNbt);

                stack.remove(MegaShowdownDataComponents.POKEMON_STORAGE.get());
                stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.mega_showdown." + namespace + ".inactive"));
                pokemon.setTradeable(false);

                if (pokemons1.contains(pokemonStored.getSpecies().getName()) &&
                        pokemon_1_aspect_conditions.validate_apply(pokemon)) {
                    Effect.getEffect(effect1.get()).applyEffects(pokemon, pokemon_1_aspect_conditions.aspectApply().aspects(), null);
                } else if (pokemon_2_aspect_conditions.validate_apply(pokemon)) {
                    Effect.getEffect(effect1.get()).applyEffects(pokemon, pokemon_2_aspect_conditions.aspectApply().aspects(), null);
                } else {
                    return InteractionResultHolder.pass(stack);
                }

            } else if (pokemonStored == null &&
                    pokemons1.contains(pokemon.getSpecies().getName()) ||
                    pokemons2.contains(pokemon.getSpecies().getName())
            ) {
                stack.set(MegaShowdownDataComponents.POKEMON_STORAGE.get(), pokemonStorge.save(registryAccess, pokemon));
                stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.mega_showdown." + namespace + ".charged"));

                playerPartyStore.remove(pokemon);
            }
        } else if (pokemonStored != null) {
            playerPartyStore.add(pokemonStored);
            stack.remove(MegaShowdownDataComponents.POKEMON_STORAGE.get());
            stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.mega_showdown." + namespace + ".inactive"));
        }

        return InteractionResultHolder.pass(stack);
    }

    private boolean checkEnabled(Pokemon pokemon) {
        return pokemon.getAspects().stream().anyMatch(fusions1::contains) || pokemon.getAspects().stream().anyMatch(fusions2::contains);
    }

    public void onDestroyed(ItemEntity itemEntity) {
        PokemonStorge pokemonStorge = itemEntity.getItem().getOrDefault(MegaShowdownDataComponents.POKEMON_STORAGE.get(), PokemonStorge.defaultStorage());
        Pokemon pokemonStored = pokemonStorge.getPokemon(itemEntity.registryAccess());

        if (pokemonStored != null) {
            if (itemEntity.getOwner() instanceof ServerPlayer player) {
                PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
                playerPartyStore.add(pokemonStored);
            }
        }
    }
}