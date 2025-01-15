package org.atcraftmc.qlib.task;

import org.bukkit.plugin.Plugin;

public interface Task {
    Plugin getOwner();

    void cancel();

    boolean isCancelled();
}
