package org.atcraftmc.qlib.language;

import org.atcraftmc.qlib.PluginConcept;

public class StandaloneLanguagePack extends LanguagePack {
    public StandaloneLanguagePack(String locale, PluginConcept provider) {
        super("--global", locale, provider);
    }

    @Override
    public String getTemplateResource() {
        return "/lang/%s.yml".formatted(this.getLocale());
    }

    @Override
    public String getStorageFile() {
        return "/lang/%s.yml".formatted(this.getLocale());
    }

    @Override
    public String getTemplateFile() {
        return "/lang/template/%s.yml".formatted(this.getLocale());
    }
}
