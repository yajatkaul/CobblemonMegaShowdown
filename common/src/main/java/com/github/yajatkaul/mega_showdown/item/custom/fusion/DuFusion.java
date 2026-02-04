package com.github.yajatkaul.mega_showdown.item.custom.fusion;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.components.PokemonStorge;
import com.github.yajatkaul.mega_showdown.item.custom.ToolTipItem;
import com.github.yajatkaul.mega_showdown.utils.PlayerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

public class DuFusion extends ToolTipItem {
    private final List<String> fusions1;
    private final List<String> fusions2;
    private final List<String> pokemons1;
    private final List<String> pokemons2;
    private final List<String> mainPokemons;
    private final String namespace;
    private final List<String> applyAspect1;
    private final List<String> applyAspect2;
    private final List<String> revertAspect1;
    private final List<String> revertAspect2;
    private final String effectId1;
    private final String effectId2;

    public DuFusion(Properties properties,
                    List<String> fusions1,
                    List<String> fusions2,
                    List<String> pokemons1,
                    List<String> pokemons2,
                    List<String> mainPokemons,
                    List<String> applyAspect1,
                    List<String> applyAspect2,
                    List<String> revertAspect1,
                    List<String> revertAspect2,
                    String effectId1,
                    String effectId2,
                    String namespace
    ) {
        super(properties);
        this.fusions1 = fusions1;
        this.fusions2 = fusions2;
        this.pokemons1 = pokemons1;
        this.pokemons2 = pokemons2;
        this.mainPokemons = mainPokemons;
        this.applyAspect1 = applyAspect1;
        this.applyAspect2 = applyAspect2;
        this.revertAspect1 = revertAspect1;
        this.revertAspect2 = revertAspect2;
        this.effectId1 = effectId1;
        this.effectId2 = effectId2;

        this.namespace = namespace;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
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

            if (pokemon.getOwnerPlayer() != player || pokemonEntity.isBattling() || pokemon.getPersistentData().contains("form_changing") || pokemonEntity.getTethering() != null) {
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

                if (pokemons1.contains(pokemonInside.getSpecies().getName())) {
                    Effect.getEffect(effectId1).revertEffects(pokemon, revertAspect1, null);
                } else {
                    Effect.getEffect(effectId2).revertEffects(pokemon, revertAspect2, null);
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

                if (pokemons1.contains(pokemonStored.getSpecies().getName())) {
                    Effect.getEffect(effectId1).applyEffects(pokemon, applyAspect1, null);
                } else {
                    Effect.getEffect(effectId2).applyEffects(pokemon, applyAspect2, null);
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

    @Override
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
