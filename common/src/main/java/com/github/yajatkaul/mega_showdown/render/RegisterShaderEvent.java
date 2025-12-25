package com.github.yajatkaul.mega_showdown.render;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

import java.io.IOException;

public interface RegisterShaderEvent {
    /**
     * Fabrics event does not allow for custom shader instances
     */
    Event<RegisterShaderEvent> EVENT = EventFactory.createLoop(RegisterShaderEvent.class);

    void registerShaders(ShaderRegister register) throws IOException;
}