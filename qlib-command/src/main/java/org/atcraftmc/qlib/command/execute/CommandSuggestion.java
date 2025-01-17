package org.atcraftmc.qlib.command.execute;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class CommandSuggestion {
    public static final List<String> ALL_OFFLINE_PLAYER_NAMES = new ArrayList<>();

    private final CommandSender sender;
    private final String[] buffer;

    private final List<String> suggestions = new ArrayList<>();

    public CommandSuggestion(CommandSender sender, String[] buffer) {
        this.sender = sender;
        this.buffer = buffer;
    }

    private void add(Collection<String> items) {
        suggestions.addAll(items);
    }

    public void suggest(int pos, String... suggestions) {
        if (this.buffer.length - 1 != pos) {
            return;
        }
        this.add(List.of(suggestions));
    }

    public void suggest(int pos, Collection<String> suggestions) {
        if (this.buffer.length - 1 != pos) {
            return;
        }
        this.add(suggestions);
    }

    public void suggestOnlinePlayers(int pos) {
        if (this.buffer.length - 1 != pos) {
            return;
        }
        this.add(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
    }

    public void suggestPlayers(int pos) {
        if (this.buffer.length - 1 != pos) {
            return;
        }

        if (ALL_OFFLINE_PLAYER_NAMES.isEmpty()) {
            ALL_OFFLINE_PLAYER_NAMES.addAll(Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList());
        }

        this.add(ALL_OFFLINE_PLAYER_NAMES);
    }


    public List<String> getSuggestions() {
        return suggestions;
    }

    public void requireAnyPermission(Consumer<CommandSuggestion> provider, Permission... permissions) {
        for (Permission permission : permissions) {
            if (this.sender.hasPermission(permission)) {
                provider.accept(this);
                return;
            }
        }
    }

    public void requireAllPermission(Consumer<CommandSuggestion> provider, Permission... permissions) {
        for (Permission permission : permissions) {
            if (!this.sender.hasPermission(permission)) {
                return;
            }
        }
        provider.accept(this);
    }

    public void matchArgument(int position, String item, Consumer<CommandSuggestion> action) {
        if (this.buffer.length - 1 < position) {
            return;
        }
        if (!this.buffer[position].equals(item)) {
            return;
        }
        action.accept(this);
    }

    public List<String> getBuffer() {
        return List.of(buffer);
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Player getSenderAsPlayer() {
        return ((Player) this.sender);
    }
}
