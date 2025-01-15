package org.atcraftmc.qlib.task.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.atcraftmc.qlib.task.AutoCancelledTaskWrapper;
import org.atcraftmc.qlib.task.Task;
import org.atcraftmc.qlib.task.TaskManager;
import org.atcraftmc.qlib.task.TaskScheduler;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public abstract class FoliaTaskScheduler extends TaskScheduler {
    protected FoliaTaskScheduler(Plugin owner, TaskManager provider) {
        super(owner, provider);
    }

    @Override
    public Task run(String id, Consumer<Task> action) {
        var wrapper = new ThreadedRegionTaskWrapper();
        var command = new AutoCancelledTaskWrapper(action, this);

        register(id, wrapper);
        wrapper.setHandle(this.runInternal((handle) -> command.accept(wrapper)));

        return wrapper;
    }

    @Override
    public Task delay(String id, long delay, Consumer<Task> action) {
        var wrapper = new ThreadedRegionTaskWrapper();
        var command = new AutoCancelledTaskWrapper(action, this);

        register(id, wrapper);
        wrapper.setHandle(this.delayInternal(delay, (handle) -> command.accept(wrapper)));

        return wrapper;
    }

    @Override
    public Task timer(String id, long delay, long period, Consumer<Task> action) {
        var wrapper = new ThreadedRegionTaskWrapper();

        register(id, wrapper);
        wrapper.setHandle(this.timerInternal(delay, period, (handle) -> action.accept(wrapper)));

        return wrapper;
    }

    protected abstract ScheduledTask runInternal(Consumer<ScheduledTask> action);

    protected abstract ScheduledTask delayInternal(long delay, Consumer<ScheduledTask> action);

    protected abstract ScheduledTask timerInternal(long delay, long period, Consumer<ScheduledTask> action);
}
