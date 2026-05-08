package org.atcraftmc.qlib.language.pipe;

import org.atcraftmc.qlib.language.Language;
import org.atcraftmc.qlib.language.MinecraftLocale;

import java.util.regex.Pattern;

@FunctionalInterface
public interface TextProcessor {
    static TextProcessor match(Pattern pattern, ResultHandler handler) {
        return (input, locale, id) -> Language.match(input, pattern, (src, m) -> handler.process(src, locale, id, m));
    }

    String process(String input, MinecraftLocale locale, String id);

    interface ResultHandler {
        String process(String input, MinecraftLocale locale, String id, String matchResult);
    }
}