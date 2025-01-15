package org.atcraftmc.qlib.task.bukkit;

import org.atcraftmc.qlib.task.TaskManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class BukkitGlobalTaskScheduler extends BukkitTaskScheduler {
    BukkitGlobalTaskScheduler(Plugin owner, TaskManager provider) {
        super(owner, provider);
    }

    @Override
    protected BukkitTask runInternal(Runnable action) {
        try {
            return wrap(action).runTask(this.owner);
        }catch (Exception e){
            this.provider.registerFinalizeTask(action);
            return null;
        }
    }

    @Override
    protected BukkitTask delayInternal(long delay, Runnable action) {
        return wrap(action).runTaskLater(this.owner, delay);
    }

    @Override
    protected BukkitTask timerInternal(long delay, long period, Runnable action) {
        return wrap(action).runTaskTimer(this.owner, delay, period);
    }
}
