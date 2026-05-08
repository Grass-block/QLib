package org.atcraftmc.qlib.text;

import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.language.Language;
import org.atcraftmc.qlib.language.LanguageItem;

import java.util.Arrays;

public final class MessageComponent implements RenderableComponent {
    private final LanguageItem entry;
    private final Object[] format;
    private final boolean random;

    public MessageComponent(LanguageItem entry, boolean random, Object[] format) {
        this.entry = entry;
        this.random = random;
        this.format = format;
    }

    @Override
    public String render(PointedAudience audience) {
        if (this.random) {
            return this.entry.inlineRandom(audience.locale());
        }
        return this.entry.inline(audience.locale());
    }

    @Override
    public String postRender(PointedAudience audience, String source) {
        return Language.format(source,this.format);
    }

    @Override
    public String toString() {
        return "MessageComponent{" + "entry=" + entry +
                ", random=" + random +
                ", format=" + (format == null ? "null" : Arrays.asList(format).toString()) +
                '}';
    }
}
