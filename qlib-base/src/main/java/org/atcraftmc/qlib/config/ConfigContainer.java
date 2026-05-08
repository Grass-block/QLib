package org.atcraftmc.qlib.config;

import org.atcraftmc.qlib.util.Identifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConfigContainer extends PackContainer<Configuration> {
    public static final ConfigContainer INSTANCE = new ConfigContainer();
    private final Map<String, ConfigurationValue> references = new ConcurrentHashMap<>();
    private final Map<String, Object> items = new ConcurrentHashMap<>();

    public static ConfigContainer getInstance() {
        return INSTANCE;
    }

    public static String key(String pack, String entry, String id) {
        return "%s:%s:%s".formatted(Identifiers.external(pack), Identifiers.external(entry), Identifiers.external(id));
    }

    public Map<String, Object> getItems() {
        return items;
    }


    //----[Packs]----
    public void inject(Configuration pack) {
        for (var namespace : pack.getNamespaces()) {
            if (!pack.getRootSection().isConfigurationSection(namespace)) {
                continue;
            }

            var section = pack.getNamespace(namespace);

            for (var key : section.getKeys(false)) {
                var location = key(pack.getId(), namespace, key);

                if (section.isString(key)) {
                    this.items.put(location, section.getString(key));
                }
                if (section.isList(key)) {
                    this.items.put(location, section.getList(key));
                }
                this.items.put(location, section.get(key));
            }
        }

        for (var r : this.references.keySet()) {
            this.references.get(r).updateValue(this.items.get(r));
        }
    }

    public void refresh(boolean clean) {
        if (clean) {
            this.items.clear();
        }
        super.refresh(clean);
    }

    //----[Access]----
    public ConfigAccess access(String pack) {
        return new ConfigAccess(this, pack);
    }

    public ConfigEntry entry(String pack, String entry) {
        return new ConfigEntry(this,pack + ":"+entry);
    }

    public ConfigurationValue value(String id) {
        return this.references.computeIfAbsent(id, (k) -> new ConfigurationValue().updateValue(this.items.get(id)));
    }
}
