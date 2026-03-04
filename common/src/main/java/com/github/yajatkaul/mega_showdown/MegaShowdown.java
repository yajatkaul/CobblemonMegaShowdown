package com.github.yajatkaul.mega_showdown;

import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlockEntities;
import com.github.yajatkaul.mega_showdown.block.MegaShowdownBlocks;
import com.github.yajatkaul.mega_showdown.command.MegaShowdownCommands;
import com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.creative.MegaShowdownTabs;
import com.github.yajatkaul.mega_showdown.event.EventRegister;
import com.github.yajatkaul.mega_showdown.item.MegaShowdownItems;
import com.github.yajatkaul.mega_showdown.networking.server.MegaShowdownNetworkHandlerServer;
import com.github.yajatkaul.mega_showdown.screen.MegaShowdownMenuTypes;
import com.github.yajatkaul.mega_showdown.sound.MegaShowdownSounds;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MegaShowdown {
    public static final String MOD_ID = "mega_showdown";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        MegaShowdownConfig.load();
        MegaShowdownDataComponents.register();
        MegaShowdownBlocks.register();
        MegaShowdownBlockEntities.register();
        MegaShowdownItems.register();
        MegaShowdownSounds.register();
        CommandRegistrationEvent.EVENT.register(MegaShowdownCommands::registerCommands);

        MegaShowdownTabs.register();

        MegaShowdownNetworkHandlerServer.register();

        MegaShowdownMenuTypes.register();

        EventRegister.register();
    }
}
