package org.atcraftmc.qlib.command;

import org.atcraftmc.qlib.command.execute.CommandExecutor;
import org.atcraftmc.qlib.command.execute.TabList;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.permissions.Permission;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractCommand extends Command implements CommandExecutor, Listener {
    private final Map<String, AbstractCommand> subCommands = new HashMap<>();
    protected CommandExecutor executor = this;
    private Command covered;

    private CommandManager handle;

    protected AbstractCommand() {
        super("");
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }


    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        if (!this.isRegistered()) {
            return;
        }

        String name = this.getName();
        String name2 = this.handle.getCommandNamespace() + ":" + this.getName();

        if (!validateExecutable(event.getPlayer(), false)) {
            return;
        }

        event.getCommands().remove(name);
        event.getCommands().remove(name2);
        for (String alias : this.getAliases()) {
            event.getCommands().remove(alias);
        }
    }


    protected void init(CommandManager handle) {
        this.handle = handle;
        for (Class<? extends AbstractCommand> clazz : this.getDescriptor().subCommands()) {
            try {
                this.registerSubCommand(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String permission = this.getPermission();
        if (permission.equals(QuarkCommand.NO_INFO)) {
            return;
        }
        String[] perms = this.getRawPermission().split(";");
        for (String str : perms) {
            if (Objects.equals(str, "")) {
                continue;
            }
            this.handle.createPermission(str);
        }
    }

    //description
    @Override
    public String getName() {
        return this.getDescriptor().name();
    }

    public QuarkCommand getDescriptor() {
        return this.getClass().getAnnotation(QuarkCommand.class);
    }

    public <V> V optionalDescriptorInfo(Function<QuarkCommand, V> consumer, V fallback) {
        QuarkCommand desc = this.getDescriptor();
        if (desc == null) {
            return fallback;
        }
        return consumer.apply(desc);
    }

    public boolean isOP() {
        return this.getDescriptor() == null || getDescriptor().op();
    }

    public final boolean isPlayerOnly() {
        return this.getDescriptor() != null && getDescriptor().playerOnly();
    }

    public final boolean isEventBased() {
        return this.optionalDescriptorInfo(QuarkCommand::eventBased, false);
    }

    @Override
    public final String getPermission() {
        return getRawPermission().replaceFirst("\\+", "").replaceFirst("-", "").replace("!", "");
    }

    public final String getRawPermission() {
        return optionalDescriptorInfo(QuarkCommand::permission, "");
    }

    @Override
    public final String getUsage() {
        return optionalDescriptorInfo(QuarkCommand::usage, QuarkCommand.NO_INFO);
    }

    @Override
    public final String getDescription() {
        return optionalDescriptorInfo(QuarkCommand::description, QuarkCommand.NO_INFO);
    }

    @Override
    public final List<String> getAliases() {
        return optionalDescriptorInfo((d) -> List.of(d.aliases()), List.of());
    }


    //error message
    public final void sendExceptionMessage(CommandSender sender, Throwable... exceptions) {
        this.handle.sendExceptionMessage(sender, exceptions);
    }

    public final void sendPermissionMessage(CommandSender sender, String s) {
        this.handle.sendPermissionMessage(sender, s);
    }

    public final void sendPlayerOnlyMessage(CommandSender sender) {
        this.handle.sendPlayerOnlyMessage(sender);
    }


    //execute
    @Override
    public final List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> tabList = new TabList();
        if (this.validateExecutable(sender, false)) {
            return tabList;
        }

        if (args.length > 1) {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            for (String s : this.subCommands.keySet()) {
                if (!args[0].equals(s)) {
                    continue;
                }
                return subCommands.get(s).tabComplete(sender, "", subArgs);
            }
        }
        if (args.length == 1) {
            tabList.addAll(this.subCommands.keySet());
        }

        this.executor.onCommandTab(sender, args, tabList);
        return tabList;
    }

    @Override
    public final List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) {
        return this.tabComplete(sender, alias, args);
    }

    @Override
    public final boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (this.validateExecutable(sender, true)) {
            return true;
        }

        if (args.length >= 1) {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            for (String s : this.subCommands.keySet()) {
                if (!args[0].equals(s)) {
                    continue;
                }
                return subCommands.get(s).execute(sender, "", subArgs);
            }
        }

        try {
            this.executor.onCommand(sender, args);
        } catch (Exception e) {
            if ((e instanceof ArrayIndexOutOfBoundsException)) {
                this.sendExceptionMessage(sender);
                this.handle.handle.getLogger().info("none-match arguments: " + Arrays.toString(args));
                return false;
            }
            if (!Objects.equals(e.getMessage(), "FORMAT_ERROR")) {
                try {
                    e.printStackTrace();
                } catch (Exception ignored) {
                }
            }
            e.printStackTrace();
            this.sendExceptionMessage(sender);
        }
        return true;
    }

    private boolean validateExecutable(CommandSender sender, boolean msg) {
        if (!(sender instanceof Player) && this.isPlayerOnly()) {
            if (msg) {
                this.sendPlayerOnlyMessage(sender);
            }
            return true;
        }
        String permission = this.getPermission();
        if (permission.isEmpty()) {
            if (this.isOP() && !sender.isOp()) {
                if (msg) {
                    this.sendPermissionMessage(sender, "(ServerOperator)");
                }
                return true;
            }
        } else {
            String[] perms = this.getRawPermission().split(";");
            for (String str : perms) {
                if (Objects.equals(str, "")) {
                    continue;
                }

                Permission perm = this.handle.getPermission(str);

                if (perm == null) {
                    this.handle.handle.getLogger().warning("skipped unknown permission: " + str);
                    continue;
                }

                if (!sender.hasPermission(perm)) {
                    if (msg) {
                        this.sendPermissionMessage(sender, str);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    protected final void checkException(boolean predict) {
        if (predict) {
            return;
        }
        throw new RuntimeException("FORMAT_ERROR");
    }


    //sub-command
    public final void registerSubCommand(AbstractCommand command) {
        this.subCommands.put(command.getName(), command);
    }

    public final void unregisterSubCommand(AbstractCommand command) {
        this.unregisterSubCommand(command.getName());
    }

    public final void unregisterSubCommand(String id) {
        this.subCommands.remove(id);
    }


    public Command getCoveredCommand() {
        return null;
    }

    public Command getCovered() {
        return covered;
    }

    public Map<String, AbstractCommand> getSubCommands() {
        return subCommands;
    }

    public void fetchCovered() {
        this.covered = getCoveredCommand();
    }


    @Override
    public final boolean testPermissionSilent(CommandSender target) {
        return true;
    }

    @Override
    public final CommandManager getHandle() {
        return this.handle;
    }
}
