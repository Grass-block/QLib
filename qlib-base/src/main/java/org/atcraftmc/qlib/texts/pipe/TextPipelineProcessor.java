package org.atcraftmc.qlib.texts.pipe;

@FunctionalInterface
public interface TextPipelineProcessor {
    String process(String text, Object player);

    default boolean enforceSecureMode(){
        return false;
    }
}
