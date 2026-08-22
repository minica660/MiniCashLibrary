package com.example.minicash.library.paper.utils;

import com.example.minicash.library.common.utils.bossbar.BossBarColor;
import com.example.minicash.library.common.utils.bossbar.BossBarStyle;
import com.example.minicash.library.common.utils.bossbar.CustomBossbar;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class PaperBossbarUtil implements CustomBossbar {

    private final Player player;
    private final BossBar bossBar;

    public PaperBossbarUtil(Player player, String title, BossBarColor color, BossBarStyle style) {

        this.player = player;
        this.bossBar = BossBar.bossBar(

                Component.text(title),
                1.0f,
                convertColor(color),
                convertStyle(style)

        );

    }

    @Override
    public void setTitle(String title) {
        bossBar.name(Component.text(title));
    }

    @Override
    public void setProgress(float progress) {
        bossBar.progress(Math.clamp(progress, 0.0f, 1.0f));
    }

    @Override
    public void setColor(BossBarColor color) {
        bossBar.color(convertColor(color));
    }

    @Override
    public void setStyle(BossBarStyle style) {
        bossBar.overlay(convertStyle(style));
    }

    @Override
    public void show() {
        player.showBossBar(bossBar);
    }

    @Override
    public void hide() {
        player.hideBossBar(bossBar);
    }


    private BossBar.Color convertColor(BossBarColor color) {
        return BossBar.Color.valueOf(color.name());
    }

    private BossBar.Overlay convertStyle(BossBarStyle style) {
        return BossBar.Overlay.valueOf(style.name());
    }
}
