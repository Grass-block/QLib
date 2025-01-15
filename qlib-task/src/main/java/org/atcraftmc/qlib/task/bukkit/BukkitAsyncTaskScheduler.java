package org.atcraftmc.qlib.task.bukkit;

import org.atcraftmc.qlib.task.TaskManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class BukkitAsyncTaskScheduler extends BukkitTaskScheduler {
    BukkitAsyncTaskScheduler(Plugin owner, TaskManager provider) {
        super(owner, provider);
    }

    @Override
    protected BukkitTask runInternal(Runnable action) {
        return wrap(action).runTaskAsynchronously(this.owner);
    }

    @Override
    protected BukkitTask delayInternal(long delay, Runnable action) {
        return wrap(action).runTaskLaterAsynchronously(this.owner, delay);
    }

    @Override
    protected BukkitTask timerInternal(long delay, long period, Runnable action) {
        return wrap(action).runTaskTimerAsynchronously(this.owner, delay, period);
    }
}
