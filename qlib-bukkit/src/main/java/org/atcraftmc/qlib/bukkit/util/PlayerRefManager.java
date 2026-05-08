package org.atcraftmc.qlib.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerRefManager implements Listener {
    private final Map<UUID, PlayerRef> playerRefs = new HashMap<>();

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        this.get(event.getPlayer().getUniqueId()).handle(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event) {
        this.playerRefs.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.playerRefs.put(event.getPlayer().getUniqueId(), new PlayerRef().handle(event.getPlayer()));
    }

    public PlayerRef get(final UUID uuid) {
        return this.playerRefs.computeIfAbsent(uuid, (k) -> new PlayerRef()).handle(Bukkit.getPlayer(uuid));
    }
}
