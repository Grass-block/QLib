package org.atcraftmc.qlib.task.bukkit;

import org.atcraftmc.qlib.task.Task;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class BukkitTaskWrapper implements Task {
    private BukkitTask handle;

    public void setHandle(BukkitTask handle) {
        this.handle = handle;
    }

    @Override
    public Plugin getOwner() {
        if (this.handle == null) {
            return null;
        }
        return this.handle.getOwner();
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
            return false;
        }
        try {
            return this.handle.isCancelled();
        } catch (NoSuchMethodError ignored) {
            return false;
        }
    }
}
