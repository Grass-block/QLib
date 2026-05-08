package org.atcraftmc.qlib.bukkit.util.permission;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RefPlayerPermissionManager extends PlayerPermissionManager {
    private final Map<UUID, VirtualPermissionAttachment> map = new HashMap<>();


    public RefPlayerPermissionManager(Plugin handle, PermissionEventHandler listener) {
        super(listener, handle);
    }

    @Override
    public void refresh(UUID uuid) {
        this.attachment(uuid).refresh();
    }

    @Override
    public void refresh(Player player) {
        this.attachment(player.getUniqueId()).refresh();
    }

    @Override
    public VirtualPermissionAttachment attachment(UUID uuid) {
        return this.map.computeIfAbsent(uuid, (k) -> new VirtualPermissionAttachment(this.handle));
    }

    @Override
    public VirtualPermissionAttachment attachment(Player player) {
        return this.attachment(player.getUniqueId());
    }

    @Override
    public void attach(Player player) {
        var attachment = this.attachment(player.getUniqueId());
        this.listener.onAttachmentCreated(player.getUniqueId(), attachment);
        attachment.bind(player);
    }

    @Override
    public void detach(Player player) {
        var attachment = this.map.remove(player.getUniqueId());
        if (attachment != null) {
            attachment.unbind();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        this.attachment(event.getPlayer().getUniqueId()).bind(event.getPlayer());
    }
}
