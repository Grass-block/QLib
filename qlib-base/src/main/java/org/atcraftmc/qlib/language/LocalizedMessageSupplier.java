package org.atcraftmc.qlib.language;

public interface LocalizedMessageSupplier {
    default String message0(MinecraftLocale locale, Object... fmtObjects) {
        return message(locale, fmtObjects).render();
    }

    RenderedMessage message(MinecraftLocale locale, Object... fmtObjects);
}
