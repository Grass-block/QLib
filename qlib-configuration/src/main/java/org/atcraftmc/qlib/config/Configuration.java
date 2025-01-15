package org.atcraftmc.qlib.config;

import org.atcraftmc.qlib.PluginConcept;

public class Configuration extends ConfigurationPack {

    public Configuration(PluginConcept provider, String id) {
        super(id, provider);
        createTemplateFile();
    }

    @Override
    public String getRootSectionName() {
        return "config";
    }

    @Override
    public String getTemplateResource() {
        return "/templates/config/%s.yml".formatted(this.id);
    }

    @Override
    public String getStorageFile() {
        return "/config/%s.yml".formatted(this.id);
    }

    @Override
    public String getTemplateFile() {
        return "/config/template/%s.yml".formatted(this.id);
    }

    @Override
    public String toString() {
        return "Configuration{id=%s loader=%s}".formatted(this.id, this.provider.getClass().getName());
    }
}
