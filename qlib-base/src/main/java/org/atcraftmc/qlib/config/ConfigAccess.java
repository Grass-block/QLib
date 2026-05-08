package org.atcraftmc.qlib.config;

public final class ConfigAccess {
    private final ConfigContainer parent;
    private final String pack;

    public ConfigAccess(ConfigContainer parent, String pack) {
        this.parent = parent;
        this.pack = pack;
    }

    public ConfigurationValue value(String entry, String id) {
        return this.parent.value(this.pack + ":" + entry + ":" + id);
    }

    public ConfigEntry entry(String entry) {
        return this.parent.entry(this.pack, entry);
    }
}
