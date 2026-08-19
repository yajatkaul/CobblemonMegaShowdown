package com.github.yajatkaul.mega_showdown.config;

import com.cobblemon.mod.common.Cobblemon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class MegaShowdownConfig {
    private static final String FILE_PATH = "./config/mega_showdown/config.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean loaded = false;

    public static int teraShardRequired = 50;
    public static boolean outSideMega = true;
    /**
     * Friendship a player-owned Pokemon needs before it can Mega Evolve or use a Z-Move.
     * Defaults to Cobblemon's maxPokemonFriendship (see load()) so the out-of-the-box rule is
     * "maxed friendship", but config.json overrides it and is the intended way to tune this.
     */
    public static int megaFriendshipRequirement = 255;
    public static boolean outSideUltraBurst = true;
    public static boolean multipleMegas = false;
    public static boolean msdPatchAutoUpdate = true;

    public static boolean mega = true;
    public static boolean zMoves = true;
    public static boolean terastallization = true;
    public static boolean dynamax = true;
    public static int powerSpotRange = 20;
    public static boolean dynamaxAnywhere = false;
    public static float dynamaxScaleFactor = 4f;

    public static double teraShardDropRate = 10.0;
    public static double stellarShardDropRate = 1.0;
    public static boolean teraHats = true;
    public static boolean legacyTeraEffect = false;

    public static int likoPendentDuration = 72000;

    public static int minBondingRequired = 200;

    // Battle HUD
    public static boolean showBattleHUD = true;
    public static boolean showStatChanges = true;
    public static boolean showMoveInspector = true;

    public static boolean debugMode = false;

    /**
     * Fields that are excluded from (de)serialization even though they're public static.
     */
    private static final Map<String, Boolean> EXCLUDED = Map.of("loaded", true);

    /**
     * Java field name -> JSON key, only listed where they differ (kept for backwards
     * compatibility with existing config.json files).
     */
    private static final Map<String, String> JSON_KEY_OVERRIDES = Map.of(
            "showMoveInspector", "showMoveTooltips"
    );

    private static void save() {
        JsonObject json = new JsonObject();

        for (Field field : configFields()) {
            String key = jsonKeyFor(field);
            try {
                Object value = field.get(null);
                if (value instanceof Boolean) {
                    json.addProperty(key, (Boolean) value);
                } else if (value instanceof Number) {
                    json.addProperty(key, (Number) value);
                }
            } catch (IllegalAccessException e) {
                MegaShowdown.LOGGER.error("Failed to read field {}", field.getName(), e);
            }
        }

        try {
            Files.createDirectories(Path.of("./config/mega_showdown"));
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write(GSON.toJson(json));
            }
        } catch (IOException e) {
            MegaShowdown.LOGGER.error("Failed to save MegaShowdown config:", e);
        }
    }

    public static void load() {
        // Default only: "maxed friendship" as Cobblemon defines it. The config.json read below
        // overrides this whenever the key is present, so the user's value always wins.
        megaFriendshipRequirement = Cobblemon.INSTANCE.getConfig().getMaxPokemonFriendship();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            MegaShowdown.LOGGER.info("MegaShowdown config not found, creating default.");
            save();
            return;
        }

        boolean missingField = false;

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            for (Field field : configFields()) {
                String key = jsonKeyFor(field);
                if (!json.has(key)) {
                    // Field doesn't exist in the file yet (e.g. added in a newer version).
                    // The field keeps its default value; we'll persist it below.
                    missingField = true;
                    continue;
                }

                JsonElement el = json.get(key);
                try {
                    // Special case
                    if (field.getName().equals("likoPendentDuration")) {
                        field.setInt(null, el.getAsInt() * 20);
                        continue;
                    }

                    Class<?> type = field.getType();
                    if (type == int.class) {
                        field.setInt(null, el.getAsInt());
                    } else if (type == boolean.class) {
                        field.setBoolean(null, el.getAsBoolean());
                    } else if (type == float.class) {
                        field.setFloat(null, el.getAsFloat());
                    } else if (type == double.class) {
                        field.setDouble(null, el.getAsDouble());
                    }
                } catch (IllegalAccessException e) {
                    MegaShowdown.LOGGER.error("Failed to set field " + field.getName(), e);
                }
            }
        } catch (Exception e) {
            MegaShowdown.LOGGER.error("Failed to load MegaShowdown config:", e);
        }
        loaded = true;

        if (missingField) {
            MegaShowdown.LOGGER.info("MegaShowdown config is missing one or more fields, updating file with defaults.");
            save();
        }
    }

    private static Field[] configFields() {
        return java.util.Arrays.stream(MegaShowdownConfig.class.getDeclaredFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()))
                .filter(f -> !EXCLUDED.containsKey(f.getName()))
                .toArray(Field[]::new);
    }

    private static String jsonKeyFor(Field field) {
        return JSON_KEY_OVERRIDES.getOrDefault(field.getName(), field.getName());
    }
}
