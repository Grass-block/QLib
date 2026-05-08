package org.atcraftmc.qlib.util.pipe;

public class StackedPipeline<I> extends Pipeline<I> {
    private final I base;
    protected I last;

    public StackedPipeline(I base) {
        this.base = base;
    }

    @Override
    public synchronized void addLast(String id, I object) {
        super.addLast(id, object);
        rebuildStructure();
    }

    @Override
    public synchronized void addFirst(String id, I object) {
        super.addFirst(id, object);
        rebuildStructure();
    }

    @Override
    public synchronized void addBefore(String target, String id, I object) {
        super.addBefore(target, id, object);
        rebuildStructure();
    }

    @Override
    public synchronized void addAfter(String target, String id, I object) {
        super.addAfter(target, id, object);
        rebuildStructure();
    }

    @Override
    public void remove(String id) {
        super.remove(id);
        rebuildStructure();
    }


    //create a *stack* of calling chain.
    public void rebuildStructure() {
        var list = this.list();

        if (list.isEmpty()) {
            return;
        }

        ((StackablePipeObject<? super I>) list.get(0)).setParent(this.base);

        if (list.size() > 1) {
            for (var i = 1; i < list.size(); i++) {
                ((StackablePipeObject<? super I>) list.get(i)).setParent(list.get(i - 1));
            }
        }

        this.last = list.get(list.size() - 1);
    }

    public I getLast() {
        return last;
    }
}
