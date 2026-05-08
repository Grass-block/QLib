package org.atcraftmc.qlib.bukkit.util.permission;

import org.atcraftmc.qlib.bukkit.BukkitEventManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public abstract class PlayerPermissionManager implements Listener {
    protected final PermissionEventHandler listener;
    protected final Plugin handle;

    protected PlayerPermissionManager(PermissionEventHandler listener, Plugin handle) {
        this.listener = listener;
        this.handle = handle;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.attach(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.detach(event.getPlayer());
    }

    public void initialize() {
        BukkitEventManager.registerEventListener(this.handle, this);
        for (var player : Bukkit.getOnlinePlayers()) {
            this.attach(player);
        }
    }

    public void release() {
        BukkitEventManager.unregisterEventListener(this);

        for (var player : Bukkit.getOnlinePlayers()) {
            this.detach(player);
        }
    }

    public abstract void refresh(UUID uuid);

    public abstract void refresh(Player player);

    public abstract PermissionAttachment attachment(UUID uuid);

    public abstract PermissionAttachment attachment(Player player);

    public abstract void attach(Player player);

    public abstract void detach(Player player);
}
