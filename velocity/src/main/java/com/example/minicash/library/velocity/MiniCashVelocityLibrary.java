package com.example.minicash.library.velocity;

import com.example.minicash.library.velocity.listener.WarpListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

@Plugin(
        id = "minicash-library-velocity",
        name = "MiniCashVelocityLibrary",
        version = "1.0.5"
)
public final class MiniCashVelocityLibrary {

    public static final MinecraftChannelIdentifier WARP_CHANNEL =
            MinecraftChannelIdentifier.from("minicash:warp");

    private final ProxyServer proxy;
    private Logger logger;

    @Inject
    public MiniCashVelocityLibrary(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Plugin initialization logic goes here

        proxy.getChannelRegistrar().register(WARP_CHANNEL);

        proxy.getEventManager().register(this, new WarpListener(proxy,this));

        logger.info("MiniCashVelocityLibrary Plugin Loaded!");
    }
}
