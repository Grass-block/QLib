package org.atcraftmc.qlib.bukkit.audience;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackRequestLike;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractAudience implements Audience {
    public void sendMessage(final @NotNull ComponentLike message) {
        this.sendMessage(message.asComponent());
    }

    public void sendMessage(final @NotNull Identified source, final @NotNull ComponentLike message) {
        this.sendMessage(source, message.asComponent());
    }

    public void sendMessage(final @NotNull Identity source, final @NotNull ComponentLike message) {
        this.sendMessage(source, message.asComponent());
    }

    public void sendMessage(final @NotNull ComponentLike message, final ChatType.@NotNull Bound boundChatType) {
        this.sendMessage(message.asComponent(), boundChatType);
    }

    public void sendMessage(final @NotNull SignedMessage signedMessage, final ChatType.@NotNull Bound boundChatType) {
        this.sendMessage(signedMessage.unsignedContent() != null ? signedMessage.unsignedContent() : Component.text(signedMessage.message()));
    }

    public void deleteMessage(final @NotNull SignedMessage signedMessage) {
        if (signedMessage.canDelete()) {
            this.deleteMessage(Objects.requireNonNull(signedMessage.signature()));
        }
    }

    public void sendActionBar(final @NotNull ComponentLike message) {
        this.sendActionBar(message.asComponent());
    }

    public void sendPlayerListHeader(final @NotNull ComponentLike header) {
        this.sendPlayerListHeader(header.asComponent());
    }

    public void sendPlayerListHeader(final @NotNull Component header) {
        this.sendPlayerListHeaderAndFooter(header, Component.empty());
    }

    public void sendPlayerListFooter(final @NotNull ComponentLike footer) {
        this.sendPlayerListFooter(footer.asComponent());
    }

    public void sendPlayerListFooter(final @NotNull Component footer) {
        this.sendPlayerListHeaderAndFooter(Component.empty(), footer);
    }

    public void sendPlayerListHeaderAndFooter(final @NotNull ComponentLike header, final @NotNull ComponentLike footer) {
        this.sendPlayerListHeaderAndFooter(header.asComponent(), footer.asComponent());
    }

    public void showTitle(final @NotNull Title title) {
        Title.Times times = title.times();
        if (times != null) {
            this.sendTitlePart(TitlePart.TIMES, times);
        }

        this.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
        this.sendTitlePart(TitlePart.TITLE, title.title());
    }

    public void stopSound(final @NotNull Sound sound) {
        this.stopSound((Objects.requireNonNull(sound, "sound")).asStop());
    }

    public void openBook(final Book.@NotNull Builder book) {
        this.openBook(book.build());
    }

    public void sendResourcePacks(final @NotNull ResourcePackInfoLike first, final ResourcePackInfoLike... others) {
        this.sendResourcePacks(ResourcePackRequest.addingRequest(first, others));
    }

    public void sendResourcePacks(final @NotNull ResourcePackRequestLike request) {
        this.sendResourcePacks(request.asResourcePackRequest());
    }

    public void removeResourcePacks(final @NotNull ResourcePackRequestLike request) {
        this.removeResourcePacks(request.asResourcePackRequest());
    }

    public void removeResourcePacks(final @NotNull ResourcePackRequest request) {
        List<ResourcePackInfo> infos = request.packs();
        if (infos.size() == 1) {
            this.removeResourcePacks((infos.get(0)).id());
        } else if (infos.isEmpty()) {
            return;
        }

        UUID[] otherReqs = new UUID[infos.size() - 1];

        for (int i = 0; i < otherReqs.length; ++i) {
            otherReqs[i] = (infos.get(i + 1)).id();
        }

        this.removeResourcePacks((infos.get(0)).id(), otherReqs);
    }

    public void removeResourcePacks(final @NotNull ResourcePackInfoLike request, final @NotNull ResourcePackInfoLike... others) {
        UUID[] otherReqs = new UUID[others.length];

        for (int i = 0; i < others.length; ++i) {
            otherReqs[i] = others[i].asResourcePackInfo().id();
        }

        this.removeResourcePacks(request.asResourcePackInfo().id(), otherReqs);
    }

    public void removeResourcePacks(final @NotNull Iterable<UUID> ids) {
        Iterator<UUID> it = ids.iterator();
        if (it.hasNext()) {
            UUID id = (UUID) it.next();
            UUID[] others;
            if (!it.hasNext()) {
                others = new UUID[0];
            } else if (ids instanceof Collection) {
                others = new UUID[((Collection<?>) ids).size() - 1];

                for (int i = 0; i < others.length; ++i) {
                    others[i] = it.next();
                }
            } else {
                List<UUID> othersList = new ArrayList<>();

                while (it.hasNext()) {
                    othersList.add(it.next());
                }

                others = (UUID[]) othersList.toArray(new UUID[0]);
            }

            this.removeResourcePacks(id, others);
        }
    }


    public abstract void deleteMessage(final SignedMessage.@NotNull Signature signature);

    public abstract void sendMessage(final @NotNull Component message);

    public abstract void sendActionBar(final @NotNull Component message);

    public abstract void sendPlayerListHeaderAndFooter(final @NotNull Component header, final @NotNull Component footer);

    public abstract <T> void sendTitlePart(final @NotNull TitlePart<T> part, final @NotNull T value);

    public abstract void clearTitle();

    public abstract void resetTitle();

    public abstract void showBossBar(final @NotNull BossBar bar);

    public abstract void hideBossBar(final @NotNull BossBar bar);

    public abstract void playSound(final @NotNull Sound sound);

    public abstract void playSound(final @NotNull Sound sound, final double x, final double y, final double z);

    public abstract void playSound(final @NotNull Sound sound, final Sound.@NotNull Emitter emitter);

    public abstract void stopSound(final @NotNull SoundStop stop);

    public abstract void openBook(final @NotNull Book book);

    public abstract void sendResourcePacks(final @NotNull ResourcePackRequest request);

    public abstract void removeResourcePacks(final @NotNull UUID id, final @NotNull UUID @NotNull ... others);

    public abstract void clearResourcePacks();
}
