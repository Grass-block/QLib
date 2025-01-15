package org.atcraftmc.qlib.language;

import org.atcraftmc.qlib.PluginConcept;
import org.atcraftmc.qlib.PluginPlatform;
import org.atcraftmc.qlib.texts.ComponentBlock;
import org.atcraftmc.qlib.texts.TextBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ILanguageAccess {
    public abstract String getRawMessage(Locale locale, String namespace, String id);

    public abstract List<String> getRawMessageList(Locale locale, String namespace, String id);

    public abstract boolean hasKey(String namespace, String id);

    public String getRawRandomMessage(Locale locale, String namespace, String id) {
        List<String> list = this.getMessageList(locale, namespace, id);
        return list.get(new Random().nextInt(0, list.size()));
    }

    //inline
    public String getInlineMessage(Locale locale, String namespace, String id) {
        String msg = getRawMessage(locale, namespace, id);
        return inline(msg, locale, namespace);
    }

    public String getInlineRandomMessage(Locale locale, String namespace, String id) {
        String msg = getRawRandomMessage(locale, namespace, id);
        return inline(msg, locale, namespace);
    }

    public List<String> getInlineMessageList(Locale locale, String namespace, String id) {
        List<String> msg = getRawMessageList(locale, namespace, id);

        msg.replaceAll(src -> inline(src, locale, namespace));
        return msg;
    }

    private String inline(String source, Locale locale, String namespace) {
        source = match(source, Language.MESSAGE_PATTERN, (src, s) -> {
            var id = s.substring(5, s.length() - 1).split(":");
            var ns = id.length == 1 ? namespace : id[0];
            return src.replace(s, getInlineMessage(locale, ns, id[id.length - 1]));
        });

        source = match(source, Language.RANDOM_MESSAGE_PATTERN, (src, s) -> {
            var id = s.substring(6, s.length() - 1).split(":");
            var ns = id.length == 1 ? namespace : id[0];
            return src.replace(s, getInlineRandomMessage(locale, ns, id[id.length - 1]));
        });

        source = match(source, Language.LOCALIZED_GLOBAL_VAR, (src, s) -> {
            var id = s.substring(8, s.length() - 1);
            var inline = LanguageContainer.getInstance().access(this.getHolder().configId()).getInlineMessage(locale, "global-vars", id);
            return src.replace(s, inline);
        });

        return PluginPlatform.instance().globalFormatMessage(source);
    }

    protected abstract PluginConcept getHolder();

    private String match(String src, Pattern pattern, BiFunction<String, String, String> process) {
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            src = process.apply(src, matcher.group());
        }
        return src;
    }

    //complete
    public String getMessage(Locale locale, String namespace, String id, Object... format) {
        return Language.format(getInlineMessage(locale, namespace, id), format);
    }

    public String getRandomMessage(Locale locale, String namespace, String id, Object... format) {
        return Language.format(getInlineRandomMessage(locale, namespace, id), format);
    }

    public List<String> getMessageList(Locale locale, String namespace, String id) {
        return getInlineMessageList(locale, namespace, id);
    }

    //send
    public void sendMessage(Object sender, String namespace, String id, Object... format) {
        var c = getMessageComponent(Language.locale(sender), namespace, id, format);
        PluginPlatform.instance().sendMessage(sender,c.asComponent());
    }

    public void sendRandomMessage(Object sender, String namespace, String id, Object... format) {
        var block = getRandomMessageComponent(Language.locale(sender), namespace, id, format);
        PluginPlatform.instance().sendMessage(sender, block.asComponent());
    }

    public void broadcastMessage(boolean op, boolean console, String namespace, String id, Object... format) {
        PluginPlatform.instance().broadcastLine((l) -> getMessageComponent(l, namespace, id, format), op, console);
    }

    public void broadcastRandomMessage(boolean op, boolean console, String namespace, String id, Object... format) {
        PluginPlatform.instance().broadcastLine((l) -> getRandomMessageComponent(l, namespace, id, format), op, console);
    }

    //template
    public String buildTemplate(Locale locale, String namespace, String template) {
        return this.inline(template, locale, namespace);
    }

    public void sendTemplate(Object sender, String namespace, String template) {
        PluginPlatform.instance().sendMessage(sender, TextBuilder.build(this.buildTemplate(Language.locale(sender), namespace, template)));
    }

    //component
    public ComponentBlock getMessageComponent(Locale locale, String namespace, String id, Object... format) {
        return TextBuilder.build(getMessage(locale, namespace, id, format));
    }

    public ComponentBlock getRandomMessageComponent(Locale locale, String namespace, String id, Object... format) {
        return TextBuilder.build(getRandomMessage(locale, namespace, id, format));
    }

    //entry
    public LanguageEntry entry(String namespace) {
        return new LanguageEntry(this, namespace);
    }

    public LanguageItem item(String namespace, String id) {
        return new LanguageItem(this, namespace, id);
    }
}
