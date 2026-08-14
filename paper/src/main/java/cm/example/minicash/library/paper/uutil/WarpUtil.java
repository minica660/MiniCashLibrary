package cm.example.minicash.library.paper.uutil;

import cm.example.minicash.library.paper.MiniCashPaperLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class WarpUtil {
    public static void sendWarpRequest(Player player, String targetServer, String warpPointName, MiniCashPaperLibrary plugin) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("RequestWarp");
        out.writeUTF(targetServer);
        out.writeUTF(warpPointName);

        player.sendPluginMessage(plugin, MiniCashPaperLibrary.WARP_CHANNEL, out.toByteArray());
    }
}
