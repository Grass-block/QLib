package org.atcraftmc.qlib.bukkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public interface BukkitEventManager {
    Logger LOGGER = LogManager.getLogger("QLib/BukkitEventManager");

    static void registerEventListener(Plugin plugin, Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    static void unregisterEventBinding(Method method, Listener listener) {
        try {
            if (!method.isAnnotationPresent(EventHandler.class)) {
                return;
            }

            var eventType = method.getParameterTypes()[0];
            var m_getHandlerList = eventType.getMethod("getHandlerList");

            var handlerList = (HandlerList) m_getHandlerList.invoke(null);

            handlerList.unregister(listener);
        } catch (Exception e) {
            LOGGER.error("Failed to unregister event binding: {}", method.getName());
            LOGGER.catching(e);
        }
    }

    static void unregisterEventListener(Listener listener) {
        try {
            for (var m : listener.getClass().getMethods()) {
                unregisterEventBinding(m, listener);
            }
        } catch (Throwable e) {
            LOGGER.error("Failed to unregister event listener: {}", listener.getClass().getName());
            LOGGER.catching(e);
        }
    }
}
