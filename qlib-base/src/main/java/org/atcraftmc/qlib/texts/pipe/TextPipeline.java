package org.atcraftmc.qlib.texts.pipe;

import org.atcraftmc.qlib.util.pipe.Pipeline;

public final class TextPipeline extends Pipeline<TextPipelineProcessor> {

    public String handle(String input, Object player) {
        for (var p : list()) {
            input = p.process(input, player);
        }

        return input;
    }
}
