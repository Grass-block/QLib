package org.atcraftmc.qlib.platform.api;

import me.gb2022.commons.reflect.method.MethodHandle;

public final class APIComponentRecord<M extends MethodHandle> {
    private final APITester tester;
    private final M handle;

    public APIComponentRecord(APITester tester, M handle) {
        this.tester = tester;
        this.handle = handle;
    }

    public APIComponentRecord(M handle) {
        this.handle = handle;
        this.tester = () -> true;
    }

    public APITester getTester() {
        return tester;
    }

    public M getHandle() {
        return handle;
    }
}
