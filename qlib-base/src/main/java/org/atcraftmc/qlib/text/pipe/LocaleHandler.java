package org.atcraftmc.qlib.text.pipe;

import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.util.pipe.StackablePipeObject;

import java.util.Locale;

public abstract class LocaleHandler extends StackablePipeObject<LocaleHandler> {
    private static final LocaleHandler FALLBACK = new LocaleHandler() {
        @Override
        public MinecraftLocale get(PointedAudience audience) {
            return MinecraftLocale.locale(Locale.getDefault());
        }
    };

    public static LocaleHandler fallback() {
        return FALLBACK;
    }

    public abstract MinecraftLocale get(PointedAudience audience);

    public static LocaleHandler wrap(ILocaleHandler handler) {
        return new LocaleHandler() {
            @Override
            public MinecraftLocale get(PointedAudience audience) {
                return handler.get(audience,this.parent);
            }
        };
    }

    public interface ILocaleHandler {
        MinecraftLocale get(PointedAudience audience, LocaleHandler ctx);
    }
}
