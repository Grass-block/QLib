package org.atcraftmc.qlib.audience;

import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class PointedForwardingAudience implements QLibAudience {
    private final Set<PointedAudience> audiences = new HashSet<>();

    public PointedForwardingAudience(Collection<PointedAudience> audiences) {
        this.audiences.addAll(audiences);
    }

    public PointedForwardingAudience() {
    }

    @Override
    public @NotNull Iterable<? extends PointedAudience> audiences() {
        return this.audiences;
    }

    public Set<PointedAudience> getAudiences() {
        return audiences;
    }

    public void add(PointedAudience audience) {
        this.audiences.add(audience);
    }

    public void remove(PointedAudience audience) {
        this.audiences.remove(audience);
    }

    public boolean contains(PointedAudience audience) {
        return this.audiences.contains(audience);
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
        for (var pointedAudience : this.audiences()) {
            pointedAudience.sendMessage(message);
        }
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        for (var pointedAudience : this.audiences()) {
            pointedAudience.sendActionBar(message);
        }
    }
}
