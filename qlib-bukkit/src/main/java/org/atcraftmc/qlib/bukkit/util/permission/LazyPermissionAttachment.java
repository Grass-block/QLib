package org.atcraftmc.qlib.bukkit.util.permission;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class LazyPermissionAttachment extends PermissionAttachment {
    protected final Map<String, Boolean> permissions = new LinkedHashMap<>();

    public LazyPermissionAttachment(@NotNull Plugin plugin, @NotNull Permissible permissible) {
        super(plugin, permissible);
    }

    public @NotNull Map<String, Boolean> getPermissions() {
        return new LinkedHashMap<>(this.permissions);
    }

    public void setPermission(@NotNull String name, boolean value) {
        this.permissions.put(name.toLowerCase(Locale.ENGLISH), value);
        this.refresh();
    }

    public void setPermission(@NotNull Permission perm, boolean value) {
        this.setPermission(perm.getName(), value);
    }

    public void unsetPermission(@NotNull String name) {
        this.permissions.remove(name.toLowerCase(Locale.ENGLISH));
        this.refresh();
    }

    public void unsetPermission(@NotNull Permission perm) {
        this.unsetPermission(perm.getName());
    }

    public void refresh() {
        this.getPermissible().recalculatePermissions();
    }
}
