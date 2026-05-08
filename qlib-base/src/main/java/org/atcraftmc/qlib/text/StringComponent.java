package org.atcraftmc.qlib.text;

import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.language.Language;

public final class StringComponent implements RenderableComponent {
    private final String text;
    private final Object[] format;

    public StringComponent(String text, Object... format) {
        this.text = text;
        this.format = format;
    }

    @Override
    public String render(PointedAudience audience) {
        return this.text;
    }

    @Override
    public String postRender(PointedAudience audience, String source) {
        return Language.format(source, this.format);
    }
}
