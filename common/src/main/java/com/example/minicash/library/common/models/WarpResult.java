package com.example.minicash.library.common.models;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class WarpResult {

    public enum Status {
        SUCCESS,
        TARGET_OFFLINE,
        KICKED,
        ERROR
    }


    private final Status status;
    private final String rawMessage;
    private final String warpPointName;

    public WarpResult(Status status, String rawMessage, String warpPointName) {
        this.status = status;
        this.rawMessage = rawMessage != null ? rawMessage : "";
        this.warpPointName = warpPointName != null ? warpPointName : "";
    }

    public static WarpResult success(String warpPointName) {
        return new WarpResult(Status.SUCCESS, "", warpPointName);
    }

    public static WarpResult targetOffline(String serverName, String warpPointName) {
        return new WarpResult(Status.TARGET_OFFLINE, "Target server '" + serverName + "' is offline.", warpPointName);
    }

    public static WarpResult kicked(String rawMessage, String warpPointName) {
        return new WarpResult(Status.KICKED, rawMessage, warpPointName);
    }

    public static WarpResult error(String message, String warpPointName) {
        return new WarpResult(Status.ERROR, message, warpPointName);
    }

    public Status getStatus() {
        return status;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public String getWarpPointName() {
        return warpPointName;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }


    public static Component getWarpMessage(Component component) {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("MapleWarp").color(NamedTextColor.GOLD)
                        .append(Component.text("]").color(NamedTextColor.GRAY))
                );
    }

}
