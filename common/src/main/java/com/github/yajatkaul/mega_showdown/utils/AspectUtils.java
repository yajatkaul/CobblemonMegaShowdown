package com.github.yajatkaul.mega_showdown.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature;
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.ActiveBattlePokemon;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.net.messages.client.battle.BattleTransformPokemonPacket;
import com.cobblemon.mod.common.net.messages.client.battle.BattleUpdateTeamPokemonPacket;
import com.cobblemon.mod.common.net.messages.client.pokemon.update.AbilityUpdatePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.properties.UnaspectPropertyType;
import com.github.yajatkaul.mega_showdown.api.codec.Effect;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.gimmick.MaxGimmick;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import com.github.yajatkaul.mega_showdown.tag.MegaShowdownTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;

import java.util.*;

public class AspectUtils {
    public static void applyAspects(Pokemon pokemon, List<String> aspects) {
        for (String aspect : aspects) {
            String[] aspect_split = aspect.split("=");
            if (aspect_split[1].equals("true") || aspect_split[1].equals("false")) {
                new FlagSpeciesFeature(aspect_split[0], Boolean.parseBoolean(aspect_split[1])).apply(pokemon);
            } else {
                new StringSpeciesFeature(aspect_split[0], aspect_split[1]).apply(pokemon);
            }
        }
    }

    public static void appendRevertDataPokemon(Effect effect, List<String> string, Pokemon pokemon, String tagName) {
        EffectPair effectPair = new EffectPair(effect, string);

        List<EffectPair> existing = getRevertDataPokemon(pokemon, tagName);

        existing.add(effectPair);

        Tag encoded =
                EffectPair.CODEC.listOf()
                        .encodeStart(NbtOps.INSTANCE, existing)
                        .getOrThrow();

        pokemon.getPersistentData().put(tagName, encoded);
        pokemon.onChange(null);
    }

    public static List<EffectPair> getRevertDataPokemon(Pokemon pokemon, String tagName) {
        Tag raw = pokemon.getPersistentData().get(tagName);

        if (raw == null) return new ArrayList<>();

        return EffectPair.CODEC.listOf()
                .parse(NbtOps.INSTANCE, raw)
                .result()
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    public static final Set<UUID> battleDisconnecter = new HashSet<>();

    public static void revertPokemonsIfRequiredBattleEnd(ServerPlayer player) {
        if (player == null || battleDisconnecter.contains(player.getUUID())) {
            if (player != null) {
                battleDisconnecter.remove(player.getUUID());
            }
            return;
        }

        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        for (Pokemon pokemon : playerPartyStore) {
            AspectUtils.revertPokemonsIfRequired(pokemon, false);

            if (pokemon.getSpecies().getName().equals("Burmy")) {
                Holder<Biome> biomeHolder = player.serverLevel().getBiome(player.blockPosition());

                boolean isSandy = biomeHolder.is(MegaShowdownTags.Biomes.sandyKey);
                boolean isForest = biomeHolder.is(MegaShowdownTags.Biomes.forestKey);
                boolean isTrash = biomeHolder.is(MegaShowdownTags.Biomes.trashKey);

                if (isForest) {
                    new StringSpeciesFeature("bagworm_cloak", "plant").apply(pokemon);
                } else if (isSandy) {
                    new StringSpeciesFeature("bagworm_cloak", "sandy").apply(pokemon);
                } else if (isTrash) {
                    new StringSpeciesFeature("bagworm_cloak", "trash").apply(pokemon);
                }
            }
        }
    }

    public static void revertPokemonsIfRequired(PlayerPartyStore playerPartyStore) {
        for (Pokemon pokemon : playerPartyStore) {
            AspectUtils.revertPokemonsIfRequired(pokemon, false);
        }
    }

    public static void revertPokemonsIfRequiredBattleStart(PlayerPartyStore playerPartyStore) {
        for (Pokemon pokemon : playerPartyStore) {
            AspectUtils.revertPokemonsIfRequired(pokemon, true);
        }
    }

    public static void revertPokemonsIfRequired(Pokemon pokemon, boolean battleStart) {
        if (battleStart) {
            if (pokemon.getPersistentData().getBoolean(MegaGimmick.IS_MEGA_TAG)) {
                pokemon.getPersistentData().remove(MegaGimmick.IS_MEGA_TAG);
                MegaGimmick.unmegaEvolve(pokemon);
            }
        }

        if (pokemon.getPersistentData().contains("battle_end_revert")) {
            List<EffectPair> aspects = AspectUtils.getRevertDataPokemon(
                    pokemon,
                    "battle_end_revert"
            );

            for (EffectPair effectPair : aspects) {
                effectPair.effect.revertEffects(pokemon, effectPair.aspects, null);
            }

            pokemon.getPersistentData().remove("battle_end_revert");
        }

        if (pokemon.getPersistentData().contains("aspects")) {
            List<EffectPair> aspects = AspectUtils.getRevertDataPokemon(
                    pokemon,
                    "aspects"
            );

            for (EffectPair effectPair : aspects) {
                effectPair.effect.revertEffects(pokemon, effectPair.aspects, null);
            }

            pokemon.getPersistentData().remove("aspects");
        }

        if (pokemon.getPersistentData().contains("revert_aspects")) {
            List<EffectPair> aspects = AspectUtils.getRevertDataPokemon(
                    pokemon,
                    "revert_aspects"
            );

            for (EffectPair effectPair : aspects) {
                effectPair.effect.revertEffects(pokemon, effectPair.aspects, null);
            }

            pokemon.getPersistentData().remove("revert_aspects");
        }

        if (pokemon.getPersistentData().getBoolean("is_tera") || pokemon.getAspects().stream().anyMatch((s) -> s.startsWith("msd:tera_"))) {
            pokemon.getAspects().stream().filter(a -> a.startsWith("msd:tera_")).forEach(name -> {
                UnaspectPropertyType.INSTANCE.fromString(name).apply(pokemon);
            });
            UnaspectPropertyType.INSTANCE.fromString("play_tera").apply(pokemon);
            if (pokemon.getEntity() != null) {
                if (MegaShowdownConfig.legacyTeraEffect) {
                    pokemon.getEntity().removeEffect(MobEffects.GLOWING);
                }
            }
            pokemon.getPersistentData().remove("is_tera");
        }

        if (pokemon.getPersistentData().getBoolean("is_max")) {
            if (pokemon.getAspects().contains("gmax")) {
                Effect.getEffect("mega_showdown:dynamax").revertEffects(pokemon, List.of("dynamax_form=none"), null);
            } else {
                UnaspectPropertyType.INSTANCE.fromString("msd:dmax").apply(pokemon);
                Effect.getEffect("mega_showdown:dynamax").revertEffects(pokemon, List.of(), null);
            }
            if (pokemon.getEntity() != null) {
                MaxGimmick.startGradualScalingDown(pokemon);
            } else {
                pokemon.setScaleModifier(pokemon.getPersistentData().getFloat("orignal_size"));
            }
            pokemon.getPersistentData().remove("is_max");
        }

        if (pokemon.getPersistentData().getBoolean("form_changing")) {
            pokemon.getPersistentData().remove("form_changing");
        }
    }

    public static void updatePackets(BattlePokemon battlePokemon) {
        Pokemon pokemon = battlePokemon.getEntity().getPokemon();
        PokemonBattle battle = battlePokemon.getActor().getBattle();

        if (battlePokemon.getActor().getType() == ActorType.PLAYER) {
            battle.sendUpdate(new AbilityUpdatePacket(
                    battlePokemon::getEffectedPokemon,
                    pokemon.getAbility().getTemplate()
            ));

            battle.sendUpdate(new BattleUpdateTeamPokemonPacket(pokemon));
        }

        for (ActiveBattlePokemon active : battle.getActivePokemon()) {
            if (active.getBattlePokemon() == null) continue;
            if (active.getBattlePokemon() != battlePokemon) continue;

            battle.sendSidedUpdate(
                    active.getActor(),
                    new BattleTransformPokemonPacket(active.getPNX(), battlePokemon, true),
                    new BattleTransformPokemonPacket(active.getPNX(), battlePokemon, false),
                    false
            );
        }
    }

    public record EffectPair(
            Effect effect,
            List<String> aspects
    ) {
        public static final Codec<EffectPair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Effect.CODEC.fieldOf("effect").forGetter(EffectPair::effect),
                Codec.STRING.listOf().fieldOf("aspects").forGetter(EffectPair::aspects)
        ).apply(instance, EffectPair::new));
    }
}
