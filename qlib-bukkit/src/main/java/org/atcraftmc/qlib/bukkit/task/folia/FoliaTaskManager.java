package org.atcraftmc.qlib.bukkit.task.folia;

import org.atcraftmc.qlib.bukkit.BukkitEventManager;
import org.atcraftmc.qlib.bukkit.task.TaskManager;
import org.atcraftmc.qlib.bukkit.task.TaskScheduler;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FoliaTaskManager extends TaskManager implements Listener {
    private final Map<String, FoliaRegionTaskScheduler> regions = new HashMap<>();
    private final Map<UUID, FoliaEntityTaskScheduler> entities = new HashMap<>();
    private final FoliaAsyncTaskScheduler async;
    private final FoliaGlobalTaskScheduler global;


    public FoliaTaskManager(Plugin owner) {
        super(owner);

        this.async = new FoliaAsyncTaskScheduler(owner, this);
        this.global = new FoliaGlobalTaskScheduler(owner, this);

        BukkitEventManager.registerEventListener(owner, this);
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
        if (!this.entities.containsKey(entity.getUniqueId())) {
            this.entities.put(entity.getUniqueId(), new FoliaEntityTaskScheduler(this.owner, entity, this));
        }

        var s = this.entities.get(entity.getUniqueId());
        s.setHandle(entity);
        return s;
    }

    @Override
    public TaskScheduler chunk(World world, int chunkX, int chunkZ) {
        var hash = world.getName() + "@" + chunkX + "," + chunkZ;

        return this.regions.computeIfAbsent(hash, (k) -> new FoliaRegionTaskScheduler(this.owner, world, chunkX, chunkZ, this));
    }

    @EventHandler
    private void onEntityDeath(EntityDeathEvent event) {
        var task = this.entities.remove(event.getEntity().getUniqueId());
        if (task == null) {
            return;
        }
        task.cleanup(this.owner);
    }

    @Override
    public void cleanup() {
        for (Map.Entry<String, FoliaRegionTaskScheduler> entry : this.regions.entrySet()) {
            entry.getValue().stop();
        }
        this.regions.clear();

        for (Map.Entry<UUID, FoliaEntityTaskScheduler> entry : this.entities.entrySet()) {
            entry.getValue().stop();
        }
        this.entities.clear();

        this.async.stop();
        this.global.stop();

        BukkitEventManager.unregisterEventListener(this);
    }
}
