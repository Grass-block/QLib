package org.atcraftmc.qlib.task;

import java.util.function.Consumer;

public final class AutoCancelledTaskWrapper implements Consumer<Task> {
    private final Consumer<Task> action;
    private final TaskScheduler owner;

    public AutoCancelledTaskWrapper(Consumer<Task> action, TaskScheduler owner) {
        this.action = action;
        this.owner = owner;
    }

    @Override
    public void accept(Task task) {
        this.action.accept(task);
        this.owner.cancel(task);
    }
}
