package org.atcraftmc.qlib.bukkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface BukkitEventManager {
    Logger LOGGER = LogManager.getLogger("QLib/BukkitEventManager");

    Set<Class<? extends Event>> REGISTERED_EVENTS = new HashSet<>();
    Map<Plugin, Set<Listener>> REGISTERED_LISTENERS = new ConcurrentHashMap<>();

    static Set<Listener> registeredListeners(Plugin plugin) {
        return REGISTERED_LISTENERS.computeIfAbsent(plugin, k -> new HashSet<>());
    }

    static void registerEventListener(Plugin plugin, Listener listener) {
        if (registeredListeners(plugin).contains(listener)) {
            return;
        }

        try {
            for (var method : listener.getClass().getMethods()) {
                try {
                    if (!method.isAnnotationPresent(EventHandler.class)) {
                        continue;
                    }

                    REGISTERED_EVENTS.add((Class<? extends Event>) method.getParameterTypes()[0]);
                } catch (Throwable t) {
                    LOGGER.warn("Failed to record event for {}. (can be ignored)", method.getName());
                }
            }
        } catch (Throwable t) {
            LOGGER.warn("Failed to record events for {}. (can be ignored)", listener.getClass().getName());
        }

        Bukkit.getPluginManager().registerEvents(listener, plugin);

        registeredListeners(plugin).add(listener);
    }

    static void cleanup(Plugin plugin) {
        var count = 0;
        for (var type : new ArrayList<>(REGISTERED_EVENTS)) {
            try {
                var m_getHandlerList = type.getMethod("getHandlerList");

                var handlerList = (HandlerList) m_getHandlerList.invoke(null);

                handlerList.unregister(plugin);
                count++;
            } catch (Throwable e) {
                LOGGER.warn("Failed to cleanup event binding for {}. (can be ignored)", type.getSimpleName());
            }
        }

        LOGGER.info("Cleaned up {} events.", count);

        count = 0;
        for (var listener : new ArrayList<>(registeredListeners(plugin))) {
            try {
                unregisterEventListener(listener);
                count++;
            } catch (Exception ignored) {
            }
        }

        LOGGER.info("Cleaned up {} listeners.", count);
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

        } catch (Throwable e) {
            LOGGER.error("Failed to unregister event binding: {}", method.getName());
            LOGGER.catching(e);
        }
    }

    static void unregisterEventListener(Listener listener) {
        for (var m : listener.getClass().getMethods()) {
            unregisterEventBinding(m, listener);
        }

        for (var p : REGISTERED_LISTENERS.values()) {
            p.remove(listener);
        }
    }
}
