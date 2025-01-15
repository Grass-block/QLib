package org.atcraftmc.qlib.texts.placeholder;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.texts.TextBuilder;

public interface StringPlaceHolder extends GlobalPlaceHolder {
    String getText();

    default ComponentLike get() {
        return TextBuilder.build(getText());
    }
}
