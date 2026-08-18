package com.example.minicash.library.common.utils;

import com.example.minicash.library.common.response.NormalResponse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordMessage {

    private JDA jda;

    private final String token;
    private final String url;
    private final String channel;

    public DiscordMessage(String token, String url, String channel) {
        this.token = token;
        this.url = url;
        this.channel = channel;
    }


    public NormalResponse connectDiscordBot() {

        try {
            jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
//                    .addEventListeners(new DiscordListener(proxy,config,this,logger))
                    .build();
            jda.awaitReady();

            return new NormalResponse(true, "Discordの起動が完了しました");

        } catch (InterruptedException e) {
            return new NormalResponse(false, e.getMessage());
        }


    }

    public NormalResponse disconnectDiscordBot() {
        try {
            jda.shutdown();
            return new NormalResponse(true, "Discord BOTを無効化しました");
        } catch (Exception e) {
            return new NormalResponse(false, e.getMessage());
        }

    }

    public boolean sendMessage(String message) {

        try {

            TextChannel textChannel = jda.getTextChannelById(channel);
            if (textChannel != null) {
                textChannel.sendMessage(message).queue();
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }


}
