package com.github.yajatkaul.mega_showdown.utils;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.api.types.tera.elemental.ElementalTypeTeraType;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.codec.ZCrystal;
import net.minecraft.ChatFormatting;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.scores.PlayerTeam;

import java.util.Locale;

public class GlowHandler {
    public static void applyDynamaxGlow(PokemonEntity pokemonEntity) {
        if (pokemonEntity.level() instanceof ServerLevel serverLevel) {
            pokemonEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, Integer.MAX_VALUE, 0, false, false));
            ServerScoreboard scoreboard = serverLevel.getScoreboard();

            String teamName;
            ChatFormatting teamColour;
            if (pokemonEntity.getPokemon().getSpecies().getName().equalsIgnoreCase("calyrex")) {
                teamName = "glow_dynamax_blue";
                teamColour = ChatFormatting.BLUE;
            } else {
                teamName = "glow_dynamax_red";
                teamColour = ChatFormatting.RED;
            }

            PlayerTeam team = scoreboard.getPlayerTeam(teamName);
            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
                team.setColor(teamColour);
            }
            scoreboard.addPlayerToTeam(pokemonEntity.getScoreboardName(), team);
        }
    }

    public static void applyTeraGlow(PokemonEntity pokemon) {
        if (pokemon.level() instanceof ServerLevel serverLevel) {
            pokemon.addEffect(new MobEffectInstance(MobEffects.GLOWING, Integer.MAX_VALUE, 0, false, false));
            ServerScoreboard scoreboard = serverLevel.getScoreboard();
            String teamName = "glow_tera_" + pokemon.getPokemon().getTeraType().showdownId();

            PlayerTeam team = scoreboard.getPlayerTeam(teamName);

            ChatFormatting color = getGlowForTera(pokemon.getPokemon().getTeraType());
            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
                team.setColor(color);
            }

            scoreboard.addPlayerToTeam(pokemon.getScoreboardName(), team);
        }
    }

    public static void applyZGlow(PokemonEntity pokemon, ZCrystal zCrystal) {
        if (pokemon.level() instanceof ServerLevel serverLevel) {
            pokemon.addEffect(new MobEffectInstance(MobEffects.GLOWING, 140, 0, false, false));
            ServerScoreboard scoreboard = serverLevel.getScoreboard();
            String teamName = "glow_type_" + zCrystal.color().toLowerCase(Locale.ROOT);

            PlayerTeam team = scoreboard.getPlayerTeam(teamName);

            ChatFormatting color = getGlowForColor(zCrystal.color());
            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
                team.setColor(color);
            }

            scoreboard.addPlayerToTeam(pokemon.getScoreboardName(), team);
        }
    }

    private static ChatFormatting getGlowForTera(TeraType teraType) {
        if (teraType instanceof ElementalTypeTeraType elementalTera) {
            return getGlowForElemental(elementalTera.getType());
        }
        return ChatFormatting.WHITE;
    }

    private static ChatFormatting getGlowForElemental(ElementalType type) {
        if (type.equals(ElementalTypes.BUG)) return ChatFormatting.DARK_GREEN;
        if (type.equals(ElementalTypes.DARK)) return ChatFormatting.BLACK;
        if (type.equals(ElementalTypes.DRAGON)) return ChatFormatting.DARK_BLUE;
        if (type.equals(ElementalTypes.ELECTRIC)) return ChatFormatting.YELLOW;
        if (type.equals(ElementalTypes.FAIRY)) return ChatFormatting.LIGHT_PURPLE;
        if (type.equals(ElementalTypes.FIGHTING)) return ChatFormatting.DARK_RED;
        if (type.equals(ElementalTypes.FIRE)) return ChatFormatting.RED;
        if (type.equals(ElementalTypes.FLYING)) return ChatFormatting.GRAY;
        if (type.equals(ElementalTypes.GHOST)) return ChatFormatting.DARK_PURPLE;
        if (type.equals(ElementalTypes.GRASS)) return ChatFormatting.GREEN;
        if (type.equals(ElementalTypes.GROUND)) return ChatFormatting.DARK_RED;
        if (type.equals(ElementalTypes.ICE)) return ChatFormatting.AQUA;
        if (type.equals(ElementalTypes.NORMAL)) return ChatFormatting.WHITE;
        if (type.equals(ElementalTypes.POISON)) return ChatFormatting.DARK_PURPLE;
        if (type.equals(ElementalTypes.PSYCHIC)) return ChatFormatting.LIGHT_PURPLE;
        if (type.equals(ElementalTypes.ROCK)) return ChatFormatting.DARK_GRAY;
        if (type.equals(ElementalTypes.STEEL)) return ChatFormatting.GRAY;
        if (type.equals(ElementalTypes.WATER)) return ChatFormatting.BLUE;
        return ChatFormatting.WHITE;
    }

    private static ChatFormatting getGlowForColor(String color) {
        if (color == null) return ChatFormatting.WHITE;

        return switch (color.toLowerCase()) {
            case "black" -> ChatFormatting.BLACK;
            case "dark_blue" -> ChatFormatting.DARK_BLUE;
            case "dark_green" -> ChatFormatting.DARK_GREEN;
            case "dark_aqua" -> ChatFormatting.DARK_AQUA;
            case "dark_red" -> ChatFormatting.DARK_RED;
            case "dark_purple" -> ChatFormatting.DARK_PURPLE;
            case "gold" -> ChatFormatting.GOLD;
            case "gray" -> ChatFormatting.GRAY;
            case "dark_gray" -> ChatFormatting.DARK_GRAY;
            case "blue" -> ChatFormatting.BLUE;
            case "green" -> ChatFormatting.GREEN;
            case "aqua" -> ChatFormatting.AQUA;
            case "red" -> ChatFormatting.RED;
            case "light_purple" -> ChatFormatting.LIGHT_PURPLE;
            case "yellow" -> ChatFormatting.YELLOW;
            default -> ChatFormatting.WHITE;
        };
    }

    public static float[] getTeraColor(String teraId) {
        if (teraId == null) {
            return WHITE;
        }

        return switch (teraId) {
            case "msd:tera_fire"      -> FIRE;
            case "msd:tera_water"     -> WATER;
            case "msd:tera_grass"     -> GRASS;
            case "msd:tera_electric"  -> ELECTRIC;
            case "msd:tera_ice"       -> ICE;
            case "msd:tera_fighting"  -> FIGHTING;
            case "msd:tera_poison"    -> POISON;
            case "msd:tera_ground"    -> GROUND;
            case "msd:tera_flying"    -> FLYING;
            case "msd:tera_psychic"   -> PSYCHIC;
            case "msd:tera_bug"       -> BUG;
            case "msd:tera_rock"      -> ROCK;
            case "msd:tera_ghost"     -> GHOST;
            case "msd:tera_dragon"    -> DRAGON;
            case "msd:tera_dark"      -> DARK;
            case "msd:tera_steel"     -> STEEL;
            case "msd:tera_fairy"     -> FAIRY;
            case "msd:tera_normal"    -> NORMAL;
            default                   -> WHITE;
        };
    }

    private static final float[] WHITE     = {1.0f, 1.0f, 1.0f, 1.0f};
    private static final float[] FIRE      = {1.0f, 0.30f, 0.20f, 1.0f};
    private static final float[] WATER     = {0.30f, 0.60f, 1.00f, 1.0f};
    private static final float[] GRASS     = {0.30f, 1.00f, 0.40f, 1.0f};
    private static final float[] ELECTRIC  = {1.00f, 1.00f, 0.30f, 1.0f};
    private static final float[] ICE       = {0.60f, 0.90f, 1.00f, 1.0f};
    private static final float[] FIGHTING  = {0.80f, 0.30f, 0.20f, 1.0f};
    private static final float[] POISON    = {0.70f, 0.30f, 0.80f, 1.0f};
    private static final float[] GROUND    = {0.80f, 0.65f, 0.40f, 1.0f};
    private static final float[] FLYING    = {0.70f, 0.70f, 1.00f, 1.0f};
    private static final float[] PSYCHIC   = {1.00f, 0.40f, 0.70f, 1.0f};
    private static final float[] BUG       = {0.60f, 0.80f, 0.20f, 1.0f};
    private static final float[] ROCK      = {0.70f, 0.60f, 0.40f, 1.0f};
    private static final float[] GHOST     = {0.60f, 0.50f, 0.90f, 1.0f};
    private static final float[] DRAGON    = {0.50f, 0.40f, 1.00f, 1.0f};
    private static final float[] DARK      = {0.30f, 0.30f, 0.30f, 1.0f};
    private static final float[] STEEL     = {0.70f, 0.75f, 0.80f, 1.0f};
    private static final float[] FAIRY     = {1.00f, 0.60f, 0.90f, 1.0f};
    private static final float[] NORMAL    = {0.90f, 0.90f, 0.90f, 1.0f};
}