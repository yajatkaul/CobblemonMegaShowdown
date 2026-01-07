package com.github.yajatkaul.mega_showdown.render.layerEntities.states;

import com.cobblemon.mod.common.api.scheduling.SchedulingTracker;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeraCrystalState extends PosableState {
    private final SchedulingTracker schedulingTracker;

    public TeraCrystalState() {
        setPose("idle");
        this.schedulingTracker = new SchedulingTracker();
    }

    @Override
    public @Nullable Entity getEntity() {
        return null;
    }

    @Override
    public void updatePartialTicks(float partialTicks) {
        this.setCurrentPartialTicks(partialTicks);
    }

    public void resetAnimation() {
        this.reset();
        this.setCurrentPartialTicks(0);
    }

    @Override
    public @NotNull SchedulingTracker getSchedulingTracker() {
        return schedulingTracker;
    }
}