package com.github.yajatkaul.mega_showdown.fabric.shader;

import com.github.yajatkaul.mega_showdown.render.RegisterShaderEvent;
import com.github.yajatkaul.mega_showdown.render.ShaderRegister;

import java.util.function.Consumer;

public class ShaderRegisterFactory implements ShaderRegister.Factory {
    @Override
    public void register(String modid, Consumer<ShaderRegister> consumer) {
        RegisterShaderEvent.EVENT.register(consumer::accept);
    }
}