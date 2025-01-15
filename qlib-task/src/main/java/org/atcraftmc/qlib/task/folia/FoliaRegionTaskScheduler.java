package org.atcraftmc.qlib.task.folia;

import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.atcraftmc.qlib.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public final class FoliaRegionTaskScheduler extends FoliaTaskScheduler {
    private final RegionScheduler scheduler = Bukkit.getServer().getRegionScheduler();

    private final Location location;

    public FoliaRegionTaskScheduler(Plugin owner, World world, int cx, int cz, TaskManager provider) {
        super(owner, provider);
        this.location = new Location(world, cx * 16, 0, cz * 16);
    }

    @Override
    protected ScheduledTask runInternal(Consumer<ScheduledTask> action) {
        return this.scheduler.run(this.owner, this.location, action);
    }

    @Override
    protected ScheduledTask delayInternal(long delay, Consumer<ScheduledTask> action) {
        return this.scheduler.runDelayed(this.owner, this.location, action, delay <= 0 ? 1 : delay);
    }

    @Override
    protected ScheduledTask timerInternal(long delay, long period, Consumer<ScheduledTask> action) {
        return this.scheduler.runAtFixedRate(this.owner, this.location, action, delay <= 0 ? 1 : delay, period);
    }
}
