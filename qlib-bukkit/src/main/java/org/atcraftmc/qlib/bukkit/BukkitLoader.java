package org.atcraftmc.qlib.bukkit;

public final class BukkitLoader extends BukkitPluginConcept {

    @Override
    public void onLoad() {
        this.logger().info("set global platform to stack<BUKKIT_DEFAULT>.");
        BukkitPlatform.init();
    }
}
