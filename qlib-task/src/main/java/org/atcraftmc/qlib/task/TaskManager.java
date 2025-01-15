package org.atcraftmc.qlib.task;

import org.atcraftmc.qlib.task.bukkit.BukkitTaskManager;
import org.atcraftmc.qlib.task.folia.FoliaTaskManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public abstract class TaskManager {
    protected final Plugin owner;
    private final Set<Runnable> finalizeTasks = new HashSet<>();

    protected TaskManager(Plugin owner) {
        this.owner = owner;
    }

    public static TaskManager getInstance(final Plugin owner) {
        if (owner == null) {
            throw new NullPointerException("owner is null");
        }

        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
        } catch (ClassNotFoundException e) {
            return new BukkitTaskManager(owner);
        }

        return new FoliaTaskManager(owner);
    }


    public abstract TaskScheduler global();

    public abstract TaskScheduler async();

    public abstract TaskScheduler entity(Entity entity);

    public abstract TaskScheduler chunk(World world, int chunkX, int chunkZ);

    public final TaskScheduler chunk(Location location) {
        return chunk(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public void cleanup() {
    }

    //finalize task api
    public void registerFinalizeTask(Runnable command) {
        this.finalizeTasks.add(command);
    }

    public void runFinalizeTask() {
        for (Runnable task : this.finalizeTasks) {
            task.run();
        }
        this.finalizeTasks.clear();
    }
}
