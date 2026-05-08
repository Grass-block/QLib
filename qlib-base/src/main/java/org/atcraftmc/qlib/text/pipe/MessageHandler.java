package org.atcraftmc.qlib.text.pipe;

import net.kyori.adventure.text.Component;
import org.atcraftmc.qlib.audience.PointedAudience;
import org.atcraftmc.qlib.util.pipe.StackablePipeObject;

public abstract class MessageHandler extends StackablePipeObject<MessageHandler> {
    private static final MessageHandler FALLBACK = new MessageHandler() {
        @Override
        public void handle(PointedAudience audience, Component message) {
            audience.getWrapped().sendMessage(message);
        }
    };

    private static final MessageHandler FALLBACK_ACTIONBAR = new MessageHandler() {
        @Override
        public void handle(PointedAudience audience, Component message) {
            audience.getWrapped().sendActionBar(message);
        }
    };

    public static MessageHandler wrap(IMessageHandler handler) {
        return new MessageHandler() {
            @Override
            public void handle(PointedAudience audience, Component message) {
                handler.handle(audience, message, this.parent);
            }
        };
    }

    public static MessageHandler fallback() {
        return FALLBACK;
    }

    public static MessageHandler fallbackActionbar() {
        return FALLBACK_ACTIONBAR;
    }

    public abstract void handle(PointedAudience audience, Component message);

    public interface IMessageHandler {
        void handle(PointedAudience audience, Component message, MessageHandler ctx);
    }
}
