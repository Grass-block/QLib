package org.atcraftmc.qlib.audience;

import net.kyori.adventure.audience.Audience;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class WrappedAudience implements PointedAudience {
    protected final Audience wrapped;
    protected final List<Audience> container;
    protected final Object pointer;
    protected final AudienceSource source;

    public WrappedAudience(Audience wrapped, Object pointer, AudienceSource source) {
        this.wrapped = unwrap(wrapped);
        this.container = List.of(wrapped);
        this.pointer = pointer;
        this.source = source;
    }

    public static Audience unwrap(Audience audience) {
        if (!(audience instanceof WrappedAudience wa)) {
            return audience;
        }

        return unwrap(wa.wrapped);
    }

    @Override
    public final @NotNull Iterable<? extends Audience> audiences() {
        return container;
    }

    @Override
    public final Audience getWrapped() {
        return wrapped;
    }

    @Override
    public final <I> I getPointer(Class<I> type) {
        return type.cast(pointer);
    }

    @Override
    public final AudienceSource getSource() {
        return source;
    }

    @Override
    public MinecraftLocale locale() {
        return MinecraftLocale.locale(Locale.getDefault());
    }

    @Override
    public final int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof PointedAudience wa)) {
            return false;
        }

        return wa.getWrapped().equals(this.wrapped);
    }
}
