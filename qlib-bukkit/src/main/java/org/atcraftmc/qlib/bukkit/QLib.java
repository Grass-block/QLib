package org.atcraftmc.qlib.bukkit;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.audience.QLibAudience;
import org.atcraftmc.qlib.bukkit.task.TaskManager;
import org.atcraftmc.qlib.text.TextEngine;
import org.atcraftmc.qlib.texts.ContextComponentBlockBuilder;
import org.bukkit.command.CommandSender;

public interface QLib {
    static QLibBukkitContext context() {
        return QLibBukkitContext.global();
    }

    static QLibAudience audience(CommandSender sender) {
        return context().audiences().get(sender);
    }

    static void message(CommandSender sender, ComponentLike message) {
        audience(sender).sendMessage(message);
    }

    static TextEngine textEngine() {
        return context().textEngine();
    }

    static TaskManager task(){
        return context().taskManager();
    }

    static ContextComponentBlockBuilder textBuilder() {
        return context().textBuilder();
    }
}
