package org.atcraftmc.qlib.task.folia;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.atcraftmc.qlib.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public final class FoliaGlobalTaskScheduler extends FoliaTaskScheduler {
    private final GlobalRegionScheduler scheduler;

    public FoliaGlobalTaskScheduler(Plugin owner, TaskManager provider) {
        super(owner, provider);
        this.scheduler = Bukkit.getServer().getGlobalRegionScheduler();
    }

    @Override
    protected ScheduledTask runInternal(Consumer<ScheduledTask> action) {
        try {
            return this.scheduler.run(this.owner, action);
        } catch (Exception e) {
            this.provider.registerFinalizeTask(() -> action.accept(null));
        }
        return null;
    }

    @Override
    protected ScheduledTask delayInternal(long delay, Consumer<ScheduledTask> action) {
        return this.scheduler.runDelayed(this.owner, action, delay <= 0 ? 1 : delay);
    }

    @Override
    protected ScheduledTask timerInternal(long delay, long period, Consumer<ScheduledTask> action) {
        return this.scheduler.runAtFixedRate(this.owner, action, delay <= 0 ? 1 : delay, period);
    }
}
