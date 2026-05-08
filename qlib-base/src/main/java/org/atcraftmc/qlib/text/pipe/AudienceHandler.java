package org.atcraftmc.qlib.text.pipe;

import net.kyori.adventure.text.Component;
import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.util.pipe.StackablePipeObject;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface AudienceHandler {
    interface MessageProcessor {
        Component process(PointedAudience audience, Component message);
    }

    interface MessageRenderer {
        String process(PointedAudience audience, String message);
    }
}
