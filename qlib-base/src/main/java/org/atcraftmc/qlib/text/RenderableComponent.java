package org.atcraftmc.qlib.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.audience.PointedAudience;
import org.jetbrains.annotations.NotNull;

public interface RenderableComponent extends ComponentLike {

    @Override
    @NotNull
    default Component asComponent() {
        throw new IllegalStateException("RenderableComponent NEED TO BE RENDERED!");
    }

    default String postRender(PointedAudience audience, String source){
        return source;
    }

    String render(PointedAudience audience);
}