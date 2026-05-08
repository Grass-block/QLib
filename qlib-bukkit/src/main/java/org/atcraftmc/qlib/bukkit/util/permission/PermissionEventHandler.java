package org.atcraftmc.qlib.bukkit.util.permission;

import org.bukkit.permissions.PermissionAttachment;

import java.util.UUID;

public interface PermissionEventHandler {
    default void onAttachmentCreated(UUID uuid, PermissionAttachment attachment) {
    }

    default void onAttachmentRemoved(UUID uuid, PermissionAttachment attachment) {

    }
}
