package org.atcraftmc.qlib.language;

import org.atcraftmc.qlib.util.Formatter;
import org.atcraftmc.qlib.util.Identifiers;
import org.atcraftmc.qlib.config.ConfigEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface Language {
    AtomicBoolean USE_LEGACY_FORMATTING = new AtomicBoolean(false);
    Pattern MESSAGE_PATTERN = Pattern.compile("\\{msg#(.*?)}");
    Pattern RANDOM_MESSAGE_PATTERN = Pattern.compile("\\{rand#(.*?)}");
    Pattern LOCALIZED_GLOBAL_VAR = Pattern.compile("\\{global#(.*?)}");

    static MinecraftLocale locale(String id) {
        return MinecraftLocale.minecraft(id);
    }

    static String locale(Locale locale) {
        return LocaleMapping.minecraft(locale);
    }


    @SafeVarargs
    static String generateTemplate(ConfigurationSection section, String id, Function<String, String>... preprocessors) {
        id = Identifiers.external(id);
        if (section.isString(id)) {
            return section.getString(id);
        }

        List<String> list = section.getStringList(id);
        return buildList(list, preprocessors);
    }

    @SafeVarargs
    static String generateTemplate(ConfigEntry entry, String id, Function<String, String>... preprocessors) {
        var value = entry.value(Identifiers.external(id));

        if (value.isType(String.class)) {
            var result = value.string();
            for (Function<String, String> preprocessor : preprocessors) {
                result = preprocessor.apply(result);
            }
            return result;
        }

        return buildList(value.list(String.class), preprocessors);
    }

    static String buildList(List<String> list, Function<String, String>[] preprocessors) {
        String result = list2string(list);
        for (Function<String, String> preprocessor : preprocessors) {
            result = preprocessor.apply(result);
        }

        return result;
    }

    static String format(String s, Object... format) {
        for (var i = 0; i < format.length; i++) {
            if (format[i] == null) {
                format[i] = "";
            }
        }

        if (USE_LEGACY_FORMATTING.get()) {
            return s.formatted(format);
        }

        for (Object o : format) {
            try {
                s = s.replaceFirst("\\{}", Formatter.format(o));
            } catch (Exception ignored) {//reserve not replaced value.
            }
        }
        for (int i = 0; i < format.length; i++) {
            s = s.replace("{" + i + "}", format[i].toString());
        }
        return s;
    }

    static String list2string(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String ss : list) {
            i++;
            sb.append(ss);
            if (i < list.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    static String key(String pack, String entry, String id) {
        return "%s:%s:%s".formatted(Identifiers.external(pack), Identifiers.external(entry), Identifiers.external(id));
    }

    static String match(String src, Pattern pattern, BiFunction<String, String, String> process) {
        var matcher = pattern.matcher(src);
        while (matcher.find()) {
            src = process.apply(src, matcher.group());
        }
        return src;
    }
}
