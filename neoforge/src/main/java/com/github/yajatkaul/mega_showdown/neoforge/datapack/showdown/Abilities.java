package com.github.yajatkaul.mega_showdown.neoforge.datapack.showdown;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.abilities.AbilityTemplate;
import com.cobblemon.mod.common.api.data.DataRegistry;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.battles.runner.graal.GraalShowdownService;
import com.cobblemon.mod.relocations.graalvm.polyglot.Value;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.utils.datapack.NewAbility;
import kotlin.Unit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Abilities implements DataRegistry {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "showdown/abilities");
    private static final SimpleObservable<Abilities> OBSERVABLE = new SimpleObservable<>();
    public static final Abilities INSTANCE = new Abilities();
    private final Map<String, String> abilityScripts = new HashMap<>();

    private Abilities() {
        OBSERVABLE.subscribe(Priority.NORMAL, this::registerAbilities);
    }

    public void registerAbilities(Abilities ability) {
        Cobblemon.INSTANCE.getShowdownThread().queue(showdownService -> {
            if (showdownService instanceof GraalShowdownService service) {
                Value receiveAbilityDataFn = service.context.getBindings("js").getMember("receiveAbilityData");
                for (Map.Entry<String, String> entry : Abilities.INSTANCE.getAbilityScripts().entrySet()) {
                    String abilityId = entry.getKey();
                    String js = entry.getValue().replace("\n", " ");
                    receiveAbilityDataFn.execute(abilityId, js);
                    AbilityTemplate newAbility = NewAbility.INSTANCE.getAbility(abilityId);

                    com.cobblemon.mod.common.api.abilities.Abilities.register(newAbility);
                }
            }
            return Unit.INSTANCE;
        });
    }

    public Map<String, String> getAbilityScripts() {
        return abilityScripts;
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @NotNull
    @Override
    public PackType getType() {
        return PackType.SERVER_DATA;
    }

    @NotNull
    @Override
    public SimpleObservable<? extends DataRegistry> getObservable() {
        return OBSERVABLE;
    }

    @Override
    public void reload(@NotNull ResourceManager resourceManager) {
        abilityScripts.clear();
        resourceManager.listResources("showdown/abilities", path -> path.getPath().endsWith(".js")).forEach((id, resource) -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.open(), StandardCharsets.UTF_8))) {
                String js = reader.lines().collect(Collectors.joining("\n"));
                String abilityId = new File(id.getPath()).getName().replace(".js", "");
                abilityScripts.put(abilityId, js);
            } catch (IOException e) {
                MegaShowdown.LOGGER.error("Failed to load ability script: {} {}", id, e);
            }
        });

        OBSERVABLE.emit(this);
    }

    @Override
    public void sync(@NotNull ServerPlayer serverPlayer) {
        // no sync needed for showdown injection
    }
}