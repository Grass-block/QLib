package org.atcraftmc.qlib.text;

public interface MessageComponentProvider {
    MessageComponent message(Object... format);

    default MessageComponent randMessage(Object... format) {
        return message(format);
    }
}
