package org.atcraftmc.qlib.platform;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.util.pipe.Pipeline;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.texts.pipe.TextPipeline;
import org.atcraftmc.qlib.util.pipe.StackedPipeline;

import java.util.function.Function;

public final class PluginPlatformStack extends StackedPipeline<PluginPlatform> implements PluginPlatform {
    private final TextPipeline textPipeline = new TextPipeline();

    public PluginPlatformStack(PluginPlatform base) {
        super(base);
    }

    public TextPipeline getTextPipeline() {
        return textPipeline;
    }

    @Override
    public void sendMessage(Object pointer, ComponentLike message) {
        this.last.sendMessage(pointer, message);
    }

    @Override
    public MinecraftLocale locale(Object sender) {
        return this.last.locale(sender);
    }

    @Override
    public void broadcastLine(Function<MinecraftLocale, ComponentLike> component, boolean opOnly, boolean toConsole) {
        this.last.broadcastLine(component, opOnly, toConsole);
    }

    @Override
    public String globalFormatMessage(String source, Object player) {
        source = this.getTextPipeline().handle(source, player);

        return this.last.globalFormatMessage(source);
    }

    @Override
    public String globalFormatMessage(String source) {
        source = this.getTextPipeline().handle(source, null);

        return this.last.globalFormatMessage(source);
    }

    @Override
    public ComponentLike examineComponent(ComponentLike component, Object pointer, MinecraftLocale locale) {
        return this.last.examineComponent(component, pointer, locale);
    }

    @Override
    public String pluginsFolder() {
        return this.last.pluginsFolder();
    }
}
