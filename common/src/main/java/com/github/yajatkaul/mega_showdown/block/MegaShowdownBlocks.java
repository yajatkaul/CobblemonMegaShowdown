package com.github.yajatkaul.mega_showdown.block;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.block.custom.*;
import com.github.yajatkaul.mega_showdown.block.custom.HorizontalDirectionalBlock;
import com.github.yajatkaul.mega_showdown.creative.MegaShowdownTabs;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import com.github.yajatkaul.mega_showdown.item.custom.dynamax.MaxMushroom;
import com.github.yajatkaul.mega_showdown.item.custom.form_change.DeoxysMeteoridItem;
import com.github.yajatkaul.mega_showdown.item.custom.form_change.Gracedia;
import com.github.yajatkaul.mega_showdown.item.custom.form_change.RotomUnitItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MegaShowdownBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MegaShowdown.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> POWER_SPOT = registerBlockWithToolTip("power_spot",
            () -> new CustomHitBoxBlock(BlockBehaviour
                    .Properties.of()
                    .strength(3f)
                    .mapColor(MapColor.COLOR_RED)
                    .lightLevel((state) -> 15)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE),
                    Block.box(2, 0, 2, 14, 4, 14)),
            MegaShowdownTabs.DYNAMAX_TAB
    );

    public static final RegistrySupplier<Block> MAX_MUSHROOM = registerBlockWithBlockItem(
            "max_mushroom",
            () -> new MaxMushroomBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .sound(SoundType.FLOWERING_AZALEA)
            ),
            block -> new MaxMushroom(block.get(), new Item.Properties().arch$tab(MegaShowdownTabs.DYNAMAX_TAB))
    );

    public static final RegistrySupplier<Block> GRACIDEA_FLOWER = registerBlockWithBlockItem("gracidea_flower",
            () -> new FlowerBlock(MobEffects.HEAL, 8, BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .lightLevel((state) -> 15)
                    .instabreak()
                    .sound(SoundType.GRASS)),
            (block) -> new Gracedia(block.get(), new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB)));

    public static final RegistrySupplier<Block> POTTED_GRACIDEA = registerFlowerPlotBlock("potted_gracidea", GRACIDEA_FLOWER);

    public static final RegistrySupplier<Block> REASSEMBLY_UNIT = registerBlockWithToolTip(
            "reassembly_unit",
            () -> new ReassemblyUnitBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(3f)
                    .pushReaction(PushReaction.IGNORE)),
            MegaShowdownTabs.FORM_TAB
    );

    public static final RegistrySupplier<Block> PEDESTAL = registerBlockWithToolTip(
            "pedestal",
            () -> new PedestalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .sound(SoundType.STONE)
                    .strength(2f)),
            MegaShowdownTabs.KEY_TAB
    );

    public static final RegistrySupplier<Block> MEGA_METEOROID_BLOCK = registerMeteoroidBlock("mega_meteorid_block");
    public static final RegistrySupplier<Block> MEGA_METEOROID_RADIATED_BLOCK = registerMeteoroidBlock("mega_meteorid_radiated_block");
    public static final RegistrySupplier<Block> MEGA_METEOROID_BRICK = registerMeteoroidBlock("mega_meteorid_brick");
    public static final RegistrySupplier<Block> CHISELED_MEGA_METEOROID_BLOCK = registerMeteoroidBlock("chiseled_mega_meteorid_block");
    public static final RegistrySupplier<Block> CHISELED_MEGA_METEOROID_BRICK = registerMeteoroidBlock("chiseled_mega_meteorid_brick");
    public static final RegistrySupplier<Block> POLISHED_MEGA_METEOROID_BLOCK = registerMeteoroidBlock("polished_mega_meteorid_block");

    public static final RegistrySupplier<Block> KEYSTONE_ORE = registerBlock("keystone_ore",
            () -> new DropExperienceBlock(UniformInt.of(6, 9),
                    BlockBehaviour
                            .Properties.of()
                            .strength(3f)
                            .mapColor(MapColor.COLOR_PURPLE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.AMETHYST)),
            MegaShowdownTabs.MEGA_TAB);
    public static final RegistrySupplier<Block> KEYSTONE_BLOCK = registerBlock(
            "keystone_block",
            () -> new HorizontalDirectionalBlock(BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.PINK)),
            MegaShowdownTabs.MEGA_TAB
    );

    public static final RegistrySupplier<Block> MEGA_METEORID_WATER_ORE = registerMeteoroidOre("mega_meteorid_water_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_DAWN_ORE = registerMeteoroidOre("mega_meteorid_dawn_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_DUSK_ORE = registerMeteoroidOre("mega_meteorid_dusk_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_FIRE_ORE = registerMeteoroidOre("mega_meteorid_fire_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_ICE_ORE = registerMeteoroidOre("mega_meteorid_ice_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_LEAF_ORE = registerMeteoroidOre("mega_meteorid_leaf_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_MOON_ORE = registerMeteoroidOre("mega_meteorid_moon_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_SHINY_ORE = registerMeteoroidOre("mega_meteorid_shiny_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_SUN_ORE = registerMeteoroidOre("mega_meteorid_sun_ore");
    public static final RegistrySupplier<Block> MEGA_METEORID_THUNDER_ORE = registerMeteoroidOre("mega_meteorid_thunder_ore");

    public static final RegistrySupplier<Block> MEGA_STONE_CRYSTAL = registerBlock("mega_stone_crystal",
            () -> new MegaStoneStand(BlockBehaviour.Properties.of()
                    .strength(1.5f)
                    .sound(SoundType.MEDIUM_AMETHYST_BUD)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.IGNORE)
                    .lightLevel((state) -> 15)),
            MegaShowdownTabs.MEGA_TAB);

    public static final RegistrySupplier<Block> WISHING_STAR_CRYSTAL = registerBlock("wishing_star_crystal",
            () -> new ParticleBlock(4,
                    3,
                    BlockBehaviour.Properties.of()
                            .strength(1.5f)
                            .sound(SoundType.MEDIUM_AMETHYST_BUD)
                            .noOcclusion()
                            .requiresCorrectToolForDrops()
                            .pushReaction(PushReaction.IGNORE)
                            .lightLevel((state) -> 15),
                    true,
                    Block.box(2, 0, 2, 14, 9, 14),
                    new DustParticleOptions(
                            new Vector3f(1.0f, 0.0f, 0.0f), // Color
                            0.5f
                    )
            ), MegaShowdownTabs.DYNAMAX_TAB
    );

    public static final RegistrySupplier<Block> DORMANT_CRYSTAL = registerBlockWithToolTip("dormant_crystal",
            () -> new DormantCrystal(4, 3, BlockBehaviour.Properties.of(), false), MegaShowdownTabs.TERA_TAB);

    public static final RegistrySupplier<Block> DEOXYS_METEORITE = registerDeoxysMeteorite("deoxys_meteorite");

    public static final RegistrySupplier<Block> ROTOM_WASHING_MACHINE = registerRotomBlock("rotom_washing_machine", "wash");
    public static final RegistrySupplier<Block> ROTOM_FAN = registerRotomBlock("rotom_fan", "fan");
    public static final RegistrySupplier<Block> ROTOM_MOW = registerRotomBlock("rotom_mow", "mow");
    public static final RegistrySupplier<Block> ROTOM_FRIDGE = registerRotomBlock("rotom_fridge", "frost");
    public static final RegistrySupplier<Block> ROTOM_OVEN = registerRotomBlock("rotom_oven", "heat");

    private static RegistrySupplier<Block> registerRotomBlock(String name, String form) {
        return registerBlockWithBlockItem(name, () ->
                        new RotomUnitBlock(BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(2)
                                .mapColor(DyeColor.ORANGE)
                                .sound(SoundType.METAL), form),
                (block) -> new RotomUnitItem(
                        block.get(),
                        new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB),
                        form
                ));
    }

    private static RegistrySupplier<Block> registerDeoxysMeteorite(String name) {
        RegistrySupplier<Block> blockSupplier = BLOCKS.register(name, () -> new PokemonSelectingBlock(
                BlockBehaviour.Properties.of()
                        .strength(3f)
                        .mapColor(MapColor.COLOR_PURPLE)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.STONE),
                ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, name)
        ));

        MegaShowdownItems.ITEMS.register(name,
                () -> new DeoxysMeteoridItem(blockSupplier.get(),
                        new Item.Properties().arch$tab(MegaShowdownTabs.FORM_TAB)));
        return blockSupplier;
    }

    private static RegistrySupplier<Block> registerFlowerPlotBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, () -> new FlowerPotBlock(block.get(),
                BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_ALLIUM)
                        .noOcclusion()
                        .instabreak()
        ));
    }

    private static RegistrySupplier<Block> registerMeteoroidOre(String name) {
        RegistrySupplier<Block> blockSupplier = BLOCKS.register(name, () -> new DropExperienceBlock(
                UniformInt.of(3, 6),
                BlockBehaviour.Properties.of()
                        .strength(3f)
                        .mapColor(MapColor.COLOR_PURPLE)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.STONE)
        ));

        MegaShowdownItems.ITEMS.register(name,
                () -> new BlockItem(blockSupplier.get(),
                        new Item.Properties().arch$tab(MegaShowdownTabs.MEGA_TAB)));
        return blockSupplier;
    }

    private static RegistrySupplier<Block> registerMeteoroidBlock(String name) {
        RegistrySupplier<Block> blockSupplier = BLOCKS.register(name, () -> new Block(
                BlockBehaviour.Properties.of()
                        .strength(3f)
                        .mapColor(MapColor.COLOR_PURPLE)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.STONE)
        ));

        MegaShowdownItems.ITEMS.register(name,
                () -> new BlockItem(blockSupplier.get(),
                        new Item.Properties().arch$tab(MegaShowdownTabs.MEGA_TAB)));
        return blockSupplier;
    }

    private static RegistrySupplier<Block> registerBlockWithBlockItem(String name,
                                                                      Supplier<Block> block,
                                                                      Function<RegistrySupplier<Block>, Item> itemFactory
    ) {
        RegistrySupplier<Block> blockSupplier = BLOCKS.register(name, block);
        MegaShowdownItems.ITEMS.register(name, () -> itemFactory.apply(blockSupplier));
        return blockSupplier;
    }

    private static RegistrySupplier<Block> registerBlock(String name, Supplier<Block> block, DeferredSupplier<CreativeModeTab> tab) {
        RegistrySupplier<Block> blockSupplier = BLOCKS.register(name, block);
        MegaShowdownItems.ITEMS.register(name, () -> new BlockItem(blockSupplier.get(), new Item.Properties().arch$tab(tab)));
        return blockSupplier;
    }

    private static RegistrySupplier<Block> registerBlockWithToolTip(String name, Supplier<Block> block, DeferredSupplier<CreativeModeTab> tab) {
        RegistrySupplier<Block> blockSupplier = BLOCKS.register(name, block);
        MegaShowdownItems.ITEMS.register(name, () -> new BlockItem(blockSupplier.get(), new Item.Properties().arch$tab(tab)) {
            @Override
            public void appendHoverText(ItemStack arg, TooltipContext arg2, List<Component> list, TooltipFlag arg3) {
                list.add(Component.translatable("tooltip.mega_showdown." + name + ".tooltip"));
                super.appendHoverText(arg, arg2, list, arg3);
            }
        });
        return blockSupplier;
    }

    public static void register() {
        BLOCKS.register();
    }
}
