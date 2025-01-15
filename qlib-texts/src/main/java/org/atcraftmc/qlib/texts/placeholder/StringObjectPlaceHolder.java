package org.atcraftmc.qlib.texts.placeholder;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.texts.TextBuilder;

public interface StringObjectPlaceHolder<I> extends ObjectPlaceHolder<I> {
    String getText(I target);

    default ComponentLike get(I target) {
        return TextBuilder.build(getText(target));
    }
}
