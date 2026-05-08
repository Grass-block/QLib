package org.atcraftmc.qlib.language;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.QLibContext;
import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.text.StringComponent;

public final class RenderedMessage {
    private final QLibContext context;
    private final MinecraftLocale locale;
    private final String message;
    private final Object[] args;

    public RenderedMessage(QLibContext context, MinecraftLocale locale, String message, Object... args) {
        this.context = context;
        this.locale = locale;
        this.message = message;
        this.args = args;
    }

    public String format() {
        return Language.format(this.message, this.args);
    }

    public String render() {
        return render(PointedAudience.DUMMY);
    }

    public String render(PointedAudience viewer) {
        return this.context.textEngine().renderString(this.message, viewer, this.args);
    }

    public ComponentLike renderComponent() {
        return this.renderComponent(PointedAudience.DUMMY);
    }

    public ComponentLike renderComponent(PointedAudience viewer) {
        return this.context.textEngine().render(viewer, new StringComponent(this.message, this.args));
    }

    public MinecraftLocale getLocale() {
        return locale;
    }

    public String getRawMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }
}
