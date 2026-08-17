package com.example.minicash.library.velocity.listener;

import com.example.minicash.library.velocity.MiniCashVelocityLibrary;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class WarpListener {

    private final MiniCashVelocityLibrary plugin;
    private final ProxyServer proxy;
    private final Gson gson = new Gson();

    private final Map<UUID, String> pendingWarpPoints = new ConcurrentHashMap<>();

    public WarpListener(ProxyServer proxy, MiniCashVelocityLibrary plugin) {
        this.proxy = proxy;
        this.plugin = plugin;
    }


    /*
        Paperからのワープリクエストプラグインメッセージ受信
     */
    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(MiniCashVelocityLibrary.WARP_CHANNEL)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();

        if ("RequestWarp".equals(subChannel)) {
            String targetServerName = in.readUTF();
            String warpPointName = in.readUTF();

            if (event.getTarget() instanceof Player player) {

//                player.sendMessage(
//                        Component.text("PluginMessage:最初")
//                );

                Optional<RegisteredServer> targetServers = proxy.getServer(targetServerName);

                if (targetServers.isEmpty()) {
                    player.sendMessage(getKickReasonMessage(
                            "指定されたワープポイントのサーバーが発見できませんでした"
                    ));
                    return;
                }

                pendingWarpPoints.put(player.getUniqueId(), warpPointName);

                RegisteredServer targetServer = targetServers.get();
//
//                player.sendMessage(
//                        Component.text("ターゲットサーバー取得")
//                );

                player.createConnectionRequest(targetServer).connect().whenComplete((result, throwable) -> {

                    // 失敗または例外発生時
                    if (throwable != null || (result != null && !result.isSuccessful())) {

                        if (pendingWarpPoints.remove(player.getUniqueId()) != null) {

                            String reasonText = "サーバーがオフラインです";

                            if (result != null) {
                                Component reasonComponent = result.getReasonComponent().orElse(null);
                                if (reasonComponent != null) {
                                    String parsed = PlainTextComponentSerializer.plainText().serialize(reasonComponent);
                                    if (!parsed.isBlank()) {
                                        reasonText = parsed;
                                    }
                                }
                            }

                            player.sendMessage(getKickReasonMessage(reasonText));


                        }


                    }
                });
            }
        }
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();

        if (pendingWarpPoints.containsKey(player.getUniqueId())) {


            String warpPointName = pendingWarpPoints.remove(player.getUniqueId());


            proxy.getScheduler().buildTask(plugin, () -> {

                if (warpPointName != null) {

//                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
//                    out.writeUTF("ExecuteTeleport");
//                    out.writeUTF(warpPointName);
//
//                    event.getServer().sendPluginMessage(MiniCashVelocityLibrary.WARP_CHANNEL, out.toByteArray());
                    player.getCurrentServer().ifPresent(serverConnection -> {

                        if (serverConnection.getServerInfo().equals(event.getServer().getServerInfo())) {

                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("ExecuteTeleport");
                            out.writeUTF(warpPointName);

                            serverConnection.sendPluginMessage(MiniCashVelocityLibrary.WARP_CHANNEL, out.toByteArray());
                        }
                    });
                }


            }).delay(750, java.util.concurrent.TimeUnit.MILLISECONDS).schedule();


        }


    }

    @Subscribe
    public void kicked(KickedFromServerEvent event) {
        Player player = event.getPlayer();

        if (!event.kickedDuringServerConnect()) {
            return;
        }

//        player.sendMessage(
//                Component.text("KickedFromServerEvent発生")
//        );

        String warpPointName = pendingWarpPoints.remove(player.getUniqueId());

        if (warpPointName != null) {

            String kickReason = event.getServerKickReason()
                    .map(component -> PlainTextComponentSerializer.plainText().serialize(component))
                    .orElse("");

            Component finalKickReason;

            if (kickReason == null || kickReason.isEmpty()) {

                finalKickReason = getKickReasonMessage("不明なエラーが発生しました");

            } else {
                finalKickReason = getKickReasonMessage(kickReason);
            }

            event.setResult(KickedFromServerEvent.Notify.create(finalKickReason));

        }


    }


    private Component getKickReasonMessage(String message) {

        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("MapleWarp").color(NamedTextColor.GOLD)
                        .append(Component.text("]", NamedTextColor.GRAY)
                                .append(Component.text("サーバー接続に失敗しました : " + message).color(NamedTextColor.RED)))
                );
    }
}

