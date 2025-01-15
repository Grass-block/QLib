package org.atcraftmc.qlib.command;

import org.atcraftmc.qlib.command.execute.CommandErrorType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandManager {
    protected final Plugin handle;
    private final Map<String, AbstractCommand> commands = new HashMap<>();

    protected CommandManager(Plugin handle) {
        this.handle = handle;
    }

    public abstract void sendExceptionMessage(CommandSender sender, Throwable... exceptions);

    public abstract void sendExecutionErrorMessage(CommandSender sender, CommandErrorType code, Object... info);

    public abstract void sendPermissionMessage(CommandSender sender, String permission);

    public abstract void sendPlayerOnlyMessage(CommandSender sender);

    public abstract void createPermission(String permission);

    public abstract String getCommandNamespace();

    public abstract Permission getPermission(String permission);


    public void register(AbstractCommand command) {
        command.fetchCovered();
        command.init(this);

        LegacyCommandManager.register(command, this.getCommandNamespace());

        this.commands.put(command.getName(), command);
    }

    public void unregister(AbstractCommand command) {
        this.commands.remove(command.getName());

        LegacyCommandManager.unregister(command.getName(), this.getCommandNamespace());
        Command covered = command.getCovered();
        if (covered == null) {
            return;
        }

        covered.register(LegacyCommandManager.getCommandMap());
    }

    public AbstractCommand getCommand(String commandName) {
        return commands.get(commandName);
    }

}
