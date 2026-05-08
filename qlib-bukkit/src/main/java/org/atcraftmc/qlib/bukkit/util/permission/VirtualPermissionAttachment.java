package org.atcraftmc.qlib.bukkit.util.permission;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionRemovedExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class VirtualPermissionAttachment extends LazyPermissionAttachment {
    private Player player;
    private PermissionAttachment view;

    public VirtualPermissionAttachment(@NotNull Plugin plugin) {
        super(plugin, null);
    }

    public void bind(Player player) {
        if (this.player != null) {
            this.unbind();
        }

        this.player = player;
        this.view = this.player.addAttachment(this.getPlugin());
        this.refresh();
    }

    public void unbind() {
        this.player.removeAttachment(this.view);
        this.player.recalculatePermissions();
    }

    @Override
    public boolean remove() {
        throw new UnsupportedOperationException("Virtual attachment");
    }

    @Override
    public @NotNull Permissible getPermissible() {
        throw new UnsupportedOperationException("Virtual attachment");
    }

    @Override
    public @Nullable PermissionRemovedExecutor getRemovalCallback() {
        throw new UnsupportedOperationException("Virtual attachment");
    }

    @Override
    public void setRemovalCallback(@Nullable PermissionRemovedExecutor ex) {
        throw new UnsupportedOperationException("Virtual attachment");
    }

    @Override
    public void refresh() {
        if (this.view == null) {
            return;
        }

        try {
            var f = PermissionAttachment.class.getDeclaredField("permissions");
            f.setAccessible(true);
            var map = ((Map<String, Boolean>) f.get(this.view));

            map.clear();
            map.putAll(this.permissions);
            this.view.getPermissible().recalculatePermissions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
