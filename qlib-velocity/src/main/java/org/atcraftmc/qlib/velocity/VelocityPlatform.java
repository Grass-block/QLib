package org.atcraftmc.qlib.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.ComponentLike;
import org.atcraftmc.qlib.language.MinecraftLocale;
import org.atcraftmc.qlib.platform.PluginPlatform;
import org.atcraftmc.qlib.platform.PluginPlatformStack;

import java.util.function.Function;

public final class VelocityPlatform implements PluginPlatform {
    private final ProxyServer server;

    public VelocityPlatform(ProxyServer server) {
        this.server = server;
    }

    public static void init(ProxyServer server) {
        PluginPlatform.GLOBAL.set(new PluginPlatformStack(new VelocityPlatform(server)));
    }

    @Override
    public void sendMessage(Object pointer, ComponentLike message) {
        ((Player) pointer).sendMessage(message);
    }

    @Override
    public MinecraftLocale locale(Object sender) {
        return MinecraftLocale.locale(((Player) sender).getEffectiveLocale());
    }

    @Override
    public void broadcastLine(Function<MinecraftLocale, ComponentLike> component, boolean opOnly, boolean toConsole) {
        this.server.getAllPlayers().forEach(player -> player.sendMessage(component.apply(locale(player))));
    }

    @Override
    public String pluginsFolder() {
        return System.getProperty("user.dir") + "/plugins";
    }
}
