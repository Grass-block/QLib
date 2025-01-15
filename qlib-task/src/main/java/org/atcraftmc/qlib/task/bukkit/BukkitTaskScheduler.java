package org.atcraftmc.qlib.task.bukkit;

import org.atcraftmc.qlib.task.AutoCancelledTaskWrapper;
import org.atcraftmc.qlib.task.Task;
import org.atcraftmc.qlib.task.TaskManager;
import org.atcraftmc.qlib.task.TaskScheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public abstract class BukkitTaskScheduler extends TaskScheduler {
    protected BukkitTaskScheduler(Plugin owner, TaskManager provider) {
        super(owner, provider);
    }

    static BukkitRunnable wrap(Runnable command) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                command.run();
            }
        };
    }

    @Override
    public Task run(String id, Consumer<Task> action) {
        var wrapper = new BukkitTaskWrapper();
        var command = new AutoCancelledTaskWrapper(action, this);

        register(id, wrapper);
        wrapper.setHandle(this.runInternal(() -> command.accept(wrapper)));

        return wrapper;
    }

    @Override
    public Task delay(String id, long delay, Consumer<Task> action) {
        var wrapper = new BukkitTaskWrapper();
        var command = new AutoCancelledTaskWrapper(action, this);

        register(id, wrapper);
        wrapper.setHandle(this.delayInternal(delay, () -> command.accept(wrapper)));

        return wrapper;
    }

    @Override
    public Task timer(String id, long delay, long period, Consumer<Task> action) {
        var wrapper = new BukkitTaskWrapper();

        register(id, wrapper);
        wrapper.setHandle(this.timerInternal(delay, period, () -> action.accept(wrapper)));

        return wrapper;
    }

    protected abstract BukkitTask runInternal(Runnable action);

    protected abstract BukkitTask delayInternal(long delay, Runnable action);

    protected abstract BukkitTask timerInternal(long delay, long period, Runnable action);
}
