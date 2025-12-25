package com.github.yajatkaul.mega_showdown.tag;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class MegaShowdownTags {
    public static class Blocks {
        public static final TagKey<Block> POWER_SPOT = createTag("power_spot");

        private static TagKey<Block> createTag(String string) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, string));
        }
    }

    public static class Items {
        public static final TagKey<Item> MEGA_BRACELET = createTag("mega_bracelet");
        public static final TagKey<Item> MEGA_STONE = createTag("mega_stone");
        public static final TagKey<Item> Z_CRYSTAL = createTag("z_crystal");
        public static final TagKey<Item> Z_RING = createTag("z_ring");
        public static final TagKey<Item> TERA_ORB = createTag("tera_orb");
        public static final TagKey<Item> TERA_SHARD = createTag("tera_shard");
        public static final TagKey<Item> DYNAMAX_BAND = createTag("dynamax_band");

        public static final TagKey<Item> OMNI_RING = createTag("omni_ring");

        public static final TagKey<Item> ROTOM_APPLIANCES = createTag("rotom_appliances");

        private static TagKey<Item> createTag(String string) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, string));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> sandyKey = TagKey.create(
                Registries.BIOME,
                ResourceLocation.tryParse("cobblemon:is_sandy")
        );
        public static final TagKey<Biome> trashKey = TagKey.create(
                Registries.BIOME,
                ResourceLocation.tryParse("cobblemon:is_cave")
        );
        public static final TagKey<Biome> forestKey = TagKey.create(
                Registries.BIOME,
                ResourceLocation.tryParse("cobblemon:is_forest")
        );
    }
}
