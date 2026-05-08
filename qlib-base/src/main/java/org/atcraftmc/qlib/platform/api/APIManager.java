package org.atcraftmc.qlib.platform.api;

import me.gb2022.commons.reflect.method.MethodHandle;
import me.gb2022.commons.reflect.method.MethodHandleO1;
import me.gb2022.commons.reflect.method.MethodHandleRO0;
import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.language.MinecraftLocale;

import java.util.HashMap;
import java.util.Map;

public class APIManager {
    private final Map<String, APIComponent<?>> components = new HashMap<>();

    protected final APIComponent<MethodHandleO1<Object, ComponentLike>> message = registerAPI("qlib:player:message");
    protected final APIComponent<MethodHandleRO0<Object, MinecraftLocale>> locale = registerAPI("qlib:player:locale");

    public APIManager() {
        init();
    }

    public void init() {
    }

    public <M extends MethodHandle> APIComponent<M> registerAPI(String id) {
        var handle = new APIComponent<M>();
        this.components.put(id, handle);

        return handle;
    }

    public <M extends MethodHandle> APIComponent<M> get(String id) {
        return (APIComponent<M>) this.components.get(id);
    }

    public APIComponent<MethodHandleO1<Object, ComponentLike>> getMessageAPI() {
        return this.message;
    }

    public APIComponent<MethodHandleRO0<Object, MinecraftLocale>> getLocaleAPI() {
        return this.locale;
    }
}