package org.atcraftmc.qlib.task.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.atcraftmc.qlib.task.Task;
import org.bukkit.plugin.Plugin;

public final class ThreadedRegionTaskWrapper implements Task {
    private ScheduledTask handle;

    public void setHandle(ScheduledTask handle) {
        this.handle = handle;
    }

    @Override
    public Plugin getOwner() {
        if (this.handle == null) {
            return null;
        }
        return this.handle.getOwningPlugin();
    }

    @Override
    public void cancel() {
        if (this.handle == null) {
            return;
        }
        this.handle.cancel();
    }

    @Override
    public boolean isCancelled() {
        if (this.handle == null) {
            return true;
        }
        return this.handle.isCancelled();
    }
}
