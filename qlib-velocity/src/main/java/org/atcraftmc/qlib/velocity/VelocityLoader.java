package org.atcraftmc.qlib.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "qlib-velocity", version = "1.0", authors = "GrassBlock2022", description = "qlib loader for velocity plugins.")
public class VelocityLoader extends VelocityPluginConcept {
    private final ProxyServer server;

    @Inject
    public VelocityLoader(ProxyServer server) {
        this.server = server;
        this.logger().info("set global platform to stack<VELOCITY_DEFAULT>.");
        VelocityPlatform.init(server);
    }

    @Override
    public String id() {
        return "qlib-velocity";
    }

    @Override
    public String configId() {
        return "__";
    }

    public ProxyServer getServer() {
        return server;
    }
}
