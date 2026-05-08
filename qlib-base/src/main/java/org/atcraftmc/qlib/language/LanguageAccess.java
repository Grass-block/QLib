package org.atcraftmc.qlib.language;

public final class LanguageAccess {
    private final LanguageContainer handle;
    private final String id;

    public LanguageAccess(LanguageContainer handle, String id) {
        this.handle = handle;
        this.id = id;
    }

    public LanguageItem item(String id) {
        return this.handle.item(this.id + ":" + id);
    }

    public LanguageEntry entry(String id) {
        return this.handle.entry(this.id + ":" + id);
    }

    public LanguageContainer handle() {
        return this.handle;
    }

    public String inline(String message, MinecraftLocale locale) {
        return this.handle.inline(message, locale, this.id);
    }
}