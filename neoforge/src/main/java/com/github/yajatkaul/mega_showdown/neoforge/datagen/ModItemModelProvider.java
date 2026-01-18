package com.github.yajatkaul.mega_showdown.neoforge.datagen;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlocks;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MegaShowdown.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MegaShowdownItems.KEYSTONE.get());
        basicItem(MegaShowdownItems.MEGA_STONE.get());
        basicItem(MegaShowdownItems.VENUSAURITE.get());
        basicItem(MegaShowdownItems.CHARIZARDITE_X.get());
        basicItem(MegaShowdownItems.CHARIZARDITE_Y.get());
        basicItem(MegaShowdownItems.BLASTOISINITE.get());
        basicItem(MegaShowdownItems.BEEDRILLITE.get());
        basicItem(MegaShowdownItems.PIDGEOTITE.get());
        basicItem(MegaShowdownItems.ALAKAZITE.get());
        basicItem(MegaShowdownItems.SLOWBRONITE.get());
        basicItem(MegaShowdownItems.GENGARITE.get());
        basicItem(MegaShowdownItems.KANGASKHANITE.get());
        basicItem(MegaShowdownItems.PINSIRITE.get());
        basicItem(MegaShowdownItems.GYARADOSITE.get());
        basicItem(MegaShowdownItems.AERODACTYLITE.get());
        basicItem(MegaShowdownItems.MEWTWONITE_X.get());
        basicItem(MegaShowdownItems.MEWTWONITE_Y.get());
        basicItem(MegaShowdownItems.AMPHAROSITE.get());
        basicItem(MegaShowdownItems.STEELIXITE.get());
        basicItem(MegaShowdownItems.SCIZORITE.get());
        basicItem(MegaShowdownItems.HERACRONITE.get());
        basicItem(MegaShowdownItems.HOUNDOOMINITE.get());
        basicItem(MegaShowdownItems.TYRANITARITE.get());
        basicItem(MegaShowdownItems.SCEPTILITE.get());
        basicItem(MegaShowdownItems.BLAZIKENITE.get());
        basicItem(MegaShowdownItems.SWAMPERTITE.get());
        basicItem(MegaShowdownItems.GARDEVOIRITE.get());
        basicItem(MegaShowdownItems.SABLENITE.get());
        basicItem(MegaShowdownItems.MAWILITE.get());
        basicItem(MegaShowdownItems.AGGRONITE.get());
        basicItem(MegaShowdownItems.MEDICHAMITE.get());
        basicItem(MegaShowdownItems.MANECTITE.get());
        basicItem(MegaShowdownItems.SHARPEDONITE.get());
        basicItem(MegaShowdownItems.CAMERUPTITE.get());
        basicItem(MegaShowdownItems.ALTARIANITE.get());
        basicItem(MegaShowdownItems.BANETTITE.get());
        basicItem(MegaShowdownItems.ABSOLITE.get());
        basicItem(MegaShowdownItems.GALLADITE.get());
        basicItem(MegaShowdownItems.SALAMENCITE.get());
        basicItem(MegaShowdownItems.METAGROSSITE.get());
        basicItem(MegaShowdownItems.LATIASITE.get());
        basicItem(MegaShowdownItems.LATIOSITE.get());
        basicItem(MegaShowdownItems.LOPUNNITE.get());
        basicItem(MegaShowdownItems.GARCHOMPITE.get());
        basicItem(MegaShowdownItems.LUCARIONITE.get());
        basicItem(MegaShowdownItems.ABOMASITE.get());
        basicItem(MegaShowdownItems.GLALITITE.get());
        basicItem(MegaShowdownItems.AUDINITE.get());
        basicItem(MegaShowdownItems.DIANCITE.get());

        basicItem(MegaShowdownItems.NORMAL_TERA_SHARD.get());
        basicItem(MegaShowdownItems.FIRE_TERA_SHARD.get());
        basicItem(MegaShowdownItems.WATER_TERA_SHARD.get());
        basicItem(MegaShowdownItems.ELECTRIC_TERA_SHARD.get());
        basicItem(MegaShowdownItems.GRASS_TERA_SHARD.get());
        basicItem(MegaShowdownItems.ICE_TERA_SHARD.get());
        basicItem(MegaShowdownItems.FIGHTING_TERA_SHARD.get());
        basicItem(MegaShowdownItems.POISON_TERA_SHARD.get());
        basicItem(MegaShowdownItems.GROUND_TERA_SHARD.get());
        basicItem(MegaShowdownItems.FLYING_TERA_SHARD.get());
        basicItem(MegaShowdownItems.PSYCHIC_TERA_SHARD.get());
        basicItem(MegaShowdownItems.BUG_TERA_SHARD.get());
        basicItem(MegaShowdownItems.ROCK_TERA_SHARD.get());
        basicItem(MegaShowdownItems.GHOST_TERA_SHARD.get());
        basicItem(MegaShowdownItems.DRAGON_TERA_SHARD.get());
        basicItem(MegaShowdownItems.DARK_TERA_SHARD.get());
        basicItem(MegaShowdownItems.STEEL_TERA_SHARD.get());
        basicItem(MegaShowdownItems.FAIRY_TERA_SHARD.get());
        basicItem(MegaShowdownItems.STELLAR_TERA_SHARD.get());

        basicItem(MegaShowdownItems.NORMALIUM_Z.get());
        basicItem(MegaShowdownItems.FIRIUM_Z.get());
        basicItem(MegaShowdownItems.WATERIUM_Z.get());
        basicItem(MegaShowdownItems.ELECTRIUM_Z.get());
        basicItem(MegaShowdownItems.GRASSIUM_Z.get());
        basicItem(MegaShowdownItems.ICIUM_Z.get());
        basicItem(MegaShowdownItems.FIGHTINIUM_Z.get());
        basicItem(MegaShowdownItems.POISONIUM_Z.get());
        basicItem(MegaShowdownItems.GROUNDIUM_Z.get());
        basicItem(MegaShowdownItems.FLYINIUM_Z.get());
        basicItem(MegaShowdownItems.PSYCHIUM_Z.get());
        basicItem(MegaShowdownItems.BUGINIUM_Z.get());
        basicItem(MegaShowdownItems.ROCKIUM_Z.get());
        basicItem(MegaShowdownItems.GHOSTIUM_Z.get());
        basicItem(MegaShowdownItems.DRAGONIUM_Z.get());
        basicItem(MegaShowdownItems.DARKINIUM_Z.get());
        basicItem(MegaShowdownItems.STEELIUM_Z.get());
        basicItem(MegaShowdownItems.FAIRIUM_Z.get());
        basicItem(MegaShowdownItems.PIKANIUM_Z.get());
        basicItem(MegaShowdownItems.EEVIUM_Z.get());
        basicItem(MegaShowdownItems.SNORLIUM_Z.get());
        basicItem(MegaShowdownItems.MEWNIUM_Z.get());
        basicItem(MegaShowdownItems.DECIDIUM_Z.get());
        basicItem(MegaShowdownItems.INCINIUM_Z.get());
        basicItem(MegaShowdownItems.PRIMARIUM_Z.get());
        basicItem(MegaShowdownItems.LYCANIUM_Z.get());
        basicItem(MegaShowdownItems.MARSHADIUM_Z.get());
        basicItem(MegaShowdownItems.ALORAICHIUM_Z.get());
        basicItem(MegaShowdownItems.KOMMONIUM_Z.get());
        basicItem(MegaShowdownItems.TAPUNIUM_Z.get());
        basicItem(MegaShowdownItems.SOLGANIUM_Z.get());
        basicItem(MegaShowdownItems.LUNALIUM_Z.get());
        basicItem(MegaShowdownItems.ULTRANECROZIUM_Z.get());
        basicItem(MegaShowdownItems.PIKASHUNIUM_Z.get());
        basicItem(MegaShowdownItems.MIMIKIUM_Z.get());
        basicItem(MegaShowdownItems.BLANK_Z.get());
        basicItem(MegaShowdownItems.SPARKLING_STONE_DARK.get());
        basicItem(MegaShowdownItems.SPARKLING_STONE_LIGHT.get());

        basicItem(MegaShowdownItems.RED_ORB.get());
        basicItem(MegaShowdownItems.BLUE_ORB.get());

        basicItem(MegaShowdownItems.DNA_SPLICER.get());
        basicItem(MegaShowdownItems.REINS_OF_UNITY.get());
        basicItem(MegaShowdownItems.N_LUNARIZER.get());
        basicItem(MegaShowdownItems.N_SOLARIZER.get());

        basicItem(MegaShowdownItems.DEBUG_STICK.get());

        basicItem(MegaShowdownItems.DYNAMAX_CANDY.get());
        basicItem(MegaShowdownItems.MAX_HONEY.get());
        basicItem(MegaShowdownItems.MAX_SOUP.get());
        basicItem(MegaShowdownItems.SWEET_MAX_SOUP.get());
        basicItem(MegaShowdownItems.WISHING_STAR.get());

        basicItem(MegaShowdownItems.ZYGARDE_CELL.get());
        basicItem(MegaShowdownItems.ZYGARDE_CORE.get());

        basicItem(MegaShowdownItems.PINK_NECTAR.get());
        basicItem(MegaShowdownItems.PURPLE_NECTAR.get());
        basicItem(MegaShowdownItems.RED_NECTAR.get());
        basicItem(MegaShowdownItems.YELLOW_NECTAR.get());

        basicItem(MegaShowdownItems.CORNERSTONE_MASK.get());
        basicItem(MegaShowdownItems.WELLSPRING_MASK.get());
        basicItem(MegaShowdownItems.HEARTHFLAME_MASK.get());

        basicItem(MegaShowdownItems.GRISEOUS_CORE.get());
        basicItem(MegaShowdownItems.ADAMANT_CRYSTAL.get());
        basicItem(MegaShowdownItems.LUSTROUS_GLOBE.get());

        basicItem(MegaShowdownItems.ASH_CAP.get());

        basicItem(MegaShowdownItems.FLAME_PLATE.get());
        basicItem(MegaShowdownItems.SPLASH_PLATE.get());
        basicItem(MegaShowdownItems.ZAP_PLATE.get());
        basicItem(MegaShowdownItems.MEADOW_PLATE.get());
        basicItem(MegaShowdownItems.ICICLE_PLATE.get());
        basicItem(MegaShowdownItems.FIST_PLATE.get());
        basicItem(MegaShowdownItems.TOXIC_PLATE.get());
        basicItem(MegaShowdownItems.EARTH_PLATE.get());
        basicItem(MegaShowdownItems.SKY_PLATE.get());
        basicItem(MegaShowdownItems.MIND_PLATE.get());
        basicItem(MegaShowdownItems.INSECT_PLATE.get());
        basicItem(MegaShowdownItems.STONE_PLATE.get());
        basicItem(MegaShowdownItems.SPOOKY_PLATE.get());
        basicItem(MegaShowdownItems.DRACO_PLATE.get());
        basicItem(MegaShowdownItems.DREAD_PLATE.get());
        basicItem(MegaShowdownItems.IRON_PLATE.get());
        basicItem(MegaShowdownItems.PIXIE_PLATE.get());

        basicItem(MegaShowdownItems.BUG_MEMORY.get());
        basicItem(MegaShowdownItems.DARK_MEMORY.get());
        basicItem(MegaShowdownItems.DRAGON_MEMORY.get());
        basicItem(MegaShowdownItems.ELECTRIC_MEMORY.get());
        basicItem(MegaShowdownItems.FAIRY_MEMORY.get());
        basicItem(MegaShowdownItems.FIGHTING_MEMORY.get());
        basicItem(MegaShowdownItems.FIRE_MEMORY.get());
        basicItem(MegaShowdownItems.FLYING_MEMORY.get());
        basicItem(MegaShowdownItems.GHOST_MEMORY.get());
        basicItem(MegaShowdownItems.GRASS_MEMORY.get());
        basicItem(MegaShowdownItems.GROUND_MEMORY.get());
        basicItem(MegaShowdownItems.ICE_MEMORY.get());
        basicItem(MegaShowdownItems.POISON_MEMORY.get());
        basicItem(MegaShowdownItems.PSYCHIC_MEMORY.get());
        basicItem(MegaShowdownItems.ROCK_MEMORY.get());
        basicItem(MegaShowdownItems.STEEL_MEMORY.get());
        basicItem(MegaShowdownItems.WATER_MEMORY.get());

        basicItem(MegaShowdownItems.BURN_DRIVE.get());
        basicItem(MegaShowdownItems.CHILL_DRIVE.get());
        basicItem(MegaShowdownItems.DOUSE_DRIVE.get());
        basicItem(MegaShowdownItems.SHOCK_DRIVE.get());

        basicItem(MegaShowdownItems.RUSTED_SWORD.get());
        basicItem(MegaShowdownItems.RUSTED_SHIELD.get());

        basicItem(MegaShowdownItems.PRISON_BOTTLE.get());
        basicItem(MegaShowdownItems.PIKA_CASE.get());

        basicItem(MegaShowdownItems.ROTOM_CATALOGUE.get());

        basicItem(MegaShowdownItems.REVEAL_GLASS.get());

        basicItem(MegaShowdownItems.BOOSTER_ENERGY.get());
        basicItem(MegaShowdownItems.LEGEND_PLATE.get());
        basicItem(MegaShowdownItems.ADAMANT_ORB.get());
        basicItem(MegaShowdownItems.GRISEOUS_ORB.get());
        basicItem(MegaShowdownItems.LUSTROUS_ORB.get());
        basicItem(MegaShowdownItems.ADRENALINE_ORB.get());
        basicItem(MegaShowdownItems.SOUL_DEW.get());

        basicItem(MegaShowdownItems.MEGA_BRACELET.get());
        basicItem(MegaShowdownItems.MEGA_RED_BRACELET.get());
        basicItem(MegaShowdownItems.MEGA_YELLOW_BRACELET.get());
        basicItem(MegaShowdownItems.MEGA_PINK_BRACELET.get());
        basicItem(MegaShowdownItems.MEGA_GREEN_BRACELET.get());
        basicItem(MegaShowdownItems.MEGA_BLUE_BRACELET.get());
        basicItem(MegaShowdownItems.MEGA_BLACK_BRACELET.get());

        basicItem(MegaShowdownItems.MAY_BRACELET.get());
        basicItem(MegaShowdownItems.MEGA_RING.get());
        basicItem(MegaShowdownItems.LYSANDRE_RING.get());

        basicItem(MegaShowdownItems.BRENDAN_MEGA_CUFF.get());
        basicItem(MegaShowdownItems.KORRINA_GLOVE.get());
        basicItem(MegaShowdownItems.MAXIE_GLASSES.get());
        basicItem(MegaShowdownItems.ARCHIE_ANCHOR.get());
        basicItem(MegaShowdownItems.LISIA_MEGA_TIARA.get());

        basicItem(MegaShowdownItems.DYNAMAX_BAND.get());
        basicItem(MegaShowdownItems.TERA_ORB.get());

        basicItem(MegaShowdownItems.Z_RING.get());
        basicItem(MegaShowdownItems.Z_RING_BLACK.get());
        basicItem(MegaShowdownItems.Z_RING_YELLOW.get());
        basicItem(MegaShowdownItems.Z_RING_GREEN.get());
        basicItem(MegaShowdownItems.Z_RING_BLUE.get());
        basicItem(MegaShowdownItems.Z_RING_PINK.get());
        basicItem(MegaShowdownItems.Z_RING_RED.get());

        basicItem(MegaShowdownItems.OLIVIAS_Z_RING.get());
        basicItem(MegaShowdownItems.HAPUS_Z_RING.get());

        basicItem(MegaShowdownItems.Z_RING_POWER.get());
        basicItem(MegaShowdownItems.OLIVIA_Z_POWER_RING.get());
        basicItem(MegaShowdownItems.HAPU_Z_POWER_RING.get());
        basicItem(MegaShowdownItems.ROCKET_Z_POWER_RING.get());
        basicItem(MegaShowdownItems.GLADION_Z_POWER_RING.get());
        basicItem(MegaShowdownItems.NANU_Z_POWER_RING.get());

        basicItem(MegaShowdownItems.OMNI_RING.get());

        basicItem(MegaShowdownItems.LIKOS_PENDANT.get());

        basicItem(MegaShowdownItems.ZYGARDE_CUBE.get());

        basicItem(MegaShowdownBlocks.GRACIDEA_FLOWER.get().asItem());
        basicItem(MegaShowdownBlocks.ROTOM_FAN.get().asItem());
        basicItem(MegaShowdownBlocks.ROTOM_FRIDGE.get().asItem());
        basicItem(MegaShowdownBlocks.ROTOM_MOW.get().asItem());
        basicItem(MegaShowdownBlocks.ROTOM_OVEN.get().asItem());
        basicItem(MegaShowdownBlocks.ROTOM_WASHING_MACHINE.get().asItem());

        basicItem(MegaShowdownBlocks.DORMANT_CRYSTAL.get().asItem());
        basicItem(MegaShowdownBlocks.MAX_MUSHROOM.get().asItem());
        basicItem(MegaShowdownItems.STAR_CORE.get());

        basicItem(MegaShowdownItems.TERA_POUCH_WHITE.get());
        basicItem(MegaShowdownItems.TERA_POUCH_ORANGE.get());
        basicItem(MegaShowdownItems.TERA_POUCH_MAGENTA.get());
        basicItem(MegaShowdownItems.TERA_POUCH_LIGHT_BLUE.get());
        basicItem(MegaShowdownItems.TERA_POUCH_YELLOW.get());
        basicItem(MegaShowdownItems.TERA_POUCH_LIME.get());
        basicItem(MegaShowdownItems.TERA_POUCH_PINK.get());
        basicItem(MegaShowdownItems.TERA_POUCH_GRAY.get());
        basicItem(MegaShowdownItems.TERA_POUCH_LIGHT_GRAY.get());
        basicItem(MegaShowdownItems.TERA_POUCH_CYAN.get());
        basicItem(MegaShowdownItems.TERA_POUCH_PURPLE.get());
        basicItem(MegaShowdownItems.TERA_POUCH_BLUE.get());
        basicItem(MegaShowdownItems.TERA_POUCH_BROWN.get());
        basicItem(MegaShowdownItems.TERA_POUCH_GREEN.get());
        basicItem(MegaShowdownItems.TERA_POUCH_RED.get());
        basicItem(MegaShowdownItems.TERA_POUCH_BLACK.get());

        basicItem(MegaShowdownBlocks.MEGA_STONE_CRYSTAL.get().asItem());

        basicItem(MegaShowdownItems.CUSTOM_TERA_SHARD.get());
    }
}