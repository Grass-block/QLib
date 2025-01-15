package org.atcraftmc.qlib;

import org.apache.logging.log4j.Logger;

public interface PluginConcept {
    String id();

    Logger logger();

    String folder();

    String configId();

    default Object handle(){
        return this;
    }
}
