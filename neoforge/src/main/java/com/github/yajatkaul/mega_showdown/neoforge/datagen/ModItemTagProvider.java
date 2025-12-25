package com.github.yajatkaul.mega_showdown.neoforge.datagen;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlocks;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import com.github.yajatkaul.mega_showdown.tag.MegaShowdownTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, MegaShowdown.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(MegaShowdownTags.Items.MEGA_BRACELET)
                .add(MegaShowdownItems.MEGA_BRACELET.get())
                .add(MegaShowdownItems.MEGA_RED_BRACELET.get())
                .add(MegaShowdownItems.MEGA_YELLOW_BRACELET.get())
                .add(MegaShowdownItems.MEGA_PINK_BRACELET.get())
                .add(MegaShowdownItems.MEGA_GREEN_BRACELET.get())
                .add(MegaShowdownItems.MEGA_BLUE_BRACELET.get())
                .add(MegaShowdownItems.MEGA_BLACK_BRACELET.get())

                .add(MegaShowdownItems.MAY_BRACELET.get())
                .add(MegaShowdownItems.MEGA_RING.get())
                .add(MegaShowdownItems.LYSANDRE_RING.get())

                .add(MegaShowdownItems.BRENDAN_MEGA_CUFF.get())
                .add(MegaShowdownItems.KORRINA_GLOVE.get())
                .add(MegaShowdownItems.MAXIE_GLASSES.get())
                .add(MegaShowdownItems.ARCHIE_ANCHOR.get())
                .add(MegaShowdownItems.LISIA_MEGA_TIARA.get());

        tag(MegaShowdownTags.Items.DYNAMAX_BAND)
                .add(MegaShowdownItems.DYNAMAX_BAND.get());

        tag(MegaShowdownTags.Items.Z_RING)
                .add(MegaShowdownItems.Z_RING.get())
                .add(MegaShowdownItems.Z_RING_BLACK.get())
                .add(MegaShowdownItems.Z_RING_YELLOW.get())
                .add(MegaShowdownItems.Z_RING_GREEN.get())
                .add(MegaShowdownItems.Z_RING_BLUE.get())
                .add(MegaShowdownItems.Z_RING_PINK.get())
                .add(MegaShowdownItems.Z_RING_RED.get())

                .add(MegaShowdownItems.OLIVIAS_Z_RING.get())
                .add(MegaShowdownItems.HAPUS_Z_RING.get())

                .add(MegaShowdownItems.Z_RING_POWER.get())
                .add(MegaShowdownItems.OLIVIA_Z_POWER_RING.get())
                .add(MegaShowdownItems.HAPU_Z_POWER_RING.get())
                .add(MegaShowdownItems.ROCKET_Z_POWER_RING.get())
                .add(MegaShowdownItems.GLADION_Z_POWER_RING.get())
                .add(MegaShowdownItems.NANU_Z_POWER_RING.get());

        tag(MegaShowdownTags.Items.TERA_ORB)
                .add(MegaShowdownItems.TERA_ORB.get());

        tag(MegaShowdownTags.Items.OMNI_RING)
                .add(MegaShowdownItems.OMNI_RING.get());

        tag(MegaShowdownTags.Items.Z_CRYSTAL)
                .add(MegaShowdownItems.ALORAICHIUM_Z.get())
                .add(MegaShowdownItems.BUGINIUM_Z.get())
                .add(MegaShowdownItems.DARKINIUM_Z.get())
                .add(MegaShowdownItems.DECIDIUM_Z.get())
                .add(MegaShowdownItems.DRAGONIUM_Z.get())
                .add(MegaShowdownItems.EEVIUM_Z.get())
                .add(MegaShowdownItems.ELECTRIUM_Z.get())
                .add(MegaShowdownItems.FAIRIUM_Z.get())
                .add(MegaShowdownItems.FIGHTINIUM_Z.get())
                .add(MegaShowdownItems.FIRIUM_Z.get())
                .add(MegaShowdownItems.FLYINIUM_Z.get())
                .add(MegaShowdownItems.GHOSTIUM_Z.get())
                .add(MegaShowdownItems.GRASSIUM_Z.get())
                .add(MegaShowdownItems.GROUNDIUM_Z.get())
                .add(MegaShowdownItems.ICIUM_Z.get())
                .add(MegaShowdownItems.INCINIUM_Z.get())
                .add(MegaShowdownItems.KOMMONIUM_Z.get())
                .add(MegaShowdownItems.LUNALIUM_Z.get())
                .add(MegaShowdownItems.LYCANIUM_Z.get())
                .add(MegaShowdownItems.MARSHADIUM_Z.get())
                .add(MegaShowdownItems.MEWNIUM_Z.get())
                .add(MegaShowdownItems.MIMIKIUM_Z.get())
                .add(MegaShowdownItems.NORMALIUM_Z.get())
                .add(MegaShowdownItems.PIKANIUM_Z.get())
                .add(MegaShowdownItems.PIKASHUNIUM_Z.get())
                .add(MegaShowdownItems.POISONIUM_Z.get())
                .add(MegaShowdownItems.PRIMARIUM_Z.get())
                .add(MegaShowdownItems.PSYCHIUM_Z.get())
                .add(MegaShowdownItems.ROCKIUM_Z.get())
                .add(MegaShowdownItems.SNORLIUM_Z.get())
                .add(MegaShowdownItems.SOLGANIUM_Z.get())
                .add(MegaShowdownItems.STEELIUM_Z.get())
                .add(MegaShowdownItems.TAPUNIUM_Z.get())
                .add(MegaShowdownItems.ULTRANECROZIUM_Z.get())
                .add(MegaShowdownItems.WATERIUM_Z.get());

        tag(MegaShowdownTags.Items.MEGA_STONE)
                .add(MegaShowdownItems.ABSOLITE.get())
                .add(MegaShowdownItems.AERODACTYLITE.get())
                .add(MegaShowdownItems.AGGRONITE.get())
                .add(MegaShowdownItems.ALAKAZITE.get())
                .add(MegaShowdownItems.ALTARIANITE.get())
                .add(MegaShowdownItems.AMPHAROSITE.get())
                .add(MegaShowdownItems.AUDINITE.get())
                .add(MegaShowdownItems.BANETTITE.get())
                .add(MegaShowdownItems.BEEDRILLITE.get())
                .add(MegaShowdownItems.BLASTOISINITE.get())
                .add(MegaShowdownItems.BLAZIKENITE.get())
                .add(MegaShowdownItems.CAMERUPTITE.get())
                .add(MegaShowdownItems.CHARIZARDITE_Y.get())
                .add(MegaShowdownItems.CHARIZARDITE_X.get())
                .add(MegaShowdownItems.DIANCITE.get())
                .add(MegaShowdownItems.GALLADITE.get())
                .add(MegaShowdownItems.GARCHOMPITE.get())
                .add(MegaShowdownItems.GARDEVOIRITE.get())
                .add(MegaShowdownItems.GENGARITE.get())
                .add(MegaShowdownItems.GLALITITE.get())
                .add(MegaShowdownItems.GYARADOSITE.get())
                .add(MegaShowdownItems.HERACRONITE.get())
                .add(MegaShowdownItems.HOUNDOOMINITE.get())
                .add(MegaShowdownItems.KANGASKHANITE.get())
                .add(MegaShowdownItems.LATIASITE.get())
                .add(MegaShowdownItems.LATIOSITE.get())
                .add(MegaShowdownItems.LOPUNNITE.get())
                .add(MegaShowdownItems.LUCARIONITE.get())
                .add(MegaShowdownItems.MANECTITE.get())
                .add(MegaShowdownItems.MAWILITE.get())
                .add(MegaShowdownItems.MEDICHAMITE.get())
                .add(MegaShowdownItems.METAGROSSITE.get())
                .add(MegaShowdownItems.MEWTWONITE_Y.get())
                .add(MegaShowdownItems.MEWTWONITE_X.get())
                .add(MegaShowdownItems.PIDGEOTITE.get())
                .add(MegaShowdownItems.PINSIRITE.get())
                .add(MegaShowdownItems.SABLENITE.get())
                .add(MegaShowdownItems.SALAMENCITE.get())
                .add(MegaShowdownItems.SCIZORITE.get())
                .add(MegaShowdownItems.SCEPTILITE.get())
                .add(MegaShowdownItems.SHARPEDONITE.get())
                .add(MegaShowdownItems.SLOWBRONITE.get())
                .add(MegaShowdownItems.STEELIXITE.get())
                .add(MegaShowdownItems.SWAMPERTITE.get())
                .add(MegaShowdownItems.TYRANITARITE.get())
                .add(MegaShowdownItems.VENUSAURITE.get());

        tag(MegaShowdownTags.Items.TERA_SHARD)
                .add(MegaShowdownItems.NORMAL_TERA_SHARD.get())
                .add(MegaShowdownItems.FIRE_TERA_SHARD.get())
                .add(MegaShowdownItems.WATER_TERA_SHARD.get())
                .add(MegaShowdownItems.ELECTRIC_TERA_SHARD.get())
                .add(MegaShowdownItems.GRASS_TERA_SHARD.get())
                .add(MegaShowdownItems.ICE_TERA_SHARD.get())
                .add(MegaShowdownItems.FIGHTING_TERA_SHARD.get())
                .add(MegaShowdownItems.POISON_TERA_SHARD.get())
                .add(MegaShowdownItems.GROUND_TERA_SHARD.get())
                .add(MegaShowdownItems.FLYING_TERA_SHARD.get())
                .add(MegaShowdownItems.PSYCHIC_TERA_SHARD.get())
                .add(MegaShowdownItems.BUG_TERA_SHARD.get())
                .add(MegaShowdownItems.ROCK_TERA_SHARD.get())
                .add(MegaShowdownItems.GHOST_TERA_SHARD.get())
                .add(MegaShowdownItems.DRAGON_TERA_SHARD.get())
                .add(MegaShowdownItems.DARK_TERA_SHARD.get())
                .add(MegaShowdownItems.STEEL_TERA_SHARD.get())
                .add(MegaShowdownItems.FAIRY_TERA_SHARD.get());

        tag(MegaShowdownTags.Items.TERA_SHARD)
                .add(MegaShowdownItems.ROTOM_CATALOGUE.get())
                .add(MegaShowdownBlocks.ROTOM_FAN.get().asItem())
                .add(MegaShowdownBlocks.ROTOM_WASHING_MACHINE.get().asItem())
                .add(MegaShowdownBlocks.ROTOM_OVEN.get().asItem())
                .add(MegaShowdownBlocks.ROTOM_MOW.get().asItem())
                .add(MegaShowdownBlocks.ROTOM_FRIDGE.get().asItem());
    }
}