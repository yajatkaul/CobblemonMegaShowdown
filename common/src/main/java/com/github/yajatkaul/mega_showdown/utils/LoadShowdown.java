package com.github.yajatkaul.mega_showdown.utils;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class LoadShowdown {
    public void load() {
        if (MegaShowdownConfig.showdownFilesLoading) {
            Path showdown_sim = Path.of("./showdown/sim");
            Path showdown_data = Path.of("./showdown/data");
            Path showdown = Path.of("./showdown");
            Path showdown_mod_data = Path.of("./showdown/data/mods/cobblemon");

            try {
                Files.createDirectories(showdown_sim);
                Files.createDirectories(showdown_data);

                yoink("/assets/mega_showdown/showdown/moves.js", showdown_data.resolve("moves.js"));
                yoink("/assets/mega_showdown/showdown/battle-actions.js", showdown_sim.resolve("battle-actions.js"));
                yoink("/assets/mega_showdown/showdown/pokemon.js", showdown_sim.resolve("pokemons.js"));
                yoink("/assets/mega_showdown/showdown/abilities.js", showdown_data.resolve("abilities.js"));
                yoink("/assets/mega_showdown/showdown/side.js", showdown_sim.resolve("side.js"));
                yoink("/assets/mega_showdown/showdown/conditions.js", showdown_data.resolve("conditions.js"));
                yoink("/assets/mega_showdown/showdown/index.js", showdown.resolve("index.js"));

                if (!Files.exists(showdown_mod_data.resolve("items.js"))) {
                    yoink("/assets/mega_showdown/showdown/mods/items.js", showdown_mod_data.resolve("items.js"));
                }
                if (!Files.exists(showdown_mod_data.resolve("conditions.js"))) {
                    yoink("/assets/mega_showdown/showdown/mods/conditions.js", showdown_mod_data.resolve("conditions.js"));
                }
                if (!Files.exists(showdown_mod_data.resolve("typechart.js"))) {
                    yoink("/assets/mega_showdown/showdown/mods/typechart.js", showdown_mod_data.resolve("typechart.js"));
                }
                if (!Files.exists(showdown_mod_data.resolve("moves.js"))) {
                    yoink("/assets/mega_showdown/showdown/mods/moves.js", showdown_mod_data.resolve("moves.js"));
                }
                if (!Files.exists(showdown_mod_data.resolve("abilities.js"))) {
                    yoink("/assets/mega_showdown/showdown/mods/abilities.js", showdown_mod_data.resolve("abilities.js"));
                }

                MegaShowdown.LOGGER.info("All files are ready!");
            } catch (IOException e) {
                MegaShowdown.LOGGER.error("Failed to prepare required files: {}", e.getMessage());
            }
        }
    }

    private void yoink(String resourcePath, Path targetPath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                MegaShowdown.LOGGER.error("Fallback file not found: {}", resourcePath);
                return;
            }
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            MegaShowdown.LOGGER.info("Loaded fallback file: {}", targetPath);
        } catch (IOException e) {
            MegaShowdown.LOGGER.error("Failed to copy fallback file {}: {}", resourcePath, e.getMessage());
        }
    }
}
