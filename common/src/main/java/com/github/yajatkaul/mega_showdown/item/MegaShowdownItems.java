package com.github.yajatkaul.mega_showdown.item;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.api.types.tera.TeraTypes;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.creative.MegaShowdownTabs;
import com.github.yajatkaul.mega_showdown.item.custom.DebugStick;
import com.github.yajatkaul.mega_showdown.item.custom.TeraPouch;
import com.github.yajatkaul.mega_showdown.item.custom.ToolTipItem;
import com.github.yajatkaul.mega_showdown.item.custom.dynamax.DynamaxCandy;
import com.github.yajatkaul.mega_showdown.item.custom.dynamax.MaxHoney;
import com.github.yajatkaul.mega_showdown.item.custom.dynamax.MaxSoup;
import com.github.yajatkaul.mega_showdown.item.custom.dynamax.SweetMaxSoup;
import com.github.yajatkaul.mega_showdown.item.custom.form_change.*;
import com.github.yajatkaul.mega_showdown.item.custom.fusion.DuFusion;
import com.github.yajatkaul.mega_showdown.item.custom.fusion.SoloFusion;
import com.github.yajatkaul.mega_showdown.item.custom.gimmick.*;
import com.github.yajatkaul.mega_showdown.item.custom.mega.MegaStone;
import com.github.yajatkaul.mega_showdown.item.custom.tera.CustomTeraShard;
import com.github.yajatkaul.mega_showdown.item.custom.tera.LikosPendant;
import com.github.yajatkaul.mega_showdown.item.custom.tera.TeraShard;
import com.github.yajatkaul.mega_showdown.item.custom.z.ElementalZCrystal;
import com.github.yajatkaul.mega_showdown.item.custom.z.SpecialZCrystal;
import com.github.yajatkaul.mega_showdown.utils.RegistryLocator;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MegaShowdownItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MegaShowdown.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> KEYSTONE = registerTooltipItem("keystone", MegaShowdownTabs.MEGA_TAB);
    public static final RegistrySupplier<Item> MEGA_STONE = registerTooltipItem("mega_stone", MegaShowdownTabs.MEGA_TAB);

    public static final RegistrySupplier<Item> ABOMASITE = registerMegaStone("abomasite");
    public static final RegistrySupplier<Item> ABSOLITE = registerMegaStone("absolite");
    public static final RegistrySupplier<Item> AERODACTYLITE = registerMegaStone("aerodactylite");
    public static final RegistrySupplier<Item> AGGRONITE = registerMegaStone("aggronite");
    public static final RegistrySupplier<Item> ALAKAZITE = registerMegaStone("alakazite");
    public static final RegistrySupplier<Item> ALTARIANITE = registerMegaStone("altarianite");
    public static final RegistrySupplier<Item> AMPHAROSITE = registerMegaStone("ampharosite");
    public static final RegistrySupplier<Item> AUDINITE = registerMegaStone("audinite");
    public static final RegistrySupplier<Item> BANETTITE = registerMegaStone("banettite");
    public static final RegistrySupplier<Item> BEEDRILLITE = registerMegaStone("beedrillite");
    public static final RegistrySupplier<Item> BLASTOISINITE = registerMegaStone("blastoisinite");
    public static final RegistrySupplier<Item> BLAZIKENITE = registerMegaStone("blazikenite");
    public static final RegistrySupplier<Item> CAMERUPTITE = registerMegaStone("cameruptite");
    public static final RegistrySupplier<Item> CHARIZARDITE_X = registerMegaStone("charizardite_x");
    public static final RegistrySupplier<Item> CHARIZARDITE_Y = registerMegaStone("charizardite_y");
    public static final RegistrySupplier<Item> DIANCITE = registerMegaStone("diancite");
    public static final RegistrySupplier<Item> GALLADITE = registerMegaStone("galladite");
    public static final RegistrySupplier<Item> GLALITITE = registerMegaStone("glalitite");
    public static final RegistrySupplier<Item> GARCHOMPITE = registerMegaStone("garchompite");
    public static final RegistrySupplier<Item> GARDEVOIRITE = registerMegaStone("gardevoirite");
    public static final RegistrySupplier<Item> GENGARITE = registerMegaStone("gengarite");
    public static final RegistrySupplier<Item> GYARADOSITE = registerMegaStone("gyaradosite");
    public static final RegistrySupplier<Item> HERACRONITE = registerMegaStone("heracronite");
    public static final RegistrySupplier<Item> HOUNDOOMINITE = registerMegaStone("houndoominite");
    public static final RegistrySupplier<Item> KANGASKHANITE = registerMegaStone("kangaskhanite");
    public static final RegistrySupplier<Item> LATIASITE = registerMegaStone("latiasite");
    public static final RegistrySupplier<Item> LATIOSITE = registerMegaStone("latiosite");
    public static final RegistrySupplier<Item> LOPUNNITE = registerMegaStone("lopunnite");
    public static final RegistrySupplier<Item> LUCARIONITE = registerMegaStone("lucarionite");
    public static final RegistrySupplier<Item> MANECTITE = registerMegaStone("manectite");
    public static final RegistrySupplier<Item> MAWILITE = registerMegaStone("mawilite");
    public static final RegistrySupplier<Item> MEDICHAMITE = registerMegaStone("medichamite");
    public static final RegistrySupplier<Item> METAGROSSITE = registerMegaStone("metagrossite");
    public static final RegistrySupplier<Item> MEWTWONITE_X = registerMegaStone("mewtwonite_x");
    public static final RegistrySupplier<Item> MEWTWONITE_Y = registerMegaStone("mewtwonite_y");
    public static final RegistrySupplier<Item> PIDGEOTITE = registerMegaStone("pidgeotite");
    public static final RegistrySupplier<Item> PINSIRITE = registerMegaStone("pinsirite");
    public static final RegistrySupplier<Item> SABLENITE = registerMegaStone("sablenite");
    public static final RegistrySupplier<Item> SALAMENCITE = registerMegaStone("salamencite");
    public static final RegistrySupplier<Item> SCEPTILITE = registerMegaStone("sceptilite");
    public static final RegistrySupplier<Item> SCIZORITE = registerMegaStone("scizorite");
    public static final RegistrySupplier<Item> SHARPEDONITE = registerMegaStone("sharpedonite");
    public static final RegistrySupplier<Item> SLOWBRONITE = registerMegaStone("slowbronite");
    public static final RegistrySupplier<Item> STEELIXITE = registerMegaStone("steelixite");
    public static final RegistrySupplier<Item> SWAMPERTITE = registerMegaStone("swampertite");
    public static final RegistrySupplier<Item> TYRANITARITE = registerMegaStone("tyranitarite");
    public static final RegistrySupplier<Item> VENUSAURITE = registerMegaStone("venusaurite");

    public static final RegistrySupplier<Item> RED_ORB = registerFormChangeHeldItems(
            "red_orb",
            "reversion_state=standard",
            "reversion_state=primal",
            List.of("Groudon"),
            "mega_showdown:red_orb",
            false,
            null
    );

    public static final RegistrySupplier<Item> BLUE_ORB = registerFormChangeHeldItems(
            "blue_orb",
            "reversion_state=standard",
            "reversion_state=primal",
            List.of("Kyogre"),
            "mega_showdown:blue_orb",
            false,
            null
    );

    public static final RegistrySupplier<Item> STAR_CORE = registerFormChangeHeldItems(
            "star_core",
            "eternamax=false",
            "eternamax=true",
            List.of("Eternatus"),
            null,
            false,
            null
    );

    public static final RegistrySupplier<Item> NORMAL_TERA_SHARD = registerTeraShards("normal_tera_shard", TeraTypes.getNORMAL());
    public static final RegistrySupplier<Item> FIRE_TERA_SHARD = registerTeraShards("fire_tera_shard", TeraTypes.getFIRE());
    public static final RegistrySupplier<Item> WATER_TERA_SHARD = registerTeraShards("water_tera_shard", TeraTypes.getWATER());
    public static final RegistrySupplier<Item> ELECTRIC_TERA_SHARD = registerTeraShards("electric_tera_shard", TeraTypes.getELECTRIC());
    public static final RegistrySupplier<Item> GRASS_TERA_SHARD = registerTeraShards("grass_tera_shard", TeraTypes.getGRASS());
    public static final RegistrySupplier<Item> ICE_TERA_SHARD = registerTeraShards("ice_tera_shard", TeraTypes.getICE());
    public static final RegistrySupplier<Item> FIGHTING_TERA_SHARD = registerTeraShards("fighting_tera_shard", TeraTypes.getFIGHTING());
    public static final RegistrySupplier<Item> POISON_TERA_SHARD = registerTeraShards("poison_tera_shard", TeraTypes.getPOISON());
    public static final RegistrySupplier<Item> GROUND_TERA_SHARD = registerTeraShards("ground_tera_shard", TeraTypes.getGROUND());
    public static final RegistrySupplier<Item> FLYING_TERA_SHARD = registerTeraShards("flying_tera_shard", TeraTypes.getFLYING());
    public static final RegistrySupplier<Item> PSYCHIC_TERA_SHARD = registerTeraShards("psychic_tera_shard", TeraTypes.getPSYCHIC());
    public static final RegistrySupplier<Item> BUG_TERA_SHARD = registerTeraShards("bug_tera_shard", TeraTypes.getBUG());
    public static final RegistrySupplier<Item> ROCK_TERA_SHARD = registerTeraShards("rock_tera_shard", TeraTypes.getROCK());
    public static final RegistrySupplier<Item> GHOST_TERA_SHARD = registerTeraShards("ghost_tera_shard", TeraTypes.getGHOST());
    public static final RegistrySupplier<Item> DRAGON_TERA_SHARD = registerTeraShards("dragon_tera_shard", TeraTypes.getDRAGON());
    public static final RegistrySupplier<Item> DARK_TERA_SHARD = registerTeraShards("dark_tera_shard", TeraTypes.getDARK());
    public static final RegistrySupplier<Item> STEEL_TERA_SHARD = registerTeraShards("steel_tera_shard", TeraTypes.getSTEEL());
    public static final RegistrySupplier<Item> FAIRY_TERA_SHARD = registerTeraShards("fairy_tera_shard", TeraTypes.getFAIRY());
    public static final RegistrySupplier<Item> STELLAR_TERA_SHARD = registerTeraShards("stellar_tera_shard", TeraTypes.getSTELLAR());

    public static final RegistrySupplier<Item> CUSTOM_TERA_SHARD = registerCustomTeraShards("custom_tera_shard");

    public static final RegistrySupplier<Item> SPARKLING_STONE_LIGHT = registerItem("sparkling_stone_light", () -> new ToolTipItem(new Item.Properties().arch$tab(MegaShowdownTabs.Z_TAB)));
    public static final RegistrySupplier<Item> SPARKLING_STONE_DARK = registerItem("sparkling_stone_dark", () -> new ToolTipItem(new Item.Properties().arch$tab(MegaShowdownTabs.Z_TAB)));
    public static final RegistrySupplier<Item> BLANK_Z = registerItem("blank_z", () -> new ToolTipItem(new Item.Properties().arch$tab(MegaShowdownTabs.Z_TAB)));
    // Elemental Z-Crystals
    public static final RegistrySupplier<Item> NORMALIUM_Z = registerZElementalCrystals("normalium_z", ElementalTypes.NORMAL);
    public static final RegistrySupplier<Item> BUGINIUM_Z = registerZElementalCrystals("buginium_z", ElementalTypes.BUG);
    public static final RegistrySupplier<Item> DARKINIUM_Z = registerZElementalCrystals("darkinium_z", ElementalTypes.DARK);
    public static final RegistrySupplier<Item> DRAGONIUM_Z = registerZElementalCrystals("dragonium_z", ElementalTypes.DRAGON);
    public static final RegistrySupplier<Item> ELECTRIUM_Z = registerZElementalCrystals("electrium_z", ElementalTypes.ELECTRIC);
    public static final RegistrySupplier<Item> FAIRIUM_Z = registerZElementalCrystals("fairium_z", ElementalTypes.FAIRY);
    public static final RegistrySupplier<Item> FIGHTINIUM_Z = registerZElementalCrystals("fightinium_z", ElementalTypes.FIGHTING);
    public static final RegistrySupplier<Item> FIRIUM_Z = registerZElementalCrystals("firium_z", ElementalTypes.FIRE);
    public static final RegistrySupplier<Item> FLYINIUM_Z = registerZElementalCrystals("flyinium_z", ElementalTypes.FLYING);
    public static final RegistrySupplier<Item> GHOSTIUM_Z = registerZElementalCrystals("ghostium_z", ElementalTypes.GHOST);
    public static final RegistrySupplier<Item> GRASSIUM_Z = registerZElementalCrystals("grassium_z", ElementalTypes.GRASS);
    public static final RegistrySupplier<Item> GROUNDIUM_Z = registerZElementalCrystals("groundium_z", ElementalTypes.GROUND);
    public static final RegistrySupplier<Item> ICIUM_Z = registerZElementalCrystals("icium_z", ElementalTypes.ICE);
    public static final RegistrySupplier<Item> POISONIUM_Z = registerZElementalCrystals("poisonium_z", ElementalTypes.POISON);
    public static final RegistrySupplier<Item> PSYCHIUM_Z = registerZElementalCrystals("psychium_z", ElementalTypes.PSYCHIC);
    public static final RegistrySupplier<Item> ROCKIUM_Z = registerZElementalCrystals("rockium_z", ElementalTypes.ROCK);
    public static final RegistrySupplier<Item> STEELIUM_Z = registerZElementalCrystals("steelium_z", ElementalTypes.STEEL);
    public static final RegistrySupplier<Item> WATERIUM_Z = registerZElementalCrystals("waterium_z", ElementalTypes.WATER);

    // Special Z-Crystals
    public static final RegistrySupplier<Item> ALORAICHIUM_Z = registerZSpecialCrystals("aloraichium_z", ElementalTypes.ELECTRIC);
    public static final RegistrySupplier<Item> DECIDIUM_Z = registerZSpecialCrystals("decidium_z", ElementalTypes.GRASS);
    public static final RegistrySupplier<Item> EEVIUM_Z = registerZSpecialCrystals("eevium_z", ElementalTypes.NORMAL);
    public static final RegistrySupplier<Item> INCINIUM_Z = registerZSpecialCrystals("incinium_z", ElementalTypes.FIRE);
    public static final RegistrySupplier<Item> KOMMONIUM_Z = registerZSpecialCrystals("kommonium_z", ElementalTypes.DRAGON);
    public static final RegistrySupplier<Item> LUNALIUM_Z = registerZSpecialCrystals("lunalium_z", ElementalTypes.PSYCHIC);
    public static final RegistrySupplier<Item> LYCANIUM_Z = registerZSpecialCrystals("lycanium_z", ElementalTypes.ROCK);
    public static final RegistrySupplier<Item> MARSHADIUM_Z = registerZSpecialCrystals("marshadium_z", ElementalTypes.GHOST);
    public static final RegistrySupplier<Item> MEWNIUM_Z = registerZSpecialCrystals("mewnium_z", ElementalTypes.PSYCHIC);
    public static final RegistrySupplier<Item> MIMIKIUM_Z = registerZSpecialCrystals("mimikium_z", ElementalTypes.GHOST);
    public static final RegistrySupplier<Item> PIKANIUM_Z = registerZSpecialCrystals("pikanium_z", ElementalTypes.ELECTRIC);
    public static final RegistrySupplier<Item> PIKASHUNIUM_Z = registerZSpecialCrystals("pikashunium_z", ElementalTypes.ELECTRIC);
    public static final RegistrySupplier<Item> PRIMARIUM_Z = registerZSpecialCrystals("primarium_z", ElementalTypes.WATER);
    public static final RegistrySupplier<Item> SNORLIUM_Z = registerZSpecialCrystals("snorlium_z", ElementalTypes.NORMAL);
    public static final RegistrySupplier<Item> SOLGANIUM_Z = registerZSpecialCrystals("solganium_z", ElementalTypes.STEEL);
    public static final RegistrySupplier<Item> TAPUNIUM_Z = registerZSpecialCrystals("tapunium_z", ElementalTypes.FAIRY);
    public static final RegistrySupplier<Item> ULTRANECROZIUM_Z = registerZSpecialCrystals("ultranecrozium_z", ElementalTypes.PSYCHIC);

    public static final RegistrySupplier<Item> DNA_SPLICER = registerDuFusion(
            "dna_splicer",
            List.of("black-fusion"),
            List.of("white-fusion"),
            List.of("Zekrom"),
            List.of("Reshiram"),
            List.of("Kyurem"),
            List.of("absofusion=black"),
            List.of("absofusion=white"),
            List.of("absofusion=none"),
            List.of("absofusion=none"),
            "mega_showdown:kyurem_black",
            "mega_showdown:kyurem_white"
    );

    public static final RegistrySupplier<Item> REINS_OF_UNITY = registerDuFusion(
            "reins_of_unity",
            List.of("shadow-rider"),
            List.of("ice-rider"),
            List.of("Spectrier"),
            List.of("Glastrier"),
            List.of("Calyrex"),
            List.of("king_steed=shadow"),
            List.of("king_steed=ice"),
            List.of("king_steed=none"),
            List.of("king_steed=none"),
            "mega_showdown:calyrex_shadow",
            "mega_showdown:calyrex_ice"
    );

    public static final RegistrySupplier<Item> N_LUNARIZER = registerSoloFusion(
            "n_lunarizer",
            List.of("dusk-fusion", "dawn-fusion"),
            List.of("Lunala"),
            List.of("Necrozma"),
            "mega_showdown:n_lunar",
            List.of("prism_fusion=dawn"),
            List.of("prism_fusion=none")
    );

    public static final RegistrySupplier<Item> N_SOLARIZER = registerSoloFusion(
            "n_solarizer",
            List.of("dusk-fusion", "dawn-fusion"),
            List.of("Solgaleo"),
            List.of("Necrozma"),
            "mega_showdown:n_solar",
            List.of("prism_fusion=dusk"),
            List.of("prism_fusion=none")
    );

    public static final RegistrySupplier<Item> DEBUG_STICK = registerItem("debug_stick", () -> new DebugStick(new Item.Properties()));

    public static final RegistrySupplier<Item> MEGA_BRACELET = registerMegaBracelet("mega_bracelet");
    public static final RegistrySupplier<Item> MEGA_RED_BRACELET = registerMegaBracelet("mega_bracelet_red");
    public static final RegistrySupplier<Item> MEGA_YELLOW_BRACELET = registerMegaBracelet("mega_bracelet_yellow");
    public static final RegistrySupplier<Item> MEGA_PINK_BRACELET = registerMegaBracelet("mega_bracelet_pink");
    public static final RegistrySupplier<Item> MEGA_GREEN_BRACELET = registerMegaBracelet("mega_bracelet_green");
    public static final RegistrySupplier<Item> MEGA_BLUE_BRACELET = registerMegaBracelet("mega_bracelet_blue");
    public static final RegistrySupplier<Item> MEGA_BLACK_BRACELET = registerMegaBracelet("mega_bracelet_black");

    public static final RegistrySupplier<Item> MAY_BRACELET = registerMegaBracelet("may_bracelet");
    public static final RegistrySupplier<Item> MEGA_RING = registerMegaBracelet("mega_ring");
    public static final RegistrySupplier<Item> LYSANDRE_RING = registerMegaBracelet("lysandre_ring");

    public static final RegistrySupplier<Item> BRENDAN_MEGA_CUFF = registerMegaBracelet("brendan_mega_cuff");
    public static final RegistrySupplier<Item> KORRINA_GLOVE = registerMegaBracelet("korrina_glove");
    public static final RegistrySupplier<Item> MAXIE_GLASSES = registerMegaBracelet("maxie_glasses");
    public static final RegistrySupplier<Item> ARCHIE_ANCHOR = registerMegaBracelet("archie_anchor");
    public static final RegistrySupplier<Item> LISIA_MEGA_TIARA = registerMegaBracelet("lisia_mega_tiara");

    public static final RegistrySupplier<Item> TERA_ORB = registerTeraOrb("tera_orb");

    public static final RegistrySupplier<Item> DYNAMAX_BAND = registerDynamaxBand("dynamax_band");

    public static final RegistrySupplier<Item> Z_RING = registerZRing("z_ring");
    public static final RegistrySupplier<Item> Z_RING_BLACK = registerZRing("z_ring_black");
    public static final RegistrySupplier<Item> Z_RING_YELLOW = registerZRing("z_ring_yellow");
    public static final RegistrySupplier<Item> Z_RING_GREEN = registerZRing("z_ring_green");
    public static final RegistrySupplier<Item> Z_RING_BLUE = registerZRing("z_ring_blue");
    public static final RegistrySupplier<Item> Z_RING_PINK = registerZRing("z_ring_pink");
    public static final RegistrySupplier<Item> Z_RING_RED = registerZRing("z_ring_red");

    public static final RegistrySupplier<Item> OLIVIAS_Z_RING = registerZRing("olivias_z_ring");
    public static final RegistrySupplier<Item> HAPUS_Z_RING = registerZRing("hapus_z_ring");

    public static final RegistrySupplier<Item> Z_RING_POWER = registerZRing("z_power_ring");
    public static final RegistrySupplier<Item> OLIVIA_Z_POWER_RING = registerZRing("olivia_z_power_ring");
    public static final RegistrySupplier<Item> HAPU_Z_POWER_RING = registerZRing("hapu_z_power_ring");
    public static final RegistrySupplier<Item> ROCKET_Z_POWER_RING = registerZRing("rocket_z_power_ring");
    public static final RegistrySupplier<Item> GLADION_Z_POWER_RING = registerZRing("gladion_z_power_ring");
    public static final RegistrySupplier<Item> NANU_Z_POWER_RING = registerZRing("nanu_z_power_ring");

    public static final RegistrySupplier<Item> OMNI_RING = registerOmniRing("omni_ring");

    public static final RegistrySupplier<Item> DYNAMAX_CANDY = registerItem("dynamax_candy", () -> new DynamaxCandy(new Item.Properties().arch$tab(MegaShowdownTabs.DYNAMAX_TAB)));
    public static final RegistrySupplier<Item> MAX_HONEY = registerItem("max_honey", () -> new MaxHoney(new Item.Properties().arch$tab(MegaShowdownTabs.DYNAMAX_TAB)));
    public static final RegistrySupplier<Item> MAX_SOUP = registerItem("max_soup", () -> new MaxSoup(new Item.Properties().arch$tab(MegaShowdownTabs.DYNAMAX_TAB)));
    public static final RegistrySupplier<Item> SWEET_MAX_SOUP = registerItem("sweet_max_soup", () -> new SweetMaxSoup(new Item.Properties().arch$tab(MegaShowdownTabs.DYNAMAX_TAB)));
    public static final RegistrySupplier<Item> WISHING_STAR = registerTooltipItem("wishing_star", MegaShowdownTabs.DYNAMAX_TAB);

    public static final RegistrySupplier<Item> ZYGARDE_CUBE = registerItem("zygarde_cube", () -> new ZygardeCube(new Item.Properties().stacksTo(1).arch$tab(MegaShowdownTabs.FORM_TAB)));
    public static final RegistrySupplier<Item> ZYGARDE_CELL = registerItem("zygarde_cell", () -> new ToolTipItem(new Item.Properties().stacksTo(95).arch$tab(MegaShowdownTabs.FORM_TAB)));
    public static final RegistrySupplier<Item> ZYGARDE_CORE = registerItem("zygarde_core", () -> new ToolTipItem(new Item.Properties().stacksTo(5).arch$tab(MegaShowdownTabs.FORM_TAB)));

    public static final RegistrySupplier<Item> LIKOS_PENDANT = registerItem("likos_pendant",
            () -> new LikosPendant(new Item.Properties().stacksTo(1)
                    .stacksTo(1)
                    .component(MegaShowdownDataComponents.LIKO_PENDANT_TICK_COMPONENT.get(), MegaShowdownConfig.likoPendentDuration)
                    .arch$tab(MegaShowdownTabs.TERA_TAB))
    );

    public static final RegistrySupplier<Item> PINK_NECTAR = registerFormChangeInteractItem(
            "pink_nectar",
            "pau-style",
            "dance_style=pau",
            List.of("Oricorio"),
            "mega_showdown:end_rod",
            1,
            false,
            null
    );

    public static final RegistrySupplier<Item> PURPLE_NECTAR = registerFormChangeInteractItem(
            "purple_nectar",
            "sensu-style",
            "dance_style=sensu",
            List.of("Oricorio"),
            "mega_showdown:end_rod",
            1,
            false,
            null
    );

    public static final RegistrySupplier<Item> RED_NECTAR = registerFormChangeInteractItem(
            "red_nectar",
            "baile-style",
            "dance_style=baile",
            List.of("Oricorio"),
            "mega_showdown:end_rod",
            1,
            false,
            null
    );

    public static final RegistrySupplier<Item> YELLOW_NECTAR = registerFormChangeInteractItem(
            "yellow_nectar",
            "pom-pom-style",
            "dance_style=pom-pom",
            List.of("Oricorio"),
            "mega_showdown:end_rod",
            1,
            false,
            null
    );

    public static final RegistrySupplier<Item> CORNERSTONE_MASK = registerFormChangeHeldItems(
            "cornerstone_mask",
            "ogre_mask=teal",
            "ogre_mask=cornerstone",
            List.of("Ogerpon"),
            null,
            true,
            (pokemon -> {
                pokemon.setTeraType(TeraTypes.getROCK());
            }),
            (pokemon -> {
                pokemon.setTeraType(TeraTypes.getGRASS());
            })
    );

    public static final RegistrySupplier<Item> WELLSPRING_MASK = registerFormChangeHeldItems(
            "wellspring_mask",
            "ogre_mask=teal",
            "ogre_mask=wellspring",
            List.of("Ogerpon"),
            null,
            true,
            (pokemon -> {
                pokemon.setTeraType(TeraTypes.getWATER());
            }),
            (pokemon -> {
                pokemon.setTeraType(TeraTypes.getGRASS());
            })
    );

    public static final RegistrySupplier<Item> HEARTHFLAME_MASK = registerFormChangeHeldItems(
            "hearthflame_mask",
            "ogre_mask=teal",
            "ogre_mask=hearthflame",
            List.of("Ogerpon"),
            null,
            true,
            (pokemon -> {
                pokemon.setTeraType(TeraTypes.getFIRE());
            }),
            (pokemon -> {
                pokemon.setTeraType(TeraTypes.getGRASS());
            })
    );

    public static final RegistrySupplier<Item> GRISEOUS_CORE = registerFormChangeHeldItems(
            "griseous_core",
            "orb_forme=altered",
            "orb_forme=origin",
            List.of("Giratina"),
            "mega_showdown:origin_g_effect",
            true
    );

    public static final RegistrySupplier<Item> ASH_CAP = registerItem("ash_cap", () -> new AshCap(new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB)));

    public static final RegistrySupplier<Item> ADAMANT_CRYSTAL = registerFormChangeHeldItems(
            "adamant_crystal",
            "orb_forme=altered",
            "orb_forme=origin",
            List.of("Dialga"),
            "mega_showdown:origin_effect",
            true
    );

    public static final RegistrySupplier<Item> LUSTROUS_GLOBE = registerFormChangeHeldItems(
            "lustrous_globe",
            "orb_forme=altered",
            "orb_forme=origin",
            List.of("Palkia"),
            "mega_showdown:origin_effect",
            true
    );

    public static final RegistrySupplier<Item> FLAME_PLATE = registerFormChangeHeldItems(
            "flame_plate",
            "multitype=normal",
            "multitype=fire",
            List.of("Arceus"),
            "mega_showdown:arceus_fire",
            true
    );

    public static final RegistrySupplier<Item> SPLASH_PLATE = registerFormChangeHeldItems(
            "splash_plate",
            "multitype=normal",
            "multitype=water",
            List.of("Arceus"),
            "mega_showdown:arceus_water",
            true
    );

    public static final RegistrySupplier<Item> ZAP_PLATE = registerFormChangeHeldItems(
            "zap_plate",
            "multitype=normal",
            "multitype=electric",
            List.of("Arceus"),
            "mega_showdown:arceus_electric",
            true
    );

    public static final RegistrySupplier<Item> MEADOW_PLATE = registerFormChangeHeldItems(
            "meadow_plate",
            "multitype=normal",
            "multitype=grass",
            List.of("Arceus"),
            "mega_showdown:arceus_grass",
            true
    );

    public static final RegistrySupplier<Item> ICICLE_PLATE = registerFormChangeHeldItems(
            "icicle_plate",
            "multitype=normal",
            "multitype=ice",
            List.of("Arceus"),
            "mega_showdown:arceus_ice",
            true
    );

    public static final RegistrySupplier<Item> FIST_PLATE = registerFormChangeHeldItems(
            "fist_plate",
            "multitype=normal",
            "multitype=fighting",
            List.of("Arceus"),
            "mega_showdown:arceus_fighting",
            true
    );

    public static final RegistrySupplier<Item> TOXIC_PLATE = registerFormChangeHeldItems(
            "toxic_plate",
            "multitype=normal",
            "multitype=poison",
            List.of("Arceus"),
            "mega_showdown:arceus_poison",
            true
    );

    public static final RegistrySupplier<Item> EARTH_PLATE = registerFormChangeHeldItems(
            "earth_plate",
            "multitype=normal",
            "multitype=ground",
            List.of("Arceus"),
            "mega_showdown:arceus_ground",
            true
    );

    public static final RegistrySupplier<Item> SKY_PLATE = registerFormChangeHeldItems(
            "sky_plate",
            "multitype=normal",
            "multitype=flying",
            List.of("Arceus"),
            "mega_showdown:arceus_flying",
            true
    );

    public static final RegistrySupplier<Item> MIND_PLATE = registerFormChangeHeldItems(
            "mind_plate",
            "multitype=normal",
            "multitype=psychic",
            List.of("Arceus"),
            "mega_showdown:arceus_psychic",
            true
    );

    public static final RegistrySupplier<Item> INSECT_PLATE = registerFormChangeHeldItems(
            "insect_plate",
            "multitype=normal",
            "multitype=bug",
            List.of("Arceus"),
            "mega_showdown:arceus_bug",
            true
    );

    public static final RegistrySupplier<Item> STONE_PLATE = registerFormChangeHeldItems(
            "stone_plate",
            "multitype=normal",
            "multitype=rock",
            List.of("Arceus"),
            "mega_showdown:arceus_rock",
            true
    );

    public static final RegistrySupplier<Item> SPOOKY_PLATE = registerFormChangeHeldItems(
            "spooky_plate",
            "multitype=normal",
            "multitype=ghost",
            List.of("Arceus"),
            "mega_showdown:arceus_ghost",
            true
    );

    public static final RegistrySupplier<Item> DRACO_PLATE = registerFormChangeHeldItems(
            "draco_plate",
            "multitype=normal",
            "multitype=dragon",
            List.of("Arceus"),
            "mega_showdown:arceus_dragon",
            true
    );

    public static final RegistrySupplier<Item> DREAD_PLATE = registerFormChangeHeldItems(
            "dread_plate",
            "multitype=normal",
            "multitype=dark",
            List.of("Arceus"),
            "mega_showdown:arceus_dark",
            true
    );

    public static final RegistrySupplier<Item> IRON_PLATE = registerFormChangeHeldItems(
            "iron_plate",
            "multitype=normal",
            "multitype=steel",
            List.of("Arceus"),
            "mega_showdown:arceus_steel",
            true
    );

    public static final RegistrySupplier<Item> PIXIE_PLATE = registerFormChangeHeldItems(
            "pixie_plate",
            "multitype=normal",
            "multitype=fairy",
            List.of("Arceus"),
            "mega_showdown:arceus_fairy",
            true
    );

    public static final RegistrySupplier<Item> BUG_MEMORY = registerFormChangeHeldItems(
            "bug_memory",
            "rks_memory=normal",
            "rks_memory=bug",
            List.of("Silvally"),
            "mega_showdown:silvally_bug_memory",
            true
    );

    public static final RegistrySupplier<Item> DARK_MEMORY = registerFormChangeHeldItems(
            "dark_memory",
            "rks_memory=normal",
            "rks_memory=dark",
            List.of("Silvally"),
            "mega_showdown:silvally_dark_memory",
            true
    );

    public static final RegistrySupplier<Item> DRAGON_MEMORY = registerFormChangeHeldItems(
            "dragon_memory",
            "rks_memory=normal",
            "rks_memory=dragon",
            List.of("Silvally"),
            "mega_showdown:silvally_dragon_memory",
            true
    );

    public static final RegistrySupplier<Item> ELECTRIC_MEMORY = registerFormChangeHeldItems(
            "electric_memory",
            "rks_memory=normal",
            "rks_memory=electric",
            List.of("Silvally"),
            "mega_showdown:silvally_electric_memory",
            true
    );

    public static final RegistrySupplier<Item> FAIRY_MEMORY = registerFormChangeHeldItems(
            "fairy_memory",
            "rks_memory=normal",
            "rks_memory=fairy",
            List.of("Silvally"),
            "mega_showdown:silvally_fairy_memory",
            true
    );

    public static final RegistrySupplier<Item> FIGHTING_MEMORY = registerFormChangeHeldItems(
            "fighting_memory",
            "rks_memory=normal",
            "rks_memory=fighting",
            List.of("Silvally"),
            "mega_showdown:silvally_fighting_memory",
            true
    );

    public static final RegistrySupplier<Item> FIRE_MEMORY = registerFormChangeHeldItems(
            "fire_memory",
            "rks_memory=normal",
            "rks_memory=fire",
            List.of("Silvally"),
            "mega_showdown:silvally_fire_memory",
            true
    );

    public static final RegistrySupplier<Item> FLYING_MEMORY = registerFormChangeHeldItems(
            "flying_memory",
            "rks_memory=normal",
            "rks_memory=flying",
            List.of("Silvally"),
            "mega_showdown:silvally_flying_memory",
            true
    );

    public static final RegistrySupplier<Item> GHOST_MEMORY = registerFormChangeHeldItems(
            "ghost_memory",
            "rks_memory=normal",
            "rks_memory=ghost",
            List.of("Silvally"),
            "mega_showdown:silvally_ghost_memory",
            true
    );

    public static final RegistrySupplier<Item> GRASS_MEMORY = registerFormChangeHeldItems(
            "grass_memory",
            "rks_memory=normal",
            "rks_memory=grass",
            List.of("Silvally"),
            "mega_showdown:silvally_grass_memory",
            true
    );

    public static final RegistrySupplier<Item> GROUND_MEMORY = registerFormChangeHeldItems(
            "ground_memory",
            "rks_memory=normal",
            "rks_memory=ground",
            List.of("Silvally"),
            "mega_showdown:silvally_ground_memory",
            true
    );

    public static final RegistrySupplier<Item> ICE_MEMORY = registerFormChangeHeldItems(
            "ice_memory",
            "rks_memory=normal",
            "rks_memory=ice",
            List.of("Silvally"),
            "mega_showdown:silvally_ice_memory",
            true
    );

    public static final RegistrySupplier<Item> POISON_MEMORY = registerFormChangeHeldItems(
            "poison_memory",
            "rks_memory=normal",
            "rks_memory=poison",
            List.of("Silvally"),
            "mega_showdown:silvally_poison_memory",
            true
    );

    public static final RegistrySupplier<Item> PSYCHIC_MEMORY = registerFormChangeHeldItems(
            "psychic_memory",
            "rks_memory=normal",
            "rks_memory=psychic",
            List.of("Silvally"),
            "mega_showdown:silvally_psychic_memory",
            true
    );

    public static final RegistrySupplier<Item> ROCK_MEMORY = registerFormChangeHeldItems(
            "rock_memory",
            "rks_memory=normal",
            "rks_memory=rock",
            List.of("Silvally"),
            "mega_showdown:silvally_rock_memory",
            true
    );

    public static final RegistrySupplier<Item> STEEL_MEMORY = registerFormChangeHeldItems(
            "steel_memory",
            "rks_memory=normal",
            "rks_memory=steel",
            List.of("Silvally"),
            "mega_showdown:silvally_steel_memory",
            true
    );

    public static final RegistrySupplier<Item> WATER_MEMORY = registerFormChangeHeldItems(
            "water_memory",
            "rks_memory=normal",
            "rks_memory=water",
            List.of("Silvally"),
            "mega_showdown:silvally_water_memory",
            true
    );

    public static final RegistrySupplier<Item> BURN_DRIVE = registerFormChangeHeldItems(
            "burn_drive",
            "techno_drive=none",
            "techno_drive=fire",
            List.of("Genesect"),
            null,
            true
    );

    public static final RegistrySupplier<Item> CHILL_DRIVE = registerFormChangeHeldItems(
            "chill_drive",
            "techno_drive=none",
            "techno_drive=ice",
            List.of("Genesect"),
            null,
            true
    );

    public static final RegistrySupplier<Item> DOUSE_DRIVE = registerFormChangeHeldItems(
            "douse_drive",
            "techno_drive=none",
            "techno_drive=water",
            List.of("Genesect"),
            null,
            true
    );

    public static final RegistrySupplier<Item> SHOCK_DRIVE = registerFormChangeHeldItems(
            "shock_drive",
            "techno_drive=none",
            "techno_drive=electric",
            List.of("Genesect"),
            null,
            true
    );

    public static final RegistrySupplier<Item> RUSTED_SWORD = registerFormChangeHeldItems(
            "rusted_sword",
            "crowned=false",
            "crowned=true",
            List.of("Zacian"),
            "mega_showdown:end_rod",
            true,
            (pokemon -> {
                Level level = pokemon.getEntity().level();
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                if (lightning != null) {
                    lightning.moveTo(Vec3.atBottomCenterOf(pokemon.getEntity().blockPosition()));
                    lightning.setVisualOnly(true);
                    level.addFreshEntity(lightning);
                }
            })
    );

    public static final RegistrySupplier<Item> RUSTED_SHIELD = registerFormChangeHeldItems(
            "rusted_shield",
            "crowned=false",
            "crowned=true",
            List.of("Zamazenta"),
            "mega_showdown:end_rod",
            true,
            (pokemon -> {
                Level level = pokemon.getEntity().level();
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                if (lightning != null) {
                    lightning.moveTo(Vec3.atBottomCenterOf(pokemon.getEntity().blockPosition()));
                    lightning.setVisualOnly(true);
                    level.addFreshEntity(lightning);
                }
            })
    );

    public static final RegistrySupplier<Item> PRISON_BOTTLE = registerFormChangeInteractItem(
            "prison_bottle",
            "unbound",
            "djinn_state=unbound",
            List.of("Hoopa"),
            "mega_showdown:prison_bottle",
            0,
            true,
            "djinn_state=confined"
    );

    public static final RegistrySupplier<Item> PIKA_CASE = registerFormChangeInteractToggleItem(
            "pika_case",
            List.of(
                    "cosplay",
                    "belle",
                    "libre",
                    "phd",
                    "pop_star",
                    "rock_star"
            ),
            List.of(
                    "cosplay=cosplay",
                    "cosplay=belle",
                    "cosplay=libre",
                    "cosplay=phd",
                    "cosplay=pop_star",
                    "cosplay=rock_star"
            ),
            List.of("Pikachu"),
            List.of(
                    "mega_showdown:end_rod",
                    "mega_showdown:end_rod",
                    "mega_showdown:end_rod",
                    "mega_showdown:end_rod",
                    "mega_showdown:end_rod",
                    "mega_showdown:end_rod"
            ),
            0
    );

    public static final RegistrySupplier<Item> ROTOM_CATALOGUE = registerItem(
            "rotom_catalogue", () -> new RotomCatalogue(new Item.Properties().stacksTo(1).arch$tab(MegaShowdownTabs.FORM_TAB))
    );

    public static final RegistrySupplier<Item> REVEAL_GLASS = registerFormChangeInteractItem(
            "reveal_glass",
            "therian-forme",
            "mirror_forme=therian",
            List.of("Tornadus", "Thundurus", "Landorus", "Enamorus"),
            "mega_showdown:end_rod",
            0,
            true,
            "mirror_forme=incarnate"
    );

    public static final RegistrySupplier<Item> BOOSTER_ENERGY = registerTooltipItem("booster_energy", MegaShowdownTabs.COMPI_TAB);
    public static final RegistrySupplier<Item> LEGEND_PLATE = registerTooltipItem("legend_plate", MegaShowdownTabs.COMPI_TAB);
    public static final RegistrySupplier<Item> ADAMANT_ORB = registerTooltipItem("adamant_orb", MegaShowdownTabs.COMPI_TAB);
    public static final RegistrySupplier<Item> GRISEOUS_ORB = registerTooltipItem("griseous_orb", MegaShowdownTabs.COMPI_TAB);
    public static final RegistrySupplier<Item> LUSTROUS_ORB = registerTooltipItem("lustrous_orb", MegaShowdownTabs.COMPI_TAB);
    public static final RegistrySupplier<Item> ADRENALINE_ORB = registerTooltipItem("adrenaline_orb", MegaShowdownTabs.COMPI_TAB);
    public static final RegistrySupplier<Item> SOUL_DEW = registerTooltipItem("soul_dew", MegaShowdownTabs.COMPI_TAB);

    public static final RegistrySupplier<Item> TERA_POUCH_WHITE = registerTeraPouch("tera_pouch_white");
    public static final RegistrySupplier<Item> TERA_POUCH_ORANGE = registerTeraPouch("tera_pouch_orange");
    public static final RegistrySupplier<Item> TERA_POUCH_MAGENTA = registerTeraPouch("tera_pouch_magenta");
    public static final RegistrySupplier<Item> TERA_POUCH_LIGHT_BLUE = registerTeraPouch("tera_pouch_light_blue");
    public static final RegistrySupplier<Item> TERA_POUCH_YELLOW = registerTeraPouch("tera_pouch_yellow");
    public static final RegistrySupplier<Item> TERA_POUCH_LIME = registerTeraPouch("tera_pouch_lime");
    public static final RegistrySupplier<Item> TERA_POUCH_PINK = registerTeraPouch("tera_pouch_pink");
    public static final RegistrySupplier<Item> TERA_POUCH_GRAY = registerTeraPouch("tera_pouch_gray");
    public static final RegistrySupplier<Item> TERA_POUCH_LIGHT_GRAY = registerTeraPouch("tera_pouch_light_gray");
    public static final RegistrySupplier<Item> TERA_POUCH_CYAN = registerTeraPouch("tera_pouch_cyan");
    public static final RegistrySupplier<Item> TERA_POUCH_PURPLE = registerTeraPouch("tera_pouch_purple");
    public static final RegistrySupplier<Item> TERA_POUCH_BLUE = registerTeraPouch("tera_pouch_blue");
    public static final RegistrySupplier<Item> TERA_POUCH_BROWN = registerTeraPouch("tera_pouch_brown");
    public static final RegistrySupplier<Item> TERA_POUCH_GREEN = registerTeraPouch("tera_pouch_green");
    public static final RegistrySupplier<Item> TERA_POUCH_RED = registerTeraPouch("tera_pouch_red");
    public static final RegistrySupplier<Item> TERA_POUCH_BLACK = registerTeraPouch("tera_pouch_black");

    private static RegistrySupplier<Item> registerTeraPouch(String name) {
        return registerItem(name, () -> new TeraPouch(
                new Item.Properties().stacksTo(1).arch$tab(MegaShowdownTabs.TERA_TAB)
        ));
    }

    private static RegistrySupplier<Item> registerFormChangeInteractItem(String name,
                                                                         String form_name,
                                                                         String form_apply,
                                                                         List<String> pokemons,
                                                                         String effectId,
                                                                         int consume,
                                                                         boolean revertable,
                                                                         String form_aspect_revert
    ) {
        return ITEMS.register(name, () -> new FormChangeInteractItem(
                new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB),
                form_name,
                form_apply,
                pokemons,
                effectId,
                consume,
                revertable,
                form_aspect_revert
        ));
    }

    private static RegistrySupplier<Item> registerFormChangeInteractToggleItem(String name,
                                                                               List<String> form_apply_order,
                                                                               List<String> form_aspect_apply_order,
                                                                               List<String> pokemons,
                                                                               List<String> effectIds,
                                                                               int consume
    ) {
        return ITEMS.register(name, () -> new FormChangeInteractToggleItem(
                new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB),
                form_apply_order,
                form_aspect_apply_order,
                pokemons,
                effectIds,
                consume
        ));
    }

    private static RegistrySupplier<Item> registerMegaStone(String name) {
        return ITEMS.register(name, () -> new MegaStone(
                        new Item.Properties()
                                .component(MegaShowdownDataComponents.REGISTRY_TYPE_COMPONENT.get(), RegistryLocator.MEGA)
                                .component(MegaShowdownDataComponents.RESOURCE_LOCATION_COMPONENT.get(), ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, name))
                                .arch$tab(MegaShowdownTabs.MEGA_TAB)
                )
        );
    }

    private static RegistrySupplier<Item> registerMegaBracelet(String name) {
        return ITEMS.register(name, () -> new MegaBracelet(
                new Item.Properties()
                        .stacksTo(1)
                        .arch$tab(MegaShowdownTabs.MEGA_TAB))
        );
    }

    private static RegistrySupplier<Item> registerOmniRing(String name) {
        return ITEMS.register(name, () -> new OmniRing(
                new Item.Properties()
                        .stacksTo(1)
                        .fireResistant()
                        .arch$tab(MegaShowdownTabs.KEY_TAB))
        );
    }

    private static RegistrySupplier<Item> registerTeraOrb(String name) {
        return ITEMS.register(name, () -> new TeraOrb(
                new Item.Properties()
                        .stacksTo(1)
                        .durability(100)
                        .arch$tab(MegaShowdownTabs.TERA_TAB))
        );
    }

    private static RegistrySupplier<Item> registerDynamaxBand(String name) {
        return ITEMS.register(name, () -> new DynamaxBand(
                new Item.Properties()
                        .stacksTo(1)
                        .arch$tab(MegaShowdownTabs.DYNAMAX_TAB))
        );
    }

    private static RegistrySupplier<Item> registerZRing(String name) {
        return ITEMS.register(name, () -> new ZRing(
                new Item.Properties()
                        .stacksTo(1)
                        .arch$tab(MegaShowdownTabs.Z_TAB))
        );
    }

    private static RegistrySupplier<Item> registerDuFusion(String name,
                                                           List<String> fusion1,
                                                           List<String> fusion2,
                                                           List<String> pokemon1,
                                                           List<String> pokemon2,
                                                           List<String> pokemonMain,
                                                           List<String> applyAspect1,
                                                           List<String> applyAspect2,
                                                           List<String> revertAspect1,
                                                           List<String> revertAspect2,
                                                           String effect1,
                                                           String effect2
    ) {
        return ITEMS.register(name, () -> new DuFusion(
                new Item.Properties()
                        .arch$tab(MegaShowdownTabs.FORM_TAB)
                        .stacksTo(1),
                fusion1,
                fusion2,
                pokemon1,
                pokemon2,
                pokemonMain,
                applyAspect1,
                applyAspect2,
                revertAspect1,
                revertAspect2,
                effect1,
                effect2,
                name)
        );
    }

    private static RegistrySupplier<Item> registerSoloFusion(String name,
                                                             List<String> fusions,
                                                             List<String> pokemon,
                                                             List<String> pokemonMain,
                                                             String effectId,
                                                             List<String> applyAspect,
                                                             List<String> revertAspect
    ) {
        return ITEMS.register(name, () -> new SoloFusion(
                new Item.Properties()
                        .arch$tab(MegaShowdownTabs.FORM_TAB)
                        .stacksTo(1),
                fusions,
                pokemon,
                pokemonMain,
                effectId,
                applyAspect,
                revertAspect,
                name)
        );
    }

    private static RegistrySupplier<Item> registerTeraShards(String name, TeraType teraType) {
        return ITEMS.register(name, () -> new TeraShard(new Item.Properties().arch$tab(MegaShowdownTabs.TERA_TAB), teraType));
    }

    private static RegistrySupplier<Item> registerCustomTeraShards(String name) {
        return ITEMS.register(name, () -> new CustomTeraShard(new Item.Properties().arch$tab(MegaShowdownTabs.TERA_TAB)));
    }

    private static RegistrySupplier<Item> registerZElementalCrystals(String name, ElementalType type) {
        return ITEMS.register(name, () -> new ElementalZCrystal(
                new Item.Properties()
                        .component(MegaShowdownDataComponents.REGISTRY_TYPE_COMPONENT.get(), RegistryLocator.Z_CRYSTAL_ITEM)
                        .component(MegaShowdownDataComponents.RESOURCE_LOCATION_COMPONENT.get(),
                                ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, name))
                        .arch$tab(MegaShowdownTabs.Z_TAB),
                List.of("Arceus"),
                true,
                type
        ));
    }

    private static RegistrySupplier<Item> registerZSpecialCrystals(String name, ElementalType type) {
        return ITEMS.register(name, () -> new SpecialZCrystal(new Item.Properties()
                .component(MegaShowdownDataComponents.REGISTRY_TYPE_COMPONENT.get(), RegistryLocator.Z_CRYSTAL_ITEM)
                .component(MegaShowdownDataComponents.RESOURCE_LOCATION_COMPONENT.get(),
                        ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, name))
                .arch$tab(MegaShowdownTabs.Z_TAB), type));
    }

    private static RegistrySupplier<Item> registerFormChangeHeldItems(String name, String revertAspect, String applyAspect, List<String> pokemons, String effectId, boolean tradable) {
        return ITEMS.register(name,
                () -> new FormChangeHeldItem(
                        new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB),
                        revertAspect,
                        applyAspect,
                        pokemons,
                        effectId,
                        tradable,
                        null,
                        null
                ));
    }

    private static RegistrySupplier<Item> registerFormChangeHeldItems(String name, String revertAspect, String applyAspect, List<String> pokemons, String effectId, boolean tradable, Consumer<Pokemon> callBack) {
        return ITEMS.register(name,
                () -> new FormChangeHeldItem(
                        new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB),
                        revertAspect,
                        applyAspect,
                        pokemons,
                        effectId,
                        tradable,
                        callBack,
                        null
                ));
    }

    private static RegistrySupplier<Item> registerFormChangeHeldItems(String name, String revertAspect, String applyAspect, List<String> pokemons, String effectId, boolean tradable, Consumer<Pokemon> callBackApply, Consumer<Pokemon> callBackRevert) {
        return ITEMS.register(name,
                () -> new FormChangeHeldItem(
                        new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB),
                        revertAspect,
                        applyAspect,
                        pokemons,
                        effectId,
                        tradable,
                        callBackApply,
                        callBackRevert
                ));
    }

    private static RegistrySupplier<Item> registerTooltipItem(String name, DeferredSupplier<CreativeModeTab> tab) {
        return ITEMS.register(name, () -> new ToolTipItem(
                new Item.Properties().arch$tab(tab))
        );
    }

    private static RegistrySupplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

    public static void register() {
        ITEMS.register();
    }
}
