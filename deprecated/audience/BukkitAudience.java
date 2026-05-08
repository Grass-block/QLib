package org.atcraftmc.qlib.bukkit.audience;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.atcraftmc.qlib.platform.bukkit.AbstractAudience;
import org.atcraftmc.qlib.platform.bukkit.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import java.time.Duration;
import java.util.UUID;

public class BukkitAudience extends AbstractAudience {
    private Player pointer;

    @Override
    public void sendMessage(@NotNull Component message) {
        API.SEND_MESSAGE.invoke(this.pointer, message);
    }

    @Override
    public void deleteMessage(SignedMessage.@NotNull Signature signature) {
        API.DELETE_MESSAGE.invoke(this.pointer, signature);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        API.SEND_ACTIONBAR_TITLE.invoke(this.pointer, message);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        API.SET_TAB.invoke(this.pointer, header, footer);
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        API.SEND_RESOURCE_PACKS.invoke(this.pointer, request);
    }

    @Override
    public void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
        API.REMOVE_RESOURCE_PACKS.invoke(this.pointer, id, others);
    }

    @Override
    public void clearResourcePacks() {
        API.CLEAR_RESOURCE_PACK.invoke(this.pointer);
    }



    @Override
    public void clearTitle() {

    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {

    }

    @Override
    public void resetTitle() {

    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {

    }

    @Override
    public void playSound(@NotNull Sound sound) {

    }

    @Override
    public void playSound(@NotNull Sound sound, Sound.@NotNull Emitter emitter) {

    }

    @Override
    public void playSound(@NotNull Sound sound, double x, double y, double z) {

    }

    @Override
    public void stopSound(@NotNull SoundStop stop) {

    }

    @Override
    public void openBook(@NotNull Book book) {

    }




    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        this.pointer.sendTitlePart(part, value);
    }

    @SuppressWarnings("Convert2MethodRef")
    interface API {
        MethodHandleO2<Player, Component, Component> SET_TAB = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("sendPlayerListHeader", Component.class),
                        (p, c1, c2) -> p.sendPlayerListHeaderAndFooter(c1, c2)
            );
            ctx.attempt(() -> Player.class.getMethod("setPlayerListHeaderFooter", BaseComponent.class, BaseComponent.class),
                        (p, c1, c2) -> {
                            var cc1 = ComponentSerializer.bungee(c1);
                            var cc2 = ComponentSerializer.bungee(c2);
                            p.setPlayerListHeaderFooter(cc1, cc2);
                        }
            );
            ctx.attempt(() -> Player.class.getMethod("setPlayerListHeader", String.class), (p, c1, c2) -> {
                p.setPlayerListHeader(ComponentSerializer.legacy(c1));
                p.setPlayerListFooter(ComponentSerializer.legacy(c2));
            });
        });
        MethodHandleO1<CommandSender, ComponentLike> SEND_MESSAGE = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("sendMessage", Component.class), (p, c) -> p.sendMessage(c));
            ctx.attempt(() -> CommandSender.Spigot.class.getMethod("sendMessage", BaseComponent.class),
                        (p, c) -> p.spigot().sendMessage(ComponentSerializer.bungee(c))
            );
            ctx.attempt(() -> CommandSender.class.getMethod("sendMessage", String.class),
                        (p, c) -> p.sendMessage(ComponentSerializer.legacy(c))
            );
        });
        MethodHandleO1<Player, SignedMessage.Signature> DELETE_MESSAGE = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("deleteMessage", SignedMessage.Signature.class), (p, s) -> p.deleteMessage(s));
            ctx.dummy((p, c) -> {});
        });

        MethodHandleO1<Player, ResourcePackRequest> SEND_RESOURCE_PACKS = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("sendResourcePacks", ResourcePackRequest.class), (p, r) -> p.sendResourcePacks(r));
            ctx.dummy((p, r) -> {});
        });
        MethodHandleO2<Player, UUID, UUID[]> REMOVE_RESOURCE_PACKS = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("removeResourcePacks", UUID.class, UUID[].class),
                        (p, u, o) -> p.removeResourcePacks(u, o)
            );
            ctx.dummy((p, u, o) -> {});
        });
        MethodHandleO0<Player> CLEAR_RESOURCE_PACK = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("clearResourcePacks"), (p) -> p.clearResourcePacks());
            ctx.dummy((p) -> {});
        });

        MethodHandleO1<Player, Title> TITLE = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("showTitle", Title.class), (p, r) -> p.sendResourcePacks(r));
            ctx.dummy((p, r) -> {});
        });

        MethodHandleO2<Player, TitlePart<?>, Object> TITLE = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("showTitle", Title.class), (p, t, s, v) -> {
                var in = Duration.ofMillis(v.x() * 50L);
                var stay = Duration.ofMillis(v.y() * 50L);
                var out = Duration.ofMillis(v.z() * 50L);
                var time = Title.Times.times(in, stay, out);

                p.showTitle(Title.title(t.asComponent(), s.asComponent(), time));
            });
        });

        MethodHandleO3<Player, ComponentLike, ComponentLike, Vector3i> SEND_TITLE = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("showTitle", Title.class), (p, t, s, v) -> {
                var in = Duration.ofMillis(v.x() * 50L);
                var stay = Duration.ofMillis(v.y() * 50L);
                var out = Duration.ofMillis(v.z() * 50L);
                var time = Title.Times.times(in, stay, out);

                p.showTitle(Title.title(t.asComponent(), s.asComponent(), time));
            });
            ctx.dummy((p, t, s, v) -> {
                var title = ComponentSerializer.legacy(t);
                var subtitle = ComponentSerializer.legacy(s);

                p.sendTitle(title, subtitle, v.x(), v.y(), v.z());
            });
        });
        MethodHandleO1<Player, ComponentLike> SEND_ACTIONBAR_TITLE = MethodHandle.select((ctx) -> {
            ctx.attempt(() -> Player.class.getMethod("sendActionBar", Component.class), (p, c) -> p.sendActionBar(c));
            ctx.attempt(() -> Player.class.getMethod("sendActionBar", BaseComponent[].class), (p, c) -> {
                var bc = ComponentSerializer.bungee(c);
                p.sendActionBar(bc);
            });
            ctx.attempt(() -> Player.class.getMethod("spigot"), (p, c) -> {
                var bc = ComponentSerializer.bungee(c);
                p.sendMessage(ChatMessageType.ACTION_BAR, bc);
            });
        });
    }
}
