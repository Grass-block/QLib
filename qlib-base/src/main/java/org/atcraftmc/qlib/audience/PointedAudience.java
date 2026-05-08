package org.atcraftmc.qlib.audience;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.text.StringComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface PointedAudience extends QLibAudience {
    Dummy DUMMY = new Dummy();

    Audience getWrapped();

    <I> I getPointer(Class<I> type);

    AudienceSource getSource();

    MinecraftLocale locale();

    default void sendMessage(String message){
        sendMessage(new StringComponent(message));
    }

    final class Dummy implements PointedAudience {

        @Override
        public Audience getWrapped() {
            return null;
        }

        @Override
        public <I> I getPointer(Class<I> type) {
            return null;
        }

        @Override
        public AudienceSource getSource() {
            return AudienceSource.OTHER;
        }

        @Override
        public MinecraftLocale locale() {
            return MinecraftLocale.ZH_CN;
        }

        @Override
        public @NotNull Iterable<? extends Audience> audiences() {
            return new ArrayList<>();
        }
    }
}
