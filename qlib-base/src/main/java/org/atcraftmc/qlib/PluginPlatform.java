package org.atcraftmc.qlib;

import net.kyori.adventure.text.ComponentLike;

import java.util.Locale;
import java.util.function.Function;

public interface PluginPlatform {
    PluginPlatform[] POINTER = new PluginPlatform[]{null};

    static PluginPlatform instance() {
        if (POINTER[0] == null) {
            throw new RuntimeException("Plugin platform not initialized!");
        }

        return POINTER[0];
    }

    static void setPlatform(PluginPlatform platform) {
        POINTER[0] = platform;
    }

    void sendMessage(Object pointer, ComponentLike message);

    Locale locale(Object sender);

    String globalFormatMessage(String source);

    void broadcastLine(Function<Locale, ComponentLike> component, boolean opOnly, boolean toConsole);

    PluginConcept defaultPlugin();

    String pluginsFolder();
}
