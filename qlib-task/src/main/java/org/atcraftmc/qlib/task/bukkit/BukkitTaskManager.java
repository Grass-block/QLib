package org.atcraftmc.qlib.task.bukkit;

import org.atcraftmc.qlib.task.TaskManager;
import org.atcraftmc.qlib.task.TaskScheduler;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public final class BukkitTaskManager extends TaskManager {
    private final BukkitAsyncTaskScheduler async;
    private final BukkitGlobalTaskScheduler global;

    public BukkitTaskManager(Plugin owner) {
        super(owner);

        this.async = new BukkitAsyncTaskScheduler(owner, this);
        this.global = new BukkitGlobalTaskScheduler(owner, this);
    }

    @Override
    public TaskScheduler global() {
        return this.global;
    }

    @Override
    public TaskScheduler async() {
        return this.async;
    }

    @Override
    public TaskScheduler entity(Entity entity) {
        return this.global;
    }

    @Override
    public TaskScheduler chunk(World world, int chunkX, int chunkZ) {
        return this.global;
    }

    @Override
    public void cleanup() {
        this.async.stop();
        this.global.stop();
    }
}
