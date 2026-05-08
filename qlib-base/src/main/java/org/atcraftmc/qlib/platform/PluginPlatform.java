package org.atcraftmc.qlib.platform;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.language.MinecraftLocale;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public interface PluginPlatform {
    AtomicReference<PluginPlatformStack> GLOBAL = new AtomicReference<>();

    static PluginPlatformStack global() {
        return GLOBAL.get();
    }

    void sendMessage(Object pointer, ComponentLike message);

    MinecraftLocale locale(Object sender);

    void broadcastLine(Function<MinecraftLocale, ComponentLike> component, boolean opOnly, boolean toConsole);

    default String globalFormatMessage(String source, Object player){
        return source;
    }

    default String globalFormatMessage(String source) {
        return source;
    }

    default ComponentLike examineComponent(ComponentLike component, Object pointer, MinecraftLocale locale) {
        return component;
    }

    String pluginsFolder();
}
