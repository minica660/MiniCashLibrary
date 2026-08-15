package com.example.minicash.library.paper.listener;

import com.example.minicash.library.paper.MiniCashPaperLibrary;
import com.example.minicash.library.common.models.WarpResult;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class WarpResponsePluginMessage implements PluginMessageListener {

    private final Gson gson = new Gson();

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {

        if (!channel.equals(MiniCashPaperLibrary.WARP_CHANNEL)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if ("WarpResponse".equals(subChannel)) {

            String jsonResult = in.readUTF();
            WarpResult result = gson.fromJson(jsonResult, WarpResult.class);

            handleWarpResult(player, result);
        }

    }


    private void handleWarpResult(Player player, WarpResult result) {

        // ワープ先でキックされた場合
        if (result.getStatus() == WarpResult.Status.KICKED) {

            Component reasonComponent = GsonComponentSerializer.gson().deserialize(result.getRawMessage());

            player.sendMessage(
                    WarpResult.getWarpMessage(
                            Component.text( result.getWarpPointName() + "へのワープに失敗しました: ")
                                    .append(reasonComponent)
                    )
            );

            // ターゲットサーバーがオフライン等のエラー
        } else if (!result.isSuccess()) {
            player.sendMessage("§cワープエラー: " + result.getRawMessage());
        }
    }

}
