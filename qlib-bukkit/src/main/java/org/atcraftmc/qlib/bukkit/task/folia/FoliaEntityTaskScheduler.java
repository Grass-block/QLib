package org.atcraftmc.qlib.bukkit.task.folia;

import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.atcraftmc.qlib.bukkit.task.TaskManager;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public final class FoliaEntityTaskScheduler extends FoliaTaskScheduler {
    private EntityScheduler scheduler;

    public FoliaEntityTaskScheduler(Plugin owner, Entity entity, TaskManager provider) {
        super(owner, provider);
        this.scheduler = entity.getScheduler();
    }

    @Override
    protected ScheduledTask runInternal(Consumer<ScheduledTask> action) {
        return this.scheduler.run(this.owner, action, null);
    }

    @Override
    protected ScheduledTask delayInternal(long delay, Consumer<ScheduledTask> action) {
        return this.scheduler.runDelayed(this.owner, action, null, delay <= 0 ? 1 : delay);
    }

    @Override
    protected ScheduledTask timerInternal(long delay, long period, Consumer<ScheduledTask> action) {
        return this.scheduler.runAtFixedRate(this.owner, action, null, delay <= 0 ? 1 : delay, period);
    }

    public void setHandle(Entity entity) {
        if (entity.getScheduler() == this.scheduler) {
            return;
        }

        this.scheduler = entity.getScheduler();
    }
}
