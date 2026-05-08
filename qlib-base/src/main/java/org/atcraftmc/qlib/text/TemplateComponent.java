package org.atcraftmc.qlib.text;

import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.language.LanguageEntry;

public final class TemplateComponent implements RenderableComponent {
    private final LanguageEntry entry;
    private final String text;

    public TemplateComponent(LanguageEntry entry, String text) {
        this.entry = entry;
        this.text = text;
    }

    @Override
    public String render(PointedAudience audience) {
        var loc = audience.locale();
        var s1 = this.entry.inline(this.text, loc);
        return this.entry.inline(s1, loc);
    }
}
