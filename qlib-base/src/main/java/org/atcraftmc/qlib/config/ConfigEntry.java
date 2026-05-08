package org.atcraftmc.qlib.config;

public final class ConfigEntry {
    private final ConfigContainer parent;
    private final String namespace;

    public ConfigEntry(ConfigContainer parent, String namespace) {
        this.parent = parent;
        this.namespace = namespace;
    }

    public ConfigurationValue value(String id) {
        return this.parent.value(this.namespace + ":" + id);
    }
}
