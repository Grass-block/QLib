package org.atcraftmc.qlib.language;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.QLibContext;
import org.atcraftmc.qlib.audience.QLibAudience;
import org.atcraftmc.qlib.text.MessageComponent;
import org.atcraftmc.qlib.text.MessageComponentProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LanguageItem implements LocalizedMessageSupplier, MessageComponentProvider {
    public static final Random TEXT_RANDOM = new Random();
    final QLibContext context;
    private final LanguageContainer handle;
    private final String id;

    public LanguageItem(LanguageContainer handle, String id) {
        this.handle = handle;
        this.id = id;
        this.context = handle.getContext();
    }

    public static LanguageItem dummy(LanguageContainer handle, String id, String message) {
        return new Dummy(handle, id, message);
    }


    public String id() {
        return id;
    }

    public LanguageContainer handle() {
        return handle;
    }


    //--[raw]--
    public String raw(MinecraftLocale locale) {
        var object = this.handle.getObject(locale, this.id);

        if (object == null) {
            return this.id + '@' + locale.minecraft();
        }

        if (object instanceof String s) {
            return s;
        }

        return Language.list2string(((List<String>) object));
    }

    public List<String> rawList(MinecraftLocale locale) {
        var object = this.handle.getObject(locale, this.id);

        if (object == null) {
            return Collections.singletonList(this.id + '@' + locale.minecraft());
        }

        if (object instanceof String s) {
            return Collections.singletonList(s);
        }

        var list = (List<?>) object;

        if (list.get(0) instanceof String) {
            return ((List<String>) list);
        }

        var l = ((List<List<String>>) list);
        var lst = new ArrayList<String>(list.size());

        for (var item : l) {
            lst.add(Language.list2string(item));
        }
        return lst;
    }

    public String rawRandom(MinecraftLocale locale, Random random) {
        var list = this.rawList(locale);
        return list.get(random.nextInt(list.size()));
    }

    public String rawRandom(MinecraftLocale locale) {
        return this.rawRandom(locale, TEXT_RANDOM);
    }


    //--[inline]--
    public String inline(MinecraftLocale locale) {
        return this.handle.inline(raw(locale), locale, this.id.substring(0, this.id.lastIndexOf(':')));
    }

    public String inlineRandom(MinecraftLocale locale, Random random) {
        return this.handle.inline(rawRandom(locale, random), locale, this.id.substring(0, this.id.lastIndexOf(':')));
    }

    public List<String> inlineList(MinecraftLocale locale) {
        var list = new ArrayList<>(this.rawList(locale));
        list.replaceAll(source -> this.handle.inline(source, locale, this.id.substring(0, this.id.lastIndexOf(':'))));

        return list;
    }

    public String inlineRandom(MinecraftLocale locale) {
        return this.inlineRandom(locale, TEXT_RANDOM);
    }


    //--[message]--
    private String _format_(String message, Object player, Object... format) {
        var platform = this.handle().holder().platform();
        return platform.globalFormatMessage(Language.format(message, format), player).formatted(format);
    }

    public RenderedMessage message(MinecraftLocale locale, Object... format) {
        return new RenderedMessage(this.context, locale, inline(locale), format);
    }

    public List<RenderedMessage> list(MinecraftLocale locale) {
        return inlineList(locale).stream().map((s) -> new RenderedMessage(this.context, locale, s)).toList();
    }

    public RenderedMessage random(MinecraftLocale locale, Random random, Object... format) {
        return new RenderedMessage(this.context, locale, inlineRandom(locale, random), format);
    }

    public RenderedMessage random(MinecraftLocale locale, Object... format) {
        return this.random(locale, TEXT_RANDOM, format);
    }

    public ComponentLike component(MinecraftLocale locale, Object... format) {
        return message(locale, format).renderComponent();
    }

    public ComponentLike randComponent(MinecraftLocale locale, Random random, Object... format) {
        return random(locale, random, format).renderComponent();
    }

    public ComponentLike randComponent(MinecraftLocale locale, Object... format) {
        return randComponent(locale, TEXT_RANDOM, format);
    }

    public List<ComponentLike> listComponent(MinecraftLocale locale) {
        return list(locale).stream().map(RenderedMessage::renderComponent).toList();
    }

    //--[v3]--
    public void send(QLibAudience target, Object... format) {
        target.sendMessage(message(format));
    }

    public void sendRandom(QLibAudience target, Object... format) {
        target.sendMessage(randMessage(format));
    }

    @Override
    public MessageComponent message(Object... format) {
        return new MessageComponent(this, false, format);
    }

    @Override
    public MessageComponent randMessage(Object... format) {
        return new MessageComponent(this, true, format);
    }


    private static final class Dummy extends LanguageItem {
        private final RenderedMessage msg;

        public Dummy(LanguageContainer handle, String id, String msg) {
            super(handle, id);
            this.msg = new RenderedMessage(this.context, MinecraftLocale.EN_US, msg);
        }


        @Override
        public String raw(MinecraftLocale locale) {
            return this.msg.getRawMessage();
        }

        @Override
        public List<String> rawList(MinecraftLocale locale) {
            return Collections.singletonList(this.msg.getRawMessage());
        }

        @Override
        public String rawRandom(MinecraftLocale locale, Random random) {
            return this.msg.getRawMessage();
        }

        @Override
        public String inline(MinecraftLocale locale) {
            return this.msg.getRawMessage();
        }

        @Override
        public String inlineRandom(MinecraftLocale locale, Random random) {
            return this.msg.getRawMessage();
        }

        @Override
        public List<String> inlineList(MinecraftLocale locale) {
            return Collections.singletonList(this.msg.render());
        }

        @Override
        public RenderedMessage message(MinecraftLocale locale, Object... format) {
            return this.msg;
        }

        @Override
        public List<RenderedMessage> list(MinecraftLocale locale) {
            return Collections.singletonList(this.msg);
        }

        @Override
        public RenderedMessage random(MinecraftLocale locale, Random random, Object... format) {
            return this.msg;
        }
    }
}
