package org.atcraftmc.qlib.config;

import org.atcraftmc.qlib.PluginConcept;

public class StandaloneConfiguration extends Configuration {
    public StandaloneConfiguration(PluginConcept provider) {
        super(provider, "--global");
    }

    @Override
    public String getTemplateResource() {
        return "/config.yml";
    }

    @Override
    public String getStorageFile() {
        return "/config.yml";
    }

    @Override
    public String getTemplateFile() {
        return "/config.yml";
    }
}
