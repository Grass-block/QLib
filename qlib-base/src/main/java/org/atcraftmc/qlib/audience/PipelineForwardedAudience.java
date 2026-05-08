package org.atcraftmc.qlib.audience;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.jetbrains.annotations.NotNull;

public final class PipelineForwardedAudience extends WrappedAudience {
    private final AudienceService<?> holder;

    public PipelineForwardedAudience(AudienceService<?> holder, Audience wrapped, Object pointer, AudienceSource source) {
        super(wrapped, pointer, source);
        this.holder = holder;
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        this.holder.getTextEngine().sendChatMessage(this, message);
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
        this.holder.getTextEngine().sendChatMessage(this, message);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        this.holder.getTextEngine().sendActionBarMessage(this, message);
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        this.holder.getTextEngine().sendActionBarMessage(this, message);
    }

    @Override
    public MinecraftLocale locale() {
        return this.holder.getTextEngine().getLocale(this);
    }

    @Override
    public String toString() {
        return "PipelineForwardedAudience{" + "holder=" + holder +
                ", wrapped=" + wrapped +
                ", pointer=" + pointer +
                ", source=" + source +
                '}';
    }
}
