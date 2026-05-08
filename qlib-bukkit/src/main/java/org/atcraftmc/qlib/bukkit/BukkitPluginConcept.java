package org.atcraftmc.qlib.bukkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atcraftmc.qlib.PluginConcept;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitPluginConcept extends JavaPlugin implements PluginConcept {
    private final Logger logger = LogManager.getLogger(getLogger().getName());

    @Override
    public String id() {
        return getName();
    }

    @Override
    public Logger logger() {
        return this.logger;
    }

    @Override
    public String folder() {
        return getDataFolder().getAbsolutePath();
    }

    @Override
    public String configId() {
        return getName();
    }
}
