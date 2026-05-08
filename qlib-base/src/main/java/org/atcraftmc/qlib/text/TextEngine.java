package org.atcraftmc.qlib.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.language.Language;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.text.pipe.AudienceHandler;
import org.atcraftmc.qlib.text.pipe.LocaleHandler;
import org.atcraftmc.qlib.text.pipe.MessageHandler;
import org.atcraftmc.qlib.texts.TextBuilder;
import org.atcraftmc.qlib.util.pipe.CachedPipeline;
import org.atcraftmc.qlib.util.pipe.StackedPipeline;

public final class TextEngine {
    private final CachedPipeline<AudienceHandler.MessageRenderer> messageRenderPipeline = new CachedPipeline<>();
    private final CachedPipeline<AudienceHandler.MessageRenderer> messagePreRenderPipeline = new CachedPipeline<>();
    private final CachedPipeline<AudienceHandler.MessageProcessor> messageProcessPipeline = new CachedPipeline<>();

    private final StackedPipeline<MessageHandler> messagePipeline = new StackedPipeline<>(MessageHandler.fallback());
    private final StackedPipeline<MessageHandler> actionBarPipeline = new StackedPipeline<>(MessageHandler.fallbackActionbar());
    private final StackedPipeline<LocaleHandler> localePipeline = new StackedPipeline<>(LocaleHandler.fallback());

    public void sendChatMessage(PointedAudience audience, ComponentLike message) {
        this.messagePipeline.getLast().handle(audience, this.render(audience, message));
    }

    public void sendActionBarMessage(PointedAudience audience, ComponentLike message) {
        this.actionBarPipeline.getLast().handle(audience, this.render(audience, message));
    }

    public MinecraftLocale getLocale(PointedAudience audience) {
        return this.localePipeline.getLast().get(audience);
    }

    public Component render(PointedAudience audience, ComponentLike component) {
        if (component instanceof Component cc) {
            return processComponent(audience, cc);
        }

        if (component instanceof RenderableComponent rc) {
            return renderSSRComponent(audience, rc);
        }

        return processComponent(audience, component.asComponent());
    }

    public Component processComponent(PointedAudience audience, Component component) {
        for (var h : this.messageProcessPipeline.list()) {
            component = h.process(audience, component);
        }

        return component;
    }

    public Component renderSSRComponent(PointedAudience audience, RenderableComponent rc) {
        var message = rc.render(audience);

        for (var h : this.messagePreRenderPipeline.list()) {
            message = h.process(audience, message);
        }

        message = rc.postRender(audience, message);

        for (var h : this.messageRenderPipeline.list()) {
            message = h.process(audience, message);
        }

        var component = TextBuilder.buildComponent(message);

        return processComponent(audience, component);
    }

    public String renderString(String message, PointedAudience audience, Object... format) {
        for (var h : this.messagePreRenderPipeline.list()) {
            message = h.process(audience, message);
        }

        message = Language.format(message, format);

        for (var h : this.messageRenderPipeline.list()) {
            message = h.process(audience, message);
        }

        return message;
    }

    public String renderString(String message, Object... format) {
        return renderString(message, PointedAudience.DUMMY, format);
    }

    public StackedPipeline<MessageHandler> getMessagePipeline() {
        return messagePipeline;
    }

    public StackedPipeline<MessageHandler> getActionBarPipeline() {
        return actionBarPipeline;
    }

    public StackedPipeline<LocaleHandler> getLocalePipeline() {
        return localePipeline;
    }

    public CachedPipeline<AudienceHandler.MessageProcessor> getMessageProcessPipeline() {
        return messageProcessPipeline;
    }

    public CachedPipeline<AudienceHandler.MessageRenderer> getMessageRenderPipeline() {
        return messageRenderPipeline;
    }

    public CachedPipeline<AudienceHandler.MessageRenderer> getMessagePreRenderPipeline() {
        return messagePreRenderPipeline;
    }
}
