package org.atcraftmc.qlib.language;

import org.atcraftmc.qlib.PluginConcept;
import org.atcraftmc.qlib.QLibContext;
import org.atcraftmc.qlib.config.PackContainer;
import org.atcraftmc.qlib.language.pipe.TextProcessPipeline;
import org.atcraftmc.qlib.language.pipe.TextProcessor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//todo: auto-detective yml2 tree(non only 3 layer arch)
public final class LanguageContainer extends PackContainer<LanguagePack> {
    private final Map<String, Map<String, Object>> items = new ConcurrentHashMap<>();
    private final QLibContext context;
    private final TextProcessPipeline pipeline = new TextProcessPipeline();
    private final PluginConcept holder;
    private final String preferredId;

    public LanguageContainer(QLibContext context, PluginConcept holder, String preferredId) {
        this.context = context;
        this.holder = holder;
        this.preferredId = preferredId;

        this.pipeline.addLast("qlib:lang-global", TextProcessor.match(Language.LOCALIZED_GLOBAL_VAR, (input, locale, cid, res) -> {
            var id = res.substring(8, res.length() - 1);
            var inline = item(this.preferredId + ":" + "global-vars:" + id).inline(locale);
            return input.replace(res, inline);
        }));
        this.pipeline.addLast("qlib:lang-simple", TextProcessor.match(Language.MESSAGE_PATTERN, (input, locale, cid, res) -> {
            var k = getInlineKey(res.substring(5, res.length() - 1), cid);
            return input.replace(res, item(k).inline(locale));
        }));
        this.pipeline.addLast("qlib:lang-random", TextProcessor.match(Language.RANDOM_MESSAGE_PATTERN, (input, locale, cid, res) -> {
            var k = getInlineKey(res.substring(6, res.length() - 1), cid);
            return input.replace(res, item(k).inlineRandom(locale));
        }));
    }

    public static String getInlineKey(String key, String cid) {
        var keys = key(key).split(":");

        if (keys.length == 1) {
            return cid + ":" + key;
        }

        //warn: now if we have multiple splits than we use only first id as start.
        //works in 3-layer but not further arch.
        if (keys.length == 2) {
            return cid.substring(cid.indexOf(":")) + ":" + key;
        }

        return key;
    }

    public static String key(String unsafe) {
        return unsafe.replace(".", ":");
    }

    //----[raw]----
    public static String error(Locale locale, String pack, String entry, String id) {
        return Language.key(pack, entry, id) + "@" + LocaleMapping.minecraft(locale);
    }

    public Map<String, Object> getItemEntry(String key) {
        if (!key.matches("[a-z0-9-]+:[a-z0-9-]+:[a-z0-9-]+")) {
            throw new IllegalArgumentException("Invalid key: " + key);
        }

        if (!this.items.containsKey(key)) {
            this.items.put(key, new HashMap<>());
        }

        return this.items.get(key);
    }

    public Map<String, Map<String, Object>> getItems() {
        return items;
    }

    //----[Packs]----
    public void inject(LanguagePack pack) {
        for (String k : pack.getKeys()) {
            String key = pack.getId() + ":" + k;

            getItemEntry(key).put(pack.getLocale(), pack.getObject(k));
        }
    }

    public void refresh(boolean clean) {
        if (clean) {
            this.items.clear();
        }
        super.refresh(clean);
    }


    public PluginConcept holder() {
        return this.holder;
    }

    public LanguageAccess access(String key) {
        return new LanguageAccess(this, key(key));
    }

    public LanguageEntry entry(String key) {
        return new LanguageEntry(this, key(key));
    }

    public LanguageItem item(String key) {
        return new LanguageItem(this, key(key));
    }

    public String inline(String source, MinecraftLocale locale, String currentKey) {
        return this.pipeline.process(source, locale, currentKey);
    }

    public Object getObject(MinecraftLocale locale, String id) {
        Map<String, Object> item = getItemEntry(id);
        String loc = LocaleMapping.remap(locale.minecraft(), item::containsKey);

        return item.get(loc);
    }

    public LanguageEntry entry(String pack, String entry) {
        return entry(String.join(":", pack, entry));
    }

    public LanguageItem item(String pack, String entry, String id) {
        return item(String.join(":", pack, entry, id));
    }

    public Object getObject(Locale locale, String pack, String entry, String id) {
        Map<String, Object> item = getItemEntry(Language.key(pack, entry, id));
        String loc = LocaleMapping.remap(LocaleMapping.minecraft(locale), item::containsKey);

        return item.get(loc);
    }

    //----[check]----
    public boolean hasAny(String pack, String entry, String id) {
        return !getItemEntry(Language.key(pack, entry, id)).isEmpty();
    }

    public boolean hasAny(String id) {
        return !getItemEntry(id).isEmpty();
    }

    public boolean has(Locale locale, String pack, String entry, String id) {
        if (!this.hasAny(pack, entry, id)) {
            return false;
        }
        return getItemEntry(Language.key(pack, entry, id)).containsKey(LocaleMapping.minecraft(locale));
    }

    public boolean has(MinecraftLocale locale, String id) {
        if (!this.hasAny(id)) {
            return false;
        }
        return getItemEntry(id).containsKey(locale.minecraft());
    }

    public QLibContext getContext() {
        return this.context;
    }
}


    /*
    public String getRawMessage(Locale locale, String pack, String entry, String id) {
        return getItem(String.join(":", pack, entry, id)).raw(MinecraftLocale.locale(locale));
    }

    public List<String> getRawMessageList(Locale locale, String pack, String entry, String id) {
        return getItem(String.join(":", pack, entry, id)).rawList(MinecraftLocale.locale(locale));
    }

    public String getRawRandomMessage(Locale locale, String pack, String entry, String id) {
        return getItem(String.join(":", pack, entry, id)).rawRandom(MinecraftLocale.locale(locale));
    }

    //----[inline]----
    public String getInlineMessage(Locale locale, String pack, String entry, String id) {
        return getItem(String.join(":", pack, entry, id)).inline(MinecraftLocale.locale(locale));
    }

    public String getInlineRandomMessage(Locale locale, String pack, String entry, String id) {
        return getItem(String.join(":", pack, entry, id)).inlineRandom(MinecraftLocale.locale(locale));
    }

    public List<String> getInlineMessageList(Locale locale, String pack, String entry, String id) {
        return getItem(String.join(":", pack, entry, id)).inlineList(MinecraftLocale.locale(locale));
    }

        //----[complete]----
    public String getMessage(Locale locale, String pack, String entry, String id, Object... format) {
        return Language.format(getInlineMessage(locale, pack, entry, id), format);
    }

    public String getRandomMessage(Locale locale, String pack, String entry, String id, Object... format) {
        return Language.format(getInlineRandomMessage(locale, pack, entry, id), format);
    }

    public List<String> getMessageList(Locale locale, String pack, String entry, String id) {
        return getInlineMessageList(locale, pack, entry, id);
    }

    public String inline(String source, Locale locale, String pack, String entry) {
        source = match(source, Language.MESSAGE_PATTERN, (src, s) -> {
            var key = inlineKey(s.substring(5, s.length() - 1), pack, entry).split(":");
            return src.replace(s, getInlineMessage(locale, key[0], key[1], key[2]));
        });

        source = match(source, Language.RANDOM_MESSAGE_PATTERN, (src, s) -> {
            var key = inlineKey(s.substring(6, s.length() - 1), pack, entry).split(":");
            return src.replace(s, getInlineRandomMessage(locale, key[0], key[1], key[2]));
        });

        source = match(source, Language.LOCALIZED_GLOBAL_VAR, (src, s) -> {
            var id = s.substring(8, s.length() - 1);
            var inline = getInlineMessage(locale, this.preferredId, "global-vars", id);
            return src.replace(s, inline);
        });

        return PluginPlatform.instance().globalFormatMessage(source);
    }

    private String inlineKey(String k, String pack, String entry) {
        return switch (k.split(":").length) {
            case 1 -> pack + ":" + entry + ":" + k;
            case 2 -> pack + ":" + k;
            default -> k;
        };
    }
     */
