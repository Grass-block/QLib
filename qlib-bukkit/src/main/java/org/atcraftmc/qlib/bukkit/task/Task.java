package org.atcraftmc.qlib.bukkit.task;

import org.bukkit.plugin.Plugin;

public interface Task {
    Plugin getOwner();

    void cancel();

    boolean isCancelled();
}
