package org.atcraftmc.qlib.platform.api;

@FunctionalInterface
public interface APITester {
    Object run() throws Throwable;
}
