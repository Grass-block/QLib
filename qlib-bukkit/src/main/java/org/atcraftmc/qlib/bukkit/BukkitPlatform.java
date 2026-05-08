package org.atcraftmc.qlib.bukkit;

import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.platform.PluginPlatform;
import org.atcraftmc.qlib.platform.PluginPlatformStack;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Function;


public final class BukkitPlatform implements PluginPlatform {
    public static final PluginPlatform DEFAULT = new BukkitPlatform();

    public static void init() {
        PluginPlatform.GLOBAL.set(new PluginPlatformStack(BukkitPlatform.DEFAULT));
    }

    @Override
    public MinecraftLocale locale(Object sender) {
        return MinecraftLocale.minecraft((sender instanceof Player) ? ((Player) sender).getLocale() : "en_us");
    }

    @Override
    public void sendMessage(Object pointer, ComponentLike message) {
        BukkitTextAPI.sendMessage(((CommandSender) pointer), examineComponent(message, pointer, locale(pointer)));
    }

    @Override
    public void broadcastLine(Function<MinecraftLocale, ComponentLike> component, boolean opOnly, boolean toConsole) {
        for (var p : Bukkit.getOnlinePlayers()) {
            if (!p.isOp() && opOnly) {
                continue;
            }
            sendMessage(p, component.apply(locale(p)));
        }
        if (!toConsole) {
            return;
        }
        CommandSender sender = Bukkit.getConsoleSender();
        sendMessage(sender, component.apply(MinecraftLocale.EN_US));
    }

    @Override
    public String pluginsFolder() {
        return System.getProperty("user.dir") + "/plugins";
    }
}
