package org.atcraftmc.qlib.bukkit;

import me.gb2022.commons.reflect.method.MethodHandle;
import me.gb2022.commons.reflect.method.MethodHandleO1;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("Convert2MethodRef")
public interface BukkitTextAPI {
    MethodHandleO1<CommandSender, ComponentLike> SEND_MESSAGE = MethodHandle.select((ctx) -> {
        ctx.attempt(() -> Player.class.getMethod("sendMessage", Component.class), (p, c) -> p.sendMessage(c));
        ctx.attempt(
                () -> CommandSender.Spigot.class.getMethod("sendMessage", BaseComponent.class),
                (p, c) -> p.spigot().sendMessage(ComponentSerializer.bungee(c))
        );
        ctx.attempt(
                () -> CommandSender.class.getMethod("sendMessage", String.class),
                (p, c) -> p.sendMessage(ComponentSerializer.legacy(c))
        );
    });

    MethodHandleO1<Player, ComponentLike> SEND_ACTIONBAR = MethodHandle.select((ctx) -> {
        ctx.attempt(() -> Player.class.getMethod("sendActionBar", Component.class), (p, c) -> p.sendActionBar(c));
        ctx.attempt(() -> Player.class.getMethod("sendActionBar", BaseComponent[].class), (p, c) -> {
            var bc = ComponentSerializer.bungee(c);
            p.sendActionBar(bc);
        });
        ctx.attempt(() -> Player.class.getMethod("sendActionBar", BaseComponent[].class), (p, c) -> {
            var bc = ComponentSerializer.bungee(c);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, bc);
        });
        ctx.dummy((p, c) -> {});
    });

    static void sendActionbar(Player sender, ComponentLike message) {
        SEND_ACTIONBAR.invoke(sender, message);
    }

    static void sendMessage(CommandSender sender, ComponentLike component) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ComponentSerializer.legacy(component));
            return;
        }
        SEND_MESSAGE.invoke(sender, component);
    }
}
