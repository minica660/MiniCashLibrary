package com.example.minicash.library.common.utils.bossbar;

public interface CustomBossbar {
    void setTitle(String title);
    void setProgress(float progress); // 0.0f - 1.0f
    void setColor(BossBarColor color);
    void setStyle(BossBarStyle style);

    void show();
    void hide();
}
