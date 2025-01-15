package org.atcraftmc.qlib.task.folia;

import org.atcraftmc.qlib.task.TaskManager;
import org.atcraftmc.qlib.task.TaskScheduler;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public final class FoliaTaskManager extends TaskManager {
    private final Map<String, FoliaRegionTaskScheduler> regions = new HashMap<>();
    private final Map<Entity, FoliaEntityTaskScheduler> entities = new HashMap<>();

    private final FoliaAsyncTaskScheduler async;
    private final FoliaGlobalTaskScheduler global;


    public FoliaTaskManager(Plugin owner) {
        super(owner);

        this.async = new FoliaAsyncTaskScheduler(owner, this);
        this.global = new FoliaGlobalTaskScheduler(owner, this);
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
        return this.entities.computeIfAbsent(entity, (e) -> new FoliaEntityTaskScheduler(this.owner, e, this));
    }

    @Override
    public TaskScheduler chunk(World world, int chunkX, int chunkZ) {
        var hash = world.getName() + "@" + chunkX + "," + chunkZ;

        return this.regions.computeIfAbsent(hash, (k) -> new FoliaRegionTaskScheduler(this.owner, world, chunkX, chunkZ, this));
    }

    @Override
    public void cleanup() {
        for (Map.Entry<String, FoliaRegionTaskScheduler> entry : this.regions.entrySet()) {
            entry.getValue().stop();
        }
        this.regions.clear();

        for (Map.Entry<Entity, FoliaEntityTaskScheduler> entry : this.entities.entrySet()) {
            entry.getValue().stop();
        }
        this.entities.clear();

        this.async.stop();
        this.global.stop();
    }
}
