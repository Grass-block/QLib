package org.atcraftmc.qlib.bukkit.util;

import org.bukkit.entity.Player;

public class PlayerRef {
    private Player player;

    public PlayerRef handle(Player player) {
        this.player = player;
        return this;
    }

    public Player handle() {
        return player;
    }
}
