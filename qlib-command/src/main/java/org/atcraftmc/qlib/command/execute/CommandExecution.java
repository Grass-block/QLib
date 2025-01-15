package org.atcraftmc.qlib.command.execute;

import org.atcraftmc.qlib.command.AbstractCommand;
import org.atcraftmc.qlib.command.assertion.ArgumentAssertionException;
import org.atcraftmc.qlib.command.assertion.CommandAssertionException;
import org.atcraftmc.qlib.command.assertion.NumberLimitation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ClassCanBeRecord")
public final class CommandExecution {
    private final String[] args;
    private final CommandSender sender;
    private final CommandExecutor command;

    public CommandExecution(CommandSender sender, String[] args, CommandExecutor command) {
        this.sender = sender;
        this.args = args;
        this.command = command;
    }

    public String requireArgumentAt(int position) {
        if (this.args.length <= position) {
            throw new ArgumentAssertionException(CommandErrorType.MISSING_ARGUMENTS, position);
        }
        return this.args[position];
    }

    public boolean hasArgumentAt(int position) {
        return this.args.length > position;
    }

    private void testNumber(double value, int position, NumberLimitation... requirements) {
        for (NumberLimitation lim : requirements) {
            lim.test(value, position);
        }
    }

    public int requireArgumentInteger(int position, NumberLimitation... requirements) {
        String arg = this.requireArgumentAt(position);
        try {
            int n = Integer.parseInt(arg);
            testNumber(n, position, requirements);
            return n;

        } catch (NumberFormatException e) {
            throw new ArgumentAssertionException(CommandErrorType.ARGUMENT_TYPE_INT, position, arg);
        }
    }

    public float requireArgumentFloat(int position, NumberLimitation... requirements) {
        String arg = this.requireArgumentAt(position);
        try {
            float n = Float.parseFloat(arg);
            testNumber(n, position, requirements);
            return n;
        } catch (NumberFormatException e) {
            throw new ArgumentAssertionException(CommandErrorType.ARGUMENT_TYPE_FLOAT, position, arg);
        }
    }

    public double requireArgumentDouble(int position, NumberLimitation... requirements) {
        String arg = this.requireArgumentAt(position);
        try {
            double n = Double.parseDouble(arg);
            testNumber(n, position, requirements);
            return n;
        } catch (NumberFormatException e) {
            throw new ArgumentAssertionException(CommandErrorType.ARGUMENT_TYPE_DOUBLE, position, arg);
        }
    }

    public String requireEnum(int position, String... accept) {
        String arg = requireArgumentAt(position);

        var list = new java.util.ArrayList<>(List.of(accept));

        if (this.command instanceof AbstractCommand cmd) {
            list.addAll(cmd.getSubCommands().keySet());
        }

        if (!list.contains(arg)) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (String s : list) {
                sb.append(s).append("|");
            }
            sb.append("]");

            sb.deleteCharAt(sb.length() - 2);

            throw new ArgumentAssertionException(CommandErrorType.ARGUMENT_TYPE_ENUM, position, sb.toString(), arg);
        }

        return arg;
    }

    public String requireEnum(int position, Set<String> accept) {
        return requireEnum(position, accept.toArray(new String[0]));
    }

    public Player requirePlayer(int position) {
        String name = this.requireArgumentAt(position);

        Player p = Bukkit.getPlayerExact(name);

        if (p == null) {
            throw new ArgumentAssertionException(CommandErrorType.REQUIRE_EXIST_PLAYER, position, name);
        }

        return p;
    }

    public OfflinePlayer requireOfflinePlayer(int position) {
        String name = this.requireArgumentAt(position);

        return Bukkit.getOfflinePlayer(name);
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    public int requireIntegerOrElse(int position, int fallback, NumberLimitation... bounds) {
        String input = this.requireArgumentAt(position);
        try {
            int n = Integer.parseInt(input);
            testNumber(n, position, bounds);
            return n;
        } catch (NumberFormatException e) {
            try {
                double val = Double.parseDouble(input);
                throw new ArgumentAssertionException(CommandErrorType.ARGUMENT_TYPE_INT, position, val);
            } catch (ArgumentAssertionException ee) {
                throw ee;
            } catch (Exception ignored) {
            }

            return fallback;
        }
    }

    public void requirePermission(String permission) {
        requirePermission(Bukkit.getPluginManager().getPermission(permission));
    }

    public void requireAnyPermission(String... permissions) {
        requireAnyPermission(Arrays.stream(permissions).map((s) -> Bukkit.getPluginManager().getPermission(s)).toArray(Permission[]::new));
    }

    public void requireAllPermission(String... permissions) {
        requireAllPermission(Arrays.stream(permissions).map((s) -> Bukkit.getPluginManager().getPermission(s)).toArray(Permission[]::new));
    }

    public void requirePermission(Permission permission) {
        if (this.sender.hasPermission(permission)) {
            return;
        }

        throw new CommandAssertionException(CommandErrorType.REQUIRE_PERMISSION, permission.getName());
    }


    public void requireAnyPermission(Permission... permissions) {
        for (Permission p : permissions) {
            if (this.sender.hasPermission(p)) {
                return;
            }
        }
        String[] names = new String[permissions.length];

        for (int i = 0; i < names.length; i++) {
            names[i] = permissions[i].getName();
        }

        throw new CommandAssertionException(CommandErrorType.LACK_ANY_PERMISSION, Arrays.toString(names));
    }

    public void requireAllPermission(Permission... permissions) {
        for (Permission p : permissions) {
            if (!this.sender.hasPermission(p)) {
                String[] names = new String[permissions.length];

                for (int i = 0; i < names.length; i++) {
                    names[i] = permissions[i].getName();
                }

                throw new CommandAssertionException(CommandErrorType.LACK_ALL_PERMISSION, Arrays.toString(names));
            }
        }
    }

    public Player requireSenderAsPlayer() {
        if (!(this.sender instanceof Player p)) {
            throw new CommandAssertionException(CommandErrorType.REQUIRE_SENDER_PLAYER);
        }
        return p;
    }

    public void matchArgument(int position, String id, Runnable command) {
        if (Objects.equals(requireArgumentAt(position), id)) {
            command.run();
        }
    }

    public String requireRemainAsParagraph(int position, boolean appendSpace) {
        StringBuilder sb = new StringBuilder();
        sb.append(requireArgumentAt(position)).append(appendSpace ? " " : "");

        for (int i = position + 1; i < args.length; i++) {
            sb.append(args[i]).append(appendSpace ? " " : "");
        }
        return sb.toString();
    }
}
