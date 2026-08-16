package com.example.minicash.library.paper.utils;

import com.example.minicash.library.paper.MiniCashPaperLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class WarpUtil {

    private static MiniCashPaperLibrary miniCashPaperLibrary;

    public WarpUtil(MiniCashPaperLibrary miniCashPaperLibrary) {
        WarpUtil.miniCashPaperLibrary = miniCashPaperLibrary;
    }

    public static void sendWarpRequest(Player player, String targetServer, String warpPointName) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("RequestWarp");
        out.writeUTF(targetServer);
        out.writeUTF(warpPointName);

        player.sendPluginMessage(miniCashPaperLibrary, MiniCashPaperLibrary.WARP_CHANNEL, out.toByteArray());
    }
}
