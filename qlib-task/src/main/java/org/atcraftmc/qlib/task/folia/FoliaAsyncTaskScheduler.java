package org.atcraftmc.qlib.task.folia;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.atcraftmc.qlib.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class FoliaAsyncTaskScheduler extends FoliaTaskScheduler {
    private final AsyncScheduler scheduler = Bukkit.getServer().getAsyncScheduler();

    public FoliaAsyncTaskScheduler(Plugin owner, TaskManager provider) {
        super(owner, provider);
    }

    @Override
    protected ScheduledTask runInternal(Consumer<ScheduledTask> action) {
        return this.scheduler.runNow(this.owner, action);
    }

    @Override
    protected ScheduledTask delayInternal(long delay, Consumer<ScheduledTask> action) {
        return this.scheduler.runDelayed(this.owner, action, delay * 50, TimeUnit.MILLISECONDS);
    }

    @Override
    protected ScheduledTask timerInternal(long delay, long period, Consumer<ScheduledTask> action) {
        return this.scheduler.runAtFixedRate(this.owner, action, delay * 50, period * 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cleanup(Plugin owner) {
        this.scheduler.cancelTasks(this.owner);
    }
}
