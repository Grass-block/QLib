package org.atcraftmc.qlib.util.pipe;

public abstract class StackablePipeObject<I> {
    protected I parent;

    public final void setParent(I parent) {
        this.parent = parent;
    }
}
