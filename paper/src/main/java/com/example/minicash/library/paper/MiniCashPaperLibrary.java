package com.example.minicash.library.paper;

import com.example.minicash.library.paper.listener.WarpResponsePluginMessage;
import com.example.minicash.library.paper.utils.WarpUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniCashPaperLibrary extends JavaPlugin {

    public static final String WARP_CHANNEL = "minicash:warp";

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Velocityへのプラグインメッセージ送信用
        getServer().getMessenger().registerOutgoingPluginChannel(this, WARP_CHANNEL);

        // Velocityからのプラグインメッセージ受信
        getServer().getMessenger().registerIncomingPluginChannel(this, WARP_CHANNEL, new WarpResponsePluginMessage() );

        new WarpUtil(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getServer().getMessenger().unregisterOutgoingPluginChannel(this, WARP_CHANNEL);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, WARP_CHANNEL);

    }
}
