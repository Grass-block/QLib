package org.atcraftmc.qlib.audience;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.AudienceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface QLibAudienceProvider extends AudienceProvider {
    @Override
    @NotNull QLibAudience all();

    @Override
    @NotNull QLibAudience console();

    @Override
    @NotNull QLibAudience players();

    @Override
    @NotNull QLibAudience player(@NotNull UUID playerId);

    @Override
    default @NotNull QLibAudience permission(@NotNull Key permission) {
        return permission(permission.asString());
    }

    @Override
    @NotNull QLibAudience permission(@NotNull String permission);

    @Override
    @NotNull QLibAudience world(@NotNull Key world);

    @Override
    @NotNull QLibAudience server(@NotNull String serverName);
}
