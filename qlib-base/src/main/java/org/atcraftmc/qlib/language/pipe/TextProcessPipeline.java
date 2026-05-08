package org.atcraftmc.qlib.language.pipe;

import org.atcraftmc.qlib.util.pipe.Pipeline;
import org.atcraftmc.qlib.language.MinecraftLocale;

public final class TextProcessPipeline extends Pipeline<TextProcessor> implements TextProcessor {

    @Override
    public String process(String text, MinecraftLocale locale, String namespace) {
        for (var id : this.orderList) {
            var processor = this.processors.get(id);

            try {
                text = processor.process(text, locale, namespace);
            } catch (Exception e) {
                throw new RuntimeException("Error on processing " + id + ", \norigin text: " + text, e);
            }
        }

        return text;
    }
}