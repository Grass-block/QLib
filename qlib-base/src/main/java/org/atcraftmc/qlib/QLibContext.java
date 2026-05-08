package org.atcraftmc.qlib;

import org.atcraftmc.qlib.audience.AudienceService;
import org.atcraftmc.qlib.config.ConfigContainer;
import org.atcraftmc.qlib.language.LanguageContainer;
import org.atcraftmc.qlib.platform.PluginPlatform;
import org.atcraftmc.qlib.text.TextEngine;
import org.atcraftmc.qlib.texts.ContextComponentBlockBuilder;

public abstract class QLibContext implements AutoCloseable {
    protected final TextEngine textEngine = new TextEngine();
    protected final ContextComponentBlockBuilder textBuilder = new ContextComponentBlockBuilder(this.textEngine);

    public TextEngine textEngine() {
        return textEngine;
    }

    public final ContextComponentBlockBuilder textBuilder() {
        return textBuilder;
    }

    public abstract AudienceService<?> audiences();

    public abstract LanguageContainer language();

    public abstract ConfigContainer config();

    public abstract PluginConcept plugin();

    public abstract PluginPlatform platform();
}
