package com.example.minicash.library.velocity.listener;

import com.example.minicash.library.common.models.WarpResult;
import com.example.minicash.library.velocity.MiniCashVelocityLibrary;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WarpListener {

    private final ProxyServer proxy;
    private final Gson gson = new Gson();

    private final Map<UUID, String> pendingWarpPoints = new ConcurrentHashMap<>();

    public WarpListener(ProxyServer proxy) {
        this.proxy = proxy;
    }


    /*
        Paperからのワープリクエストプラグインメッセージ受信
     */
    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {

        if (!event.getIdentifier().equals(MiniCashVelocityLibrary.WARP_CHANNEL)){
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();

        if ("RequestWarp".equals(subChannel)) {

            String targetServerName = in.readUTF();
            String warpPointName = in.readUTF();

            if (event.getTarget() instanceof Player player) {

                Optional<RegisteredServer> targetServer = proxy.getServer(targetServerName);

                if (targetServer.isEmpty()) {

                    // サーバーが存在しないかオフラインの場合
                    WarpResult result = WarpResult.targetOffline(targetServerName, warpPointName);
                    sendWarpResultToPaper(player, result);
                    return;
                }

                // キック時に参照できるよう一時保存
                pendingWarpPoints.put(player.getUniqueId(), warpPointName);

                // ターゲットサーバーへの転送
                player.createConnectionRequest(targetServer.get()).connect().thenAccept(result -> {
                    if (result.isSuccessful()) {
                        sendExecuteTeleportToTarget(player, warpPointName);
                        pendingWarpPoints.remove(player.getUniqueId());
                    } else {
                        pendingWarpPoints.remove(player.getUniqueId());
                        WarpResult warpResult = WarpResult.targetOffline(targetServerName, warpPointName);
                        sendWarpResultToPaper(player, warpResult);
                    }
                });


            }


        }

    }


    /*
        サーバー転送でキックされた場合
     */
    @Subscribe
    public void kicked(KickedFromServerEvent event) {

        Player player = event.getPlayer();

        // 一時保存していたワープポイント名を取得する
        String warpPointName = pendingWarpPoints.remove(player.getUniqueId());

        if (warpPointName == null){
            warpPointName = "";
        }

        // 初期接続ではなく、ワープ中のサーバー移動によるキックかどうかの判定
        if (event.getServer().equals(player.getCurrentServer().orElse(null))) {
            return;
        }

        // キック理由の取得
        String rawKickReason = event.getServerKickReason()
                .map(reason -> GsonComponentSerializer.gson().serialize(reason))
                .orElse("Kicked with no reason provided.");

        // プロキシに表示されるエラー表示をされないようにして、元のサーバーにとどまらせる
        event.setResult(KickedFromServerEvent.Notify.create(
                net.kyori.adventure.text.Component.empty()
        ));

        // 元いたサーバーにWarpResultを送信
        WarpResult warpResult = WarpResult.kicked(rawKickReason, warpPointName);
        sendWarpResultToPaper(player, warpResult);

    }


    /**
     * 転送成功後、移動先のPaperサーバーへテレポート指示を送る
     */
    private void sendExecuteTeleportToTarget(Player player, String warpPointName) {
        player.getCurrentServer().ifPresent(serverConnection -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ExecuteTeleport");
            out.writeUTF(warpPointName);

            serverConnection.sendPluginMessage(MiniCashVelocityLibrary.WARP_CHANNEL, out.toByteArray());
        });
    }

    /**
     * Paper側へ結果を送り返す
     */
    private void sendWarpResultToPaper(Player player, WarpResult result) {

        player.getCurrentServer().ifPresent(serverConnection -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("WarpResponse");
            out.writeUTF(gson.toJson(result));

            serverConnection.sendPluginMessage(MiniCashVelocityLibrary.WARP_CHANNEL, out.toByteArray());

        });
    }


}
