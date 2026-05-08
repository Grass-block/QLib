package org.atcraftmc.qlib.util.pipe;

import java.util.ArrayList;
import java.util.List;

public class CachedPipeline<I> extends Pipeline<I> {
    private List<I> list = new ArrayList<I>();

    @Override
    public List<I> list() {
        return this.list;
    }

    @Override
    public void update() {
        this.list = super.list();
    }
}
