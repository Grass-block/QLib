package org.atcraftmc.qlib.platform;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.util.pipe.StackablePipeObject;

import java.util.function.Function;

public abstract class ForwardingPluginPlatform extends StackablePipeObject<PluginPlatform> implements PluginPlatform {

    @Override
    public void sendMessage(Object pointer, ComponentLike message) {
        this.parent.sendMessage(pointer, message);
    }

    @Override
    public MinecraftLocale locale(Object sender) {
        return this.parent.locale(sender);
    }

    @Override
    public void broadcastLine(Function<MinecraftLocale, ComponentLike> component, boolean opOnly, boolean toConsole) {
        this.parent.broadcastLine(component, opOnly, toConsole);
    }

    @Override
    public String globalFormatMessage(String source) {
        return this.parent.globalFormatMessage(source);
    }

    @Override
    public String globalFormatMessage(String source, Object player) {
        return this.parent.globalFormatMessage(source, player);
    }

    @Override
    public ComponentLike examineComponent(ComponentLike component, Object pointer, MinecraftLocale locale) {
        return this.parent.examineComponent(component, pointer, locale);
    }

    @Override
    public String pluginsFolder() {
        return this.parent.pluginsFolder();
    }
}
