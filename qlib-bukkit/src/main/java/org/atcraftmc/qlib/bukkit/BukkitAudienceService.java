package org.atcraftmc.qlib.bukkit;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import org.atcraftmc.qlib.audience.*;
import org.atcraftmc.qlib.text.TextEngine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Collectors;

public abstract class BukkitAudienceService extends AudienceService<CommandSender> implements Listener {
    private final Plugin plugin;
    private final PointedForwardingAudience all = new PointedForwardingAudience();
    private final PointedForwardingAudience allPlayers = new PointedForwardingAudience();
    private final PointedAudience console = get(Bukkit.getConsoleSender());

    public BukkitAudienceService(Plugin plugin, TextEngine textEngine) {
        super(textEngine);

        this.plugin = plugin;

        for (var player : Bukkit.getOnlinePlayers()) {
            var a = get(player);
            this.all.add(a);
            this.allPlayers.add(a);
        }

        this.all.add(this.console);
    }

    @SuppressWarnings("ConstantValue")
    static BukkitAudienceService create(Plugin plugin, TextEngine textEngine) {
        if (Bukkit.getConsoleSender() instanceof Audience) {
            return new NativeBukkitAudienceService(plugin, textEngine);
        }

        return new WrappedBukkitAudienceService(plugin, textEngine);
    }

    public void init(){
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void close() {
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    @Override
    public AudienceSource source(CommandSender pointer) {
        if (pointer instanceof Player) {
            return AudienceSource.PLAYER;
        }
        if (pointer instanceof ConsoleCommandSender) {
            return AudienceSource.CONSOLE;
        }
        return AudienceSource.OTHER;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var a = get(event.getPlayer());
        this.all.add(a);
        this.allPlayers.add(a);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var a = get(event.getPlayer());
        this.all.remove(a);
        this.allPlayers.remove(a);
    }


    @Override
    public @NotNull QLibAudience all() {
        return this.all;
    }

    @Override
    public @NotNull QLibAudience console() {
        return this.console;
    }

    @Override
    public @NotNull QLibAudience players() {
        return this.allPlayers;
    }

    @Override
    public @NotNull QLibAudience player(@NotNull UUID playerId) {
        return get(Bukkit.getPlayer(playerId));
    }

    @Override
    public @NotNull QLibAudience permission(@NotNull String permission) {
        return new PointedForwardingAudience(this.all.getAudiences()
                                                     .stream()
                                                     .filter((a) -> a.getPointer(CommandSender.class).hasPermission(permission))
                                                     .collect(Collectors.toSet()));
    }

    @Override
    public @NotNull QLibAudience world(@NotNull Key world) {
        return new PointedForwardingAudience(this.allPlayers.getAudiences()
                                                     .stream()
                                                     .filter((a) -> a.getPointer(Player.class)
                                                             .getWorld()
                                                             .getName()
                                                             .equals(world.asMinimalString()))
                                                     .collect(Collectors.toSet()));
    }

    @Override
    public @NotNull QLibAudience server(@NotNull String serverName) {
        return this.all;
    }

    private static final class WrappedBukkitAudienceService extends BukkitAudienceService {
        private final BukkitAudiences audiences;

        public WrappedBukkitAudienceService(Plugin plugin, TextEngine textEngine) {
            super(plugin, textEngine);
            this.audiences = BukkitAudiences.create(plugin);
        }

        @Override
        public Audience wrap(CommandSender pointer) {
            return this.audiences.sender(pointer);
        }

        @Override
        public @NotNull ComponentFlattener flattener() {
            return this.audiences.flattener();
        }

        @Override
        public void close() {
            super.close();
            this.audiences.close();
        }
    }

    private static final class NativeBukkitAudienceService extends BukkitAudienceService {
        public NativeBukkitAudienceService(Plugin plugin, TextEngine textEngine) {
            super(plugin, textEngine);
        }

        @Override
        public Audience wrap(CommandSender pointer) {
            return pointer;
        }

        @Override
        public @NotNull ComponentFlattener flattener() {
            throw new IllegalArgumentException("WHY?");
        }
    }
}
