package org.atcraftmc.qlib.texts;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.text.TextEngine;

import java.util.ArrayList;
import java.util.List;

public final class ContextComponentBlockBuilder {
    private final TextEngine textEngine;

    public ContextComponentBlockBuilder(TextEngine textEngine) {
        this.textEngine = textEngine;
    }

    public ComponentBlock build(String s, Component... format) {
        return TextBuilder.build(this.textEngine.renderString(s), format);
    }

    public ComponentBlock build(String s, boolean checkURLFully, Component... format) {
        return TextBuilder.build(this.textEngine.renderString(s), checkURLFully, format);
    }

    public Component buildComponent(String s, Component... format) {
        return build(s, format).toSingleLine();
    }

    public Component buildComponent(String s, boolean checkURLFully, Component... format) {
        return build(s, checkURLFully, format).toSingleLine();
    }

    public String buildString(String s, Component... format) {
        return LegacyComponentSerializer.legacySection().serialize(buildComponent(s, format));
    }

    public List<String> buildStringBlocks(String s, Component... format) {
        List<String> list = new ArrayList<>();
        for (Component c : build(s, format)) {
            list.add(LegacyComponentSerializer.legacySection().serialize(c));
        }
        return list;
    }
}
