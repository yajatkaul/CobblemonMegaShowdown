package com.github.yajatkaul.mega_showdown.neoforge.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlocks;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(arg, completableFuture);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.KEYSTONE_BLOCK.get())
                .pattern("KKK")
                .pattern("KKK")
                .pattern("KKK")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaShowdownItems.KEYSTONE.get(), 9)
                .requires(MegaShowdownBlocks.KEYSTONE_BLOCK.get())
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.PIKA_CASE.get())
                .pattern("YYY")
                .pattern("IEI")
                .pattern("III")
                .define('Y', Items.YELLOW_DYE)
                .define('I', Items.IRON_INGOT)
                .define('E', CobblemonItems.ELECTRIC_GEM)
                .unlockedBy("has_electric_gem", has(CobblemonItems.ELECTRIC_GEM)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.OMNI_RING.get())
                .pattern(" W ")
                .pattern("KNS")
                .pattern(" T ")
                .define('W', MegaShowdownItems.WISHING_STAR.get())
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('N', Items.NETHERITE_INGOT)
                .define('S', Ingredient.of(
                        MegaShowdownItems.SPARKLING_STONE_DARK.get(),
                        MegaShowdownItems.SPARKLING_STONE_LIGHT.get()
                ))
                .define('T', MegaShowdownItems.TERA_ORB.get())
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_BRACELET.get())
                .pattern("IDI")
                .pattern("AKA")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.WHITE_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_RED_BRACELET.get())
                .pattern("IDI")
                .pattern("AKA")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.RED_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_BLACK_BRACELET.get())
                .pattern("IDI")
                .pattern("AKA")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.BLACK_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_BLUE_BRACELET.get())
                .pattern("IDI")
                .pattern("AKA")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.BLUE_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_GREEN_BRACELET.get())
                .pattern("IDI")
                .pattern("AKA")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.GREEN_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_PINK_BRACELET.get())
                .pattern("IDI")
                .pattern("AKA")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.PINK_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_YELLOW_BRACELET.get())
                .pattern("IDI")
                .pattern("AKA")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.YELLOW_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MAXIE_GLASSES.get())
                .pattern("   ")
                .pattern("DKD")
                .pattern("IGI")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_INGOT)
                .define('G', CobblemonItems.WISE_GLASSES)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.ARCHIE_ANCHOR.get())
                .pattern("GCG")
                .pattern("GKG")
                .pattern("DGD")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('D', Items.DIAMOND)
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.CHAIN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.BRENDAN_MEGA_CUFF.get())
                .pattern(" N ")
                .pattern("AKA")
                .pattern("ADA")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.RED_APRICORN)
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.LYSANDRE_RING.get())
                .pattern("IDI")
                .pattern("IKI")
                .pattern("III")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.KORRINA_GLOVE.get())
                .pattern(" D ")
                .pattern("IKI")
                .pattern("AAA")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.RED_APRICORN)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MEGA_RING.get())
                .pattern("ADA")
                .pattern("AKA")
                .pattern("IAI")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('D', Items.DIAMOND)
                .define('A', CobblemonItems.BLACK_APRICORN)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.N_LUNARIZER.get())
                .pattern("IWI")
                .pattern("ISI")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('W', Items.LIGHT_BLUE_WOOL)
                .define('S', CobblemonItems.MOON_STONE)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_moonstone", has(CobblemonItems.MOON_STONE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.N_SOLARIZER.get())
                .pattern("IWI")
                .pattern("ISI")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('W', Items.ORANGE_WOOL)
                .define('S', CobblemonItems.SUN_STONE)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_sunstone", has(CobblemonItems.SUN_STONE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_ORB.get())
                .pattern("SES")
                .pattern("GDG")
                .pattern("SBS")
                .define('S', Items.AMETHYST_SHARD)
                .define('G', Items.GLOWSTONE_DUST)
                .define('B', Items.BLAZE_POWDER)
                .define('D', Items.DIAMOND)
                .define('E', Items.ENDER_PEARL)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.DNA_SPLICER.get())
                .pattern(" BN")
                .pattern("BIB")
                .pattern("IB ")
                .define('B', Items.BLAZE_ROD)
                .define('I', Items.IRON_INGOT)
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy("has_netherite", has(Items.NETHERITE_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.MAY_BRACELET.get())
                .pattern("YSY")
                .pattern("IKI")
                .pattern("YPY")
                .define('S', Items.NAUTILUS_SHELL)
                .define('P', CobblemonItems.PINK_APRICORN)
                .define('I', Items.IRON_INGOT)
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('Y', CobblemonItems.YELLOW_APRICORN)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.REINS_OF_UNITY.get())
                .pattern("SGS")
                .pattern("L  ")
                .pattern("SIS")
                .define('L', Items.LAPIS_LAZULI)
                .define('S', Items.STRING)
                .define('I', CobblemonItems.ICE_GEM)
                .define('G', CobblemonItems.GHOST_GEM)
                .unlockedBy("has_gem", has(CobblemonItems.GHOST_GEM)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.ROTOM_FAN.get())
                .pattern(" C ")
                .pattern("CWC")
                .pattern(" B ")
                .define('C', Items.COPPER_INGOT)
                .define('W', Items.WIND_CHARGE)
                .define('B', Items.COPPER_BLOCK)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.ROTOM_FRIDGE.get())
                .pattern("BBB")
                .pattern("DID")
                .pattern("BRB")
                .define('D', Items.IRON_DOOR)
                .define('R', Items.REDSTONE)
                .define('I', Items.ICE)
                .define('B', Items.COPPER_BLOCK)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.ROTOM_MOW.get())
                .pattern(" C ")
                .pattern("BMB")
                .pattern("RRR")
                .define('C', Items.COPPER_INGOT)
                .define('R', Items.REDSTONE)
                .define('M', Items.MINECART)
                .define('B', Items.COPPER_BLOCK)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.ROTOM_OVEN.get())
                .pattern("BBB")
                .pattern("DTD")
                .pattern("BRB")
                .define('T', Items.IRON_TRAPDOOR)
                .define('R', Items.REDSTONE)
                .define('D', Items.BLAZE_POWDER)
                .define('B', Items.COPPER_BLOCK)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.ROTOM_WASHING_MACHINE.get())
                .pattern("BBB")
                .pattern("BCB")
                .pattern("BRB")
                .define('C', Items.CAULDRON)
                .define('R', Items.REDSTONE)
                .define('B', Items.COPPER_BLOCK)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.ROTOM_CATALOGUE.get())
                .pattern("   ")
                .pattern("WBG")
                .pattern("FMO")
                .define('F', MegaShowdownBlocks.ROTOM_FAN.get())
                .define('W', MegaShowdownBlocks.ROTOM_WASHING_MACHINE.get())
                .define('M', MegaShowdownBlocks.ROTOM_MOW.get())
                .define('O', MegaShowdownBlocks.ROTOM_OVEN.get())
                .define('G', MegaShowdownBlocks.ROTOM_FRIDGE.get())
                .define('B', Items.BOOK)
                .unlockedBy("has_book", has(Items.BOOK)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.PINK_NECTAR.get())
                .pattern("   ")
                .pattern("HPH")
                .pattern(" H ")
                .define('H', Items.HONEYCOMB)
                .define('P', Items.PEONY)
                .unlockedBy("has_honey", has(Items.HONEYCOMB)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.RED_NECTAR.get())
                .pattern("   ")
                .pattern("HRH")
                .pattern(" H ")
                .define('H', Items.HONEYCOMB)
                .define('R', Items.ROSE_BUSH)
                .unlockedBy("has_honey", has(Items.HONEYCOMB)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.YELLOW_NECTAR.get())
                .pattern("   ")
                .pattern("HYH")
                .pattern(" H ")
                .define('H', Items.HONEYCOMB)
                .define('Y', Items.SUNFLOWER)
                .unlockedBy("has_honey", has(Items.HONEYCOMB)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.PURPLE_NECTAR.get())
                .pattern("   ")
                .pattern("HPH")
                .pattern(" H ")
                .define('H', Items.HONEYCOMB)
                .define('P', Items.LILAC)
                .unlockedBy("has_honey", has(Items.HONEYCOMB)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.ASH_CAP.get())
                .pattern(" WR")
                .pattern("RGR")
                .pattern("RWR")
                .define('W', Items.WHITE_WOOL)
                .define('R', Items.RED_WOOL)
                .define('G', Items.GREEN_WOOL)
                .unlockedBy("has_wool", has(Items.WHITE_WOOL)).save(recipeOutput);

// Water
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_WATER_ORE.get()), RecipeCategory.MISC, CobblemonItems.WATER_STONE, 0.7f, 100)
                .unlockedBy("has_mega_water_ore", has(MegaShowdownBlocks.MEGA_METEORID_WATER_ORE.get())).save(recipeOutput, "water_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_WATER_ORE.get()), RecipeCategory.MISC, CobblemonItems.WATER_STONE, 0.7f, 200)
                .unlockedBy("has_mega_water_ore", has(MegaShowdownBlocks.MEGA_METEORID_WATER_ORE.get())).save(recipeOutput, "water_stone_smelting");

// Dawn
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_DAWN_ORE.get()), RecipeCategory.MISC, CobblemonItems.DAWN_STONE, 0.7f, 100)
                .unlockedBy("has_mega_dawn_ore", has(MegaShowdownBlocks.MEGA_METEORID_DAWN_ORE.get())).save(recipeOutput, "dawn_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_DAWN_ORE.get()), RecipeCategory.MISC, CobblemonItems.DAWN_STONE, 0.7f, 200)
                .unlockedBy("has_mega_dawn_ore", has(MegaShowdownBlocks.MEGA_METEORID_DAWN_ORE.get())).save(recipeOutput, "dawn_stone_smelting");

// Dusk
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_DUSK_ORE.get()), RecipeCategory.MISC, CobblemonItems.DUSK_STONE, 0.7f, 100)
                .unlockedBy("has_mega_dusk_ore", has(MegaShowdownBlocks.MEGA_METEORID_DUSK_ORE.get())).save(recipeOutput, "dusk_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_DUSK_ORE.get()), RecipeCategory.MISC, CobblemonItems.DUSK_STONE, 0.7f, 200)
                .unlockedBy("has_mega_dusk_ore", has(MegaShowdownBlocks.MEGA_METEORID_DUSK_ORE.get())).save(recipeOutput, "dusk_stone_smelting");

// Fire
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_FIRE_ORE.get()), RecipeCategory.MISC, CobblemonItems.FIRE_STONE, 0.7f, 100)
                .unlockedBy("has_mega_fire_ore", has(MegaShowdownBlocks.MEGA_METEORID_FIRE_ORE.get())).save(recipeOutput, "fire_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_FIRE_ORE.get()), RecipeCategory.MISC, CobblemonItems.FIRE_STONE, 0.7f, 200)
                .unlockedBy("has_mega_fire_ore", has(MegaShowdownBlocks.MEGA_METEORID_FIRE_ORE.get())).save(recipeOutput, "fire_stone_smelting");

// Ice
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_ICE_ORE.get()), RecipeCategory.MISC, CobblemonItems.ICE_STONE, 0.7f, 100)
                .unlockedBy("has_mega_ice_ore", has(MegaShowdownBlocks.MEGA_METEORID_ICE_ORE.get())).save(recipeOutput, "ice_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_ICE_ORE.get()), RecipeCategory.MISC, CobblemonItems.ICE_STONE, 0.7f, 200)
                .unlockedBy("has_mega_ice_ore", has(MegaShowdownBlocks.MEGA_METEORID_ICE_ORE.get())).save(recipeOutput, "ice_stone_smelting");

// Leaf
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_LEAF_ORE.get()), RecipeCategory.MISC, CobblemonItems.LEAF_STONE, 0.7f, 100)
                .unlockedBy("has_mega_leaf_ore", has(MegaShowdownBlocks.MEGA_METEORID_LEAF_ORE.get())).save(recipeOutput, "leaf_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_LEAF_ORE.get()), RecipeCategory.MISC, CobblemonItems.LEAF_STONE, 0.7f, 200)
                .unlockedBy("has_mega_leaf_ore", has(MegaShowdownBlocks.MEGA_METEORID_LEAF_ORE.get())).save(recipeOutput, "leaf_stone_smelting");

// Moon
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_MOON_ORE.get()), RecipeCategory.MISC, CobblemonItems.MOON_STONE, 0.7f, 100)
                .unlockedBy("has_mega_moon_ore", has(MegaShowdownBlocks.MEGA_METEORID_MOON_ORE.get())).save(recipeOutput, "moon_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_MOON_ORE.get()), RecipeCategory.MISC, CobblemonItems.MOON_STONE, 0.7f, 200)
                .unlockedBy("has_mega_moon_ore", has(MegaShowdownBlocks.MEGA_METEORID_MOON_ORE.get())).save(recipeOutput, "moon_stone_smelting");

// Shiny
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_SHINY_ORE.get()), RecipeCategory.MISC, CobblemonItems.SHINY_STONE, 0.7f, 100)
                .unlockedBy("has_mega_shiny_ore", has(MegaShowdownBlocks.MEGA_METEORID_SHINY_ORE.get())).save(recipeOutput, "shiny_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_SHINY_ORE.get()), RecipeCategory.MISC, CobblemonItems.SHINY_STONE, 0.7f, 200)
                .unlockedBy("has_mega_shiny_ore", has(MegaShowdownBlocks.MEGA_METEORID_SHINY_ORE.get())).save(recipeOutput, "shiny_stone_smelting");

// Sun
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_SUN_ORE.get()), RecipeCategory.MISC, CobblemonItems.SUN_STONE, 0.7f, 100)
                .unlockedBy("has_mega_sun_ore", has(MegaShowdownBlocks.MEGA_METEORID_SUN_ORE.get())).save(recipeOutput, "sun_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_SUN_ORE.get()), RecipeCategory.MISC, CobblemonItems.SUN_STONE, 0.7f, 200)
                .unlockedBy("has_mega_sun_ore", has(MegaShowdownBlocks.MEGA_METEORID_SUN_ORE.get())).save(recipeOutput, "sun_stone_smelting");

// Thunder
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_THUNDER_ORE.get()), RecipeCategory.MISC, CobblemonItems.THUNDER_STONE, 0.7f, 100)
                .unlockedBy("has_mega_thunder_ore", has(MegaShowdownBlocks.MEGA_METEORID_THUNDER_ORE.get())).save(recipeOutput, "thunder_stone_blasting");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MegaShowdownBlocks.MEGA_METEORID_THUNDER_ORE.get()), RecipeCategory.MISC, CobblemonItems.THUNDER_STONE, 0.7f, 200)
                .unlockedBy("has_mega_thunder_ore", has(MegaShowdownBlocks.MEGA_METEORID_THUNDER_ORE.get())).save(recipeOutput, "thunder_stone_smelting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.DYNAMAX_BAND.get())
                .pattern("PPP")
                .pattern("IWI")
                .pattern("BBB")
                .define('P', CobblemonItems.PINK_APRICORN)
                .define('I', Items.IRON_INGOT)
                .define('W', MegaShowdownItems.WISHING_STAR.get())
                .define('B', CobblemonItems.BLUE_APRICORN)
                .unlockedBy("has_wishing_star", has(MegaShowdownItems.WISHING_STAR.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.POWER_SPOT.get())
                .pattern("RMR")
                .pattern("RWR")
                .pattern("SSS")
                .define('R', Items.REDSTONE)
                .define('W', MegaShowdownItems.WISHING_STAR.get())
                .define('S', Items.STONE)
                .define('M', MegaShowdownBlocks.MAX_MUSHROOM.get())
                .unlockedBy("has_wishing_star", has(MegaShowdownItems.WISHING_STAR.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.DYNAMAX_CANDY.get())
                .pattern(" E ")
                .pattern("EME")
                .pattern(" E ")
                .define('E', CobblemonItems.EXPERIENCE_CANDY_S)
                .define('M', MegaShowdownBlocks.MAX_MUSHROOM.get())
                .unlockedBy("has_max_mushroom", has(MegaShowdownBlocks.MAX_MUSHROOM.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaShowdownItems.MAX_HONEY.get())
                .requires(Items.HONEY_BOTTLE)
                .requires(MegaShowdownBlocks.MAX_MUSHROOM.get())
                .unlockedBy("has_max_mushroom", has(MegaShowdownBlocks.MAX_MUSHROOM.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', CobblemonItems.WHITE_APRICORN)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING_BLACK.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', CobblemonItems.BLACK_APRICORN)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING_YELLOW.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', CobblemonItems.YELLOW_APRICORN)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING_RED.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', CobblemonItems.RED_APRICORN)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.OLIVIAS_Z_RING.get())
                .pattern("PSP")
                .pattern("CAC")
                .pattern("CIC")
                .define('P', CobblemonItems.PINK_APRICORN)
                .define('I', Items.IRON_NUGGET)
                .define('C', Items.COPPER_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.HAPUS_Z_RING.get())
                .pattern("GSG")
                .pattern("LYL")
                .pattern("LLL")
                .define('G', Items.GOLD_INGOT)
                .define('L', Items.LEATHER)
                .define('Y', CobblemonItems.YELLOW_APRICORN)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING_PINK.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', CobblemonItems.PINK_APRICORN)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING_BLUE.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', CobblemonItems.BLUE_APRICORN)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING_GREEN.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_LIGHT.get())
                .define('A', CobblemonItems.GREEN_APRICORN)
                .unlockedBy("has_light_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_LIGHT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.Z_RING_POWER.get())
                .pattern("ISI")
                .pattern("A A")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_DARK.get())
                .define('A', CobblemonItems.BLACK_APRICORN)
                .unlockedBy("has_dark_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_DARK.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.OLIVIA_Z_POWER_RING.get())
                .pattern("ASA")
                .pattern("NIN")
                .pattern("NNN")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_DARK.get())
                .define('N', Items.IRON_NUGGET)
                .define('A', CobblemonItems.PINK_APRICORN)
                .unlockedBy("has_dark_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_DARK.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.GLADION_Z_POWER_RING.get())
                .pattern("ASA")
                .pattern("AIA")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_DARK.get())
                .define('A', CobblemonItems.RED_APRICORN)
                .unlockedBy("has_dark_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_DARK.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.NANU_Z_POWER_RING.get())
                .pattern("GSG")
                .pattern("AIA")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_DARK.get())
                .define('A', CobblemonItems.BLACK_APRICORN)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_dark_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_DARK.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.HAPU_Z_POWER_RING.get())
                .pattern("GSG")
                .pattern("AIA")
                .pattern("LLL")
                .define('I', Items.IRON_INGOT)
                .define('L', Items.LEATHER)
                .define('S', MegaShowdownItems.SPARKLING_STONE_DARK.get())
                .define('A', CobblemonItems.YELLOW_APRICORN)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_dark_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_DARK.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.ROCKET_Z_POWER_RING.get())
                .pattern("ISI")
                .pattern("RIB")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', MegaShowdownItems.SPARKLING_STONE_DARK.get())
                .define('B', CobblemonItems.BLUE_APRICORN)
                .define('R', CobblemonItems.RED_APRICORN)
                .unlockedBy("has_dark_sparkling_stone", has(MegaShowdownItems.SPARKLING_STONE_DARK.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.SHOCK_DRIVE.get())
                .pattern("IEI")
                .pattern("RNR")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('N', Items.NETHERITE_SCRAP)
                .define('E', CobblemonItems.ELECTRIC_GEM)
                .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.BURN_DRIVE.get())
                .pattern("IFI")
                .pattern("RNR")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('N', Items.NETHERITE_SCRAP)
                .define('F', CobblemonItems.FIRE_GEM)
                .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.CHILL_DRIVE.get())
                .pattern("ICI")
                .pattern("RNR")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('N', Items.NETHERITE_SCRAP)
                .define('C', CobblemonItems.ICE_GEM)
                .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.DOUSE_DRIVE.get())
                .pattern("IWI")
                .pattern("RNR")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('N', Items.NETHERITE_SCRAP)
                .define('W', CobblemonItems.WATER_GEM)
                .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.ZYGARDE_CUBE.get())
                .pattern("BGB")
                .pattern("GNG")
                .pattern("BGB")
                .define('G', CobblemonItems.GREEN_APRICORN)
                .define('B', CobblemonItems.BLACK_APRICORN)
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy("has_netherite", has(Items.NETHERITE_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.PEDESTAL.get())
                .pattern(" S ")
                .pattern(" B ")
                .pattern("   ")
                .define('S', Items.SMOOTH_STONE_SLAB)
                .define('B', Items.CHISELED_STONE_BRICKS)
                .unlockedBy("has_chiseled_stone_bricks", has(Items.CHISELED_STONE_BRICKS)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownBlocks.REASSEMBLY_UNIT.get())
                .pattern("RRR")
                .pattern("RTR")
                .pattern("III")
                .define('R', Items.REDSTONE)
                .define('T', CobblemonItems.RESTORATION_TANK)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("has_restoration_tank", has(CobblemonItems.RESTORATION_TANK)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.LISIA_MEGA_TIARA.get())
                .pattern("L L")
                .pattern("IKI")
                .pattern("L L")
                .define('K', MegaShowdownItems.KEYSTONE.get())
                .define('L', Items.LAPIS_LAZULI)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_keystone", has(MegaShowdownItems.KEYSTONE.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.LIKOS_PENDANT.get())
                .pattern("S S")
                .pattern(" D ")
                .pattern("   ")
                .define('D', MegaShowdownBlocks.DORMANT_CRYSTAL.get())
                .define('S', Items.STRING)
                .unlockedBy("has_dormant_crystal", has(MegaShowdownBlocks.DORMANT_CRYSTAL.get())).save(recipeOutput);

// ─────────────────────────────
// SHAPED CRAFTING RECIPES
// ─────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get(), 4)
                .pattern("MM")
                .pattern("MM")
                .define('M', MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get())
                .unlockedBy("has_megameteoroid", has(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.MEGA_METEOROID_BRICK.get(), 4)
                .pattern("MM")
                .pattern("MM")
                .define('M', MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get())
                .unlockedBy("has_polished_meteor", has(MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.CHISELED_MEGA_METEOROID_BLOCK.get())
                .pattern("#")
                .pattern("#")
                .define('#', MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get())
                .unlockedBy("has_meteor_block", has(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.CHISELED_MEGA_METEOROID_BRICK.get())
                .pattern("#")
                .pattern("#")
                .define('#', MegaShowdownBlocks.MEGA_METEOROID_BRICK.get())
                .unlockedBy("has_meteor_brick", has(MegaShowdownBlocks.MEGA_METEOROID_BRICK.get()))
                .save(recipeOutput);


// ─────────────────────────────
// STONECUTTING RECIPES
// ─────────────────────────────
        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get()
                )
                .unlockedBy("has_meteor", has(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()))
                .save(recipeOutput, "polished_meteor_from_block_stonecutting");

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.MEGA_METEOROID_BRICK.get()
                )
                .unlockedBy("has_polished_meteor", has(MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get()))
                .save(recipeOutput, "meteor_brick_from_polished_stonecutting");

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(MegaShowdownBlocks.MEGA_METEOROID_BRICK.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.CHISELED_MEGA_METEOROID_BLOCK.get()
                )
                .unlockedBy("has_bricks", has(MegaShowdownBlocks.MEGA_METEOROID_BRICK.get()))
                .save(recipeOutput, "chiseled_meteor_from_bricks_stonecutting");

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(MegaShowdownBlocks.MEGA_METEOROID_BRICK.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.CHISELED_MEGA_METEOROID_BRICK.get()
                )
                .unlockedBy("has_bricks", has(MegaShowdownBlocks.MEGA_METEOROID_BRICK.get()))
                .save(recipeOutput, "chiseled_brick_from_bricks_stonecutting");


// ─────────────────────────────
// SMELTING + BLASTING (custom recipe names)
// ─────────────────────────────
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get(),
                        0.1f,
                        200
                )
                .unlockedBy("has_meteor", has(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()))
                .save(recipeOutput, "polished_meteor_from_smelting");

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        MegaShowdownBlocks.POLISHED_MEGA_METEOROID_BLOCK.get(),
                        0.1f,
                        100
                )
                .unlockedBy("has_meteor", has(MegaShowdownBlocks.MEGA_METEOROID_BLOCK.get()))
                .save(recipeOutput, "polished_meteor_from_blasting");


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_WHITE.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.WHITE_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_ORANGE.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.ORANGE_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_MAGENTA.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.MAGENTA_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_LIGHT_BLUE.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.LIGHT_BLUE_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_YELLOW.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.YELLOW_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_LIME.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.LIME_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_PINK.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.PINK_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_GRAY.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.GRAY_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_LIGHT_GRAY.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.LIGHT_GRAY_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_CYAN.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.CYAN_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_PURPLE.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.PURPLE_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_BLUE.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.BLUE_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_BROWN.get())
                .pattern("   ")
                .pattern("LPL")
                .pattern("LLL")
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_GREEN.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.GREEN_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_RED.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.RED_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaShowdownItems.TERA_POUCH_BLACK.get())
                .pattern(" D ")
                .pattern("LPL")
                .pattern("LLL")
                .define('D', Items.BLACK_DYE)
                .define('L', Items.LEATHER)
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);
    }
}