package org.atcraftmc.qlib.velocity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atcraftmc.qlib.PluginConcept;

public abstract class VelocityPluginConcept implements PluginConcept {
    protected final Logger logger = LogManager.getLogger(id());

    @Override
    public Logger logger() {
        return this.logger;
    }

    @Override
    public String folder() {
        return System.getProperty("user.dir") + "/plugins/" + this.id();
    }
}
