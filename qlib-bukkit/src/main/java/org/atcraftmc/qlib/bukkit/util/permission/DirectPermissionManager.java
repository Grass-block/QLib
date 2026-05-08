package org.atcraftmc.qlib.bukkit.util.permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public final class DirectPermissionManager extends PlayerPermissionManager implements Listener {
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    public DirectPermissionManager(PermissionEventHandler listener, Plugin handle) {
        super(listener, handle);
    }

    @Override
    public void refresh(UUID uuid) {
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).recalculatePermissions();
    }

    @Override
    public void refresh(Player player) {
        player.recalculatePermissions();
    }

    @Override
    public PermissionAttachment attachment(UUID uuid) {
        var player = Bukkit.getPlayer(uuid);

        if (player == null) {
            throw new NullPointerException("player is null");
        }

        return this.attachment(player);
    }

    @Override
    public PermissionAttachment attachment(Player player) {
        if (!this.attachments.containsKey(player.getUniqueId())) {
            var p = player.addAttachment(this.handle);
            this.listener.onAttachmentCreated(player.getUniqueId(), p);
            this.attachments.put(player.getUniqueId(), p);
        }

        return this.attachments.get(player.getUniqueId());
    }

    @Override
    public void attach(Player player) {
        this.attachment(player);
        refresh(player);
    }

    @Override
    public void detach(Player player) {
        this.attachments.remove(player.getUniqueId()).remove();
    }
}
