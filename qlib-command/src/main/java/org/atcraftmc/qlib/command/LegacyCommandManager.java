package org.atcraftmc.qlib.command;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("unused")
public interface LegacyCommandManager {

    @SuppressWarnings("rawtypes")
    static Map<String, Command> getKnownCommands(CommandMap map) {
        try {
            Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f.setAccessible(true);
            Object cmdMap = f.get(map);
            return (Map) cmdMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static CommandMap getCommandMap() {
        Class<?> c = Bukkit.getServer().getClass();
        try {
            Method m = c.getMethod("getCommandMap");
            m.setAccessible(true);
            return (CommandMap) m.invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static void register(Command command, String namespace) {
        CommandMap map = getCommandMap();
        Map<String, Command> knownCommands = getKnownCommands(map);

        command.register(map);
        knownCommands.put(namespace + ":" + command.getName(), command);
        knownCommands.put(command.getName(), command);

        for (String alias : command.getAliases()) {
            knownCommands.put(alias, command);
            knownCommands.put(namespace + ":" + alias, command);
        }
    }

    static void register(Command command) {
        CommandMap map = getCommandMap();
        Map<String, Command> knownCommands = getKnownCommands(map);

        command.register(map);

        knownCommands.put(command.getName(), command);
        for (String alias : command.getAliases()) {
            knownCommands.put(alias, command);
        }
    }

    static void unregister(String name, String namespace) {
        CommandMap map = getCommandMap();
        Map<String, Command> knownCommands = getKnownCommands(map);

        Command c = knownCommands.get(name);

        knownCommands.remove(name);
        knownCommands.remove(namespace + ":" + name);

        if (c == null) {
            return;
        }

        c.unregister(map);


        for (String alias : c.getAliases()) {
            knownCommands.remove(alias);
            knownCommands.remove(namespace + ":" + alias);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    static Command unregister(String name) {
        CommandMap map = getCommandMap();
        Map<String, Command> knownCommands = getKnownCommands(map);

        Command c = knownCommands.get(name);

        knownCommands.remove(name);

        if (c == null) {
            return null;
        }
        c.unregister(map);
        for (String alias : c.getAliases()) {
            knownCommands.remove(alias);
        }

        return c;
    }

    static void unregister(Command command) {
        unregister(command.getName());
    }

    static boolean isQLibCommand(String id) {
        return getCommandEntries().get(id) instanceof AbstractCommand;
    }

    @SuppressWarnings({"RedundantClassCall"})
    static Collection<String> getCommands() {
        CommandMap map = getCommandMap();
        try {
            Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f.setAccessible(true);
            Object cmdMap = f.get(map);
            return Map.class.cast(cmdMap).keySet();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void sync() {
        try {
            Server server = Bukkit.getServer();
            server.getClass().getDeclaredMethod("syncCommands").invoke(server);
        } catch (NoSuchMethodError | NoSuchMethodException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Map<String, Command> getCommandEntries() {
        try {
            return getKnownCommands(getCommandMap());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

