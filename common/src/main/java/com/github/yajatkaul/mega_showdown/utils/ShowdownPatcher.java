package com.github.yajatkaul.mega_showdown.utils;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ShowdownPatcher {
    private static final String SHOWDOWN_VERSION = "1.3.2";
    private static final Gson GSON = new Gson();

    public static void patch() {
        if (MegaShowdownConfig.msdPatchAutoUpdate) {
            Path showdown_sim = Path.of("./showdown/sim");
            Path showdown_data = Path.of("./showdown/data");
            Path showdown = Path.of("./showdown");
            Path showdown_mod_data = Path.of("./showdown/data/mods/cobblemon");
            Path versionFile = showdown.resolve("MSDPatch.json");

            try {
                Files.createDirectories(showdown_sim);
                Files.createDirectories(showdown_data);

                if (isUpToDate(versionFile)) {
                    MegaShowdown.LOGGER.info("Showdown files are up to date (v{}).", SHOWDOWN_VERSION);
                    return;
                }

                yoink("/assets/mega_showdown/showdown/moves.js", showdown_data.resolve("moves.js"));
                yoink("/assets/mega_showdown/showdown/battle-actions.js", showdown_sim.resolve("battle-actions.js"));
                yoink("/assets/mega_showdown/showdown/battle.js", showdown_sim.resolve("battle.js"));
                yoink("/assets/mega_showdown/showdown/pokemon.js", showdown_sim.resolve("pokemon.js"));
                yoink("/assets/mega_showdown/showdown/abilities.js", showdown_data.resolve("abilities.js"));
                yoink("/assets/mega_showdown/showdown/side.js", showdown_sim.resolve("side.js"));
                yoink("/assets/mega_showdown/showdown/conditions.js", showdown_data.resolve("conditions.js"));
                yoink("/assets/mega_showdown/showdown/index.js", showdown.resolve("index.js"));

                Files.createDirectories(showdown_mod_data);
                yoink("/assets/mega_showdown/showdown/mods/items.js", showdown_mod_data.resolve("items.js"));
                yoink("/assets/mega_showdown/showdown/mods/conditions.js", showdown_mod_data.resolve("conditions.js"));
                yoink("/assets/mega_showdown/showdown/mods/typechart.js", showdown_mod_data.resolve("typechart.js"));
                yoink("/assets/mega_showdown/showdown/mods/moves.js", showdown_mod_data.resolve("moves.js"));
                yoink("/assets/mega_showdown/showdown/mods/abilities.js", showdown_mod_data.resolve("abilities.js"));

                writeVersionFile(versionFile);
                MegaShowdown.LOGGER.info("All files are ready!");
            } catch (IOException e) {
                MegaShowdown.LOGGER.error("Failed to prepare required files: {}", e.getMessage());
            }
        }
    }

    private static boolean isUpToDate(Path versionFile) {
        if (!Files.exists(versionFile)) return false;
        try (Reader reader = Files.newBufferedReader(versionFile)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            return json != null && SHOWDOWN_VERSION.equals(json.get("version").getAsString());
        } catch (Exception e) {
            return false;
        }
    }

    private static void writeVersionFile(Path versionFile) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("version", SHOWDOWN_VERSION);
        Files.writeString(versionFile, GSON.toJson(json));
    }

    private static void yoink(String resourcePath, Path targetPath) {
        try (InputStream inputStream = ShowdownPatcher.class.getResourceAsStream(resourcePath)) {
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