package org.atcraftmc.qlib.platform.api;

import me.gb2022.commons.reflect.method.MethodHandle;
import org.atcraftmc.qlib.util.pipe.Pipeline;

public final class APIComponent<M extends MethodHandle> extends Pipeline<APIComponentRecord<M>> {
    private APIComponentRecord<M> record;

    public M get() {
        if (this.record == null) {
            this.update();
        }
        if (this.record == null) {
            throw new UnsupportedOperationException("No method handle found!");
        }

        return this.record.getHandle();
    }

    @Override
    public void update() {
        var list = this.list();

        if (list.isEmpty()) {
            return;
        }

        this.record = list.get(0);
    }

    public void addLast(String id, APITester tester, M handle) {
        addLast(id, new APIComponentRecord<M>(tester, handle));
    }

    public void addFirst(String id, APITester tester, M handle) {
        addFirst(id, new APIComponentRecord<M>(tester, handle));
    }
}
