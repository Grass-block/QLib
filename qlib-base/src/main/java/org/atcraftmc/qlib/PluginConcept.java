package org.atcraftmc.qlib;

import org.apache.logging.log4j.Logger;
import org.atcraftmc.qlib.platform.PluginPlatform;

public interface PluginConcept {
    static PluginConcept wrapSubPack(Object handle, PluginConcept owner) {
        return new SubPackPluginConceptWrapper(handle, owner);
    }

    String id();

    Logger logger();

    String folder();

    String configId();

    default PluginPlatform platform() {
        return PluginPlatform.global();
    }

    default Object handle() {
        return this;
    }

    final class SubPackPluginConceptWrapper implements PluginConcept {
        private final Object handle;
        private final PluginConcept owner;

        private SubPackPluginConceptWrapper(Object handle, PluginConcept owner) {
            this.handle = handle;
            this.owner = owner;
        }

        public static PluginConcept of(Object handle, PluginConcept owner) {
            return new SubPackPluginConceptWrapper(handle, owner);
        }

        @Override
        public String id() {
            return this.owner.id();
        }

        @Override
        public String folder() {
            return this.owner.folder();
        }

        @Override
        public String configId() {
            return this.owner.configId();
        }

        @Override
        public Logger logger() {
            return this.owner.logger();
        }

        @Override
        public Object handle() {
            return this.handle;
        }
    }
}
