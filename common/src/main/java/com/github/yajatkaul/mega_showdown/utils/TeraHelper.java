package com.github.yajatkaul.mega_showdown.utils;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.api.types.tera.TeraTypes;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;

public class TeraHelper {
    public static Item getTeraShardForType(Iterable<ElementalType> types) {
        RegistrySupplier<Item> shard = MegaShowdownItems.NORMAL_TERA_SHARD;

        for (ElementalType type : types) {
            if (type == null) continue;

            shard = switch (type.getName().toLowerCase()) {
                case "bug" -> MegaShowdownItems.BUG_TERA_SHARD;
                case "dark" -> MegaShowdownItems.DARK_TERA_SHARD;
                case "dragon" -> MegaShowdownItems.DRAGON_TERA_SHARD;
                case "electric" -> MegaShowdownItems.ELECTRIC_TERA_SHARD;
                case "fairy" -> MegaShowdownItems.FAIRY_TERA_SHARD;
                case "fighting" -> MegaShowdownItems.FIGHTING_TERA_SHARD;
                case "fire" -> MegaShowdownItems.FIRE_TERA_SHARD;
                case "flying" -> MegaShowdownItems.FLYING_TERA_SHARD;
                case "ghost" -> MegaShowdownItems.GHOST_TERA_SHARD;
                case "grass" -> MegaShowdownItems.GRASS_TERA_SHARD;
                case "ground" -> MegaShowdownItems.GROUND_TERA_SHARD;
                case "ice" -> MegaShowdownItems.ICE_TERA_SHARD;
                case "normal" -> MegaShowdownItems.NORMAL_TERA_SHARD;
                case "poison" -> MegaShowdownItems.POISON_TERA_SHARD;
                case "psychic" -> MegaShowdownItems.PSYCHIC_TERA_SHARD;
                case "rock" -> MegaShowdownItems.ROCK_TERA_SHARD;
                case "steel" -> MegaShowdownItems.STEEL_TERA_SHARD;
                case "water" -> MegaShowdownItems.WATER_TERA_SHARD;
                default -> shard;
            };
        }

        return shard.get();
    }

    public static TeraType getTeraFromElement(ElementalType type) {
        if (type == null) return TeraTypes.getNORMAL();

        return switch (type.getName().toLowerCase()) {
            case "bug" -> TeraTypes.getBUG();
            case "dark" -> TeraTypes.getDARK();
            case "dragon" -> TeraTypes.getDRAGON();
            case "electric" -> TeraTypes.getELECTRIC();
            case "fairy" -> TeraTypes.getFAIRY();
            case "fighting" -> TeraTypes.getFIGHTING();
            case "fire" -> TeraTypes.getFIRE();
            case "flying" -> TeraTypes.getFLYING();
            case "ghost" -> TeraTypes.getGHOST();
            case "grass" -> TeraTypes.getGRASS();
            case "ground" -> TeraTypes.getGROUND();
            case "ice" -> TeraTypes.getICE();
            case "poison" -> TeraTypes.getPOISON();
            case "psychic" -> TeraTypes.getPSYCHIC();
            case "rock" -> TeraTypes.getROCK();
            case "steel" -> TeraTypes.getSTEEL();
            case "water" -> TeraTypes.getWATER();
            default -> TeraTypes.getNORMAL();
        };
    }

    public static String getTeraAnimationFromAspect(String aspect) {
        if (aspect == null) {
            return "cobblemon:tera_normal";
        }

        return switch (aspect) {
            case "msd:tera_bug" -> "cobblemon:tera_bug";
            case "msd:tera_dark" -> "cobblemon:tera_dark";
            case "msd:tera_dragon" -> "cobblemon:tera_dragon";
            case "msd:tera_electric" -> "cobblemon:tera_electric";
            case "msd:tera_fairy" -> "cobblemon:tera_fairy";
            case "msd:tera_fighting" -> "cobblemon:tera_fighting";
            case "msd:tera_fire" -> "cobblemon:tera_fire";
            case "msd:tera_flying" -> "cobblemon:tera_flying";
            case "msd:tera_ghost" -> "cobblemon:tera_ghost";
            case "msd:tera_grass" -> "cobblemon:tera_grass";
            case "msd:tera_ground" -> "cobblemon:tera_ground";
            case "msd:tera_ice" -> "cobblemon:tera_ice";
            case "msd:tera_poison" -> "cobblemon:tera_poison";
            case "msd:tera_psychic" -> "cobblemon:tera_psychic";
            case "msd:tera_rock" -> "cobblemon:tera_rock";
            case "msd:tera_steel" -> "cobblemon:tera_steel";
            case "msd:tera_water" -> "cobblemon:tera_water";

            default -> "cobblemon:tera_normal";
        };
    }
}
