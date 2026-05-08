//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.atcraftmc.qlib.language;

import java.util.List;
import java.util.Locale;
import org.atcraftmc.qlib.PluginConcept;
import org.atcraftmc.qlib.PluginPlatform;

public final class LanguageAccess extends ILanguageAccess {
    private final LanguageContainer parent;
    private final String pack;

    public LanguageAccess(LanguageContainer parent, String pack) {
        this.parent = parent;
        this.pack = pack;
    }

    public String getRawMessage(Locale locale, String namespace, String id) {
        return this.parent.getRawMessage(locale, this.pack, namespace, id);
    }

    public List<String> getRawMessageList(Locale locale, String namespace, String id) {
        return this.parent.getRawMessageList(locale, this.pack, namespace, id);
    }

    public String getRawRandomMessage(Locale locale, String namespace, String id) {
        return this.parent.getRawRandomMessage(locale, this.pack, namespace, id);
    }

    public String getInlineMessage(Locale locale, String namespace, String id) {
        return this.parent.getInlineMessage(locale, this.pack, namespace, id);
    }

    public List<String> getInlineMessageList(Locale locale, String namespace, String id) {
        return this.parent.getInlineMessageList(locale, this.pack, namespace, id);
    }

    protected PluginConcept getHolder() {
        return PluginPlatform.instance().defaultPlugin();
    }

    public String getInlineRandomMessage(Locale locale, String namespace, String id) {
        return this.parent.getInlineRandomMessage(locale, this.pack, namespace, id);
    }

    public String getMessage(Locale locale, String namespace, String id, Object... format) {
        return this.parent.getMessage(locale, this.pack, namespace, id, format);
    }

    public String getRandomMessage(Locale locale, String namespace, String id, Object... format) {
        return this.parent.getRandomMessage(locale, this.pack, namespace, id, format);
    }

    public List<String> getMessageList(Locale locale, String namespace, String id) {
        return this.parent.getMessageList(locale, this.pack, namespace, id);
    }

    public boolean hasKey(String entry, String id) {
        return this.parent.hasAny(this.pack, entry, id);
    }
}
