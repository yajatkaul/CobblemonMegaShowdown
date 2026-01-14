package com.github.yajatkaul.mega_showdown.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.feature.SpeciesFeatureAssignments;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.datapack.MegaShowdownDatapackRegister;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import com.github.yajatkaul.mega_showdown.utils.RegistryLocator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MegaShowdownCommands {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection environment) {
        dispatcher.register(literal("msd")
                .then(literal("hard_reset_mega")
                        .executes(MegaShowdownCommands::hard_reset_mega))
                .then(literal("reload")
                        .requires(req -> req.hasPermission(4))
                        .executes(MegaShowdownCommands::reload))
                .then(literal("apply")
                        .requires(req -> req.hasPermission(4))
                        .then(argument("type", StringArgumentType.string())
                                .suggests(((cxt, builder) -> {
                                    builder.suggest(RegistryLocator.SOLO_FUSION);
                                    builder.suggest(RegistryLocator.MEGA);
                                    builder.suggest(RegistryLocator.SHOWDOWN_ITEM);
                                    builder.suggest(RegistryLocator.HELD_FORM_CHANGE);
                                    builder.suggest(RegistryLocator.DU_FUSION);
                                    builder.suggest(RegistryLocator.FORM_CHANGE_INTERACT);
                                    builder.suggest(RegistryLocator.FORM_CHANGE_TOGGLE_INTERACT);
                                    builder.suggest(RegistryLocator.Z_CRYSTAL_ITEM);
                                    return builder.buildFuture();
                                }))
                                .then(argument("resource_id", StringArgumentType.greedyString())
                                        .executes(MegaShowdownCommands::applyComponent)
                                        .suggests(((cxt, builder) -> {
                                            String type = StringArgumentType.getString(cxt, "type");
                                            switch (type) {
                                                case RegistryLocator.SOLO_FUSION:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.SOLO_FUSION_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                                case RegistryLocator.MEGA:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.MEGA_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                                case RegistryLocator.HELD_FORM_CHANGE:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.HELD_ITEM_FORM_CHANGE_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                                case RegistryLocator.DU_FUSION:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.DU_FUSION_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                                case RegistryLocator.FORM_CHANGE_INTERACT:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.FORM_CHANGE_INTERACT_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                                case RegistryLocator.FORM_CHANGE_TOGGLE_INTERACT:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.FORM_CHANGE_TOGGLE_INTERACT_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                                case RegistryLocator.SHOWDOWN_ITEM:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.SHOWDOWN_ITEM_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                                case RegistryLocator.Z_CRYSTAL_ITEM:
                                                    for (ResourceLocation location : MegaShowdownDatapackRegister.Z_CRYSTAL_ITEM_REGISTRY.keySet()) {
                                                        builder.suggest(String.valueOf(location));
                                                    }
                                                    break;
                                            }
                                            return builder.buildFuture();
                                        })))))
        );
    }

    private static int reload(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        MegaShowdownConfig.load();
        return 1;
    }

    private static int hard_reset_mega(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) {
            return 0;
        }

        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);

        for (Pokemon pokemon : playerPartyStore) {
            boolean hasMega = SpeciesFeatureAssignments.getFeatures(pokemon.getSpecies()).stream()
                    .anyMatch(pokemon.getAspects()::contains);
            if (hasMega) {
                MegaGimmick.unmegaEvolve(pokemon);
            }
        }

        return 1;
    }

    private static int applyComponent(CommandContext<CommandSourceStack> context) {
        String type = StringArgumentType.getString(context, "type");
        String resourceId = StringArgumentType.getString(context, "resource_id");

        ServerPlayer player = context.getSource().getPlayer();

        if (player == null) {
            return 1;
        }

        ItemStack stack = player.getMainHandItem();

        stack.set(MegaShowdownDataComponents.REGISTRY_TYPE_COMPONENT.get(), type);
        stack.set(MegaShowdownDataComponents.RESOURCE_LOCATION_COMPONENT.get(), ResourceLocation.tryParse(resourceId));

        return 0;
    }
}
