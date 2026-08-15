package com.example.minicash.library.paper;


import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExampleConfigUpdate {

    /*
        例 ConfigUpdateのupdateメソッド使い方！
     */
    public static void configUpdate(JavaPlugin plugin,List<String> noUpdateSections) {

        plugin.saveDefaultConfig();

        File configFile = new File(plugin.getDataFolder(), "config.yml");

        InputStream defaultConfigStream = plugin.getResource("config.yml");

        if (defaultConfigStream == null) {
            return;
        }

        // 更新から除外したいキー名をList型で入力

//        List<String> noUpdateSections = Arrays.asList("database", "messages");


        try {

            boolean updated = com.example.minicash.library.common.utils.ConfigUpdate.update(defaultConfigStream, configFile, noUpdateSections);

            if (updated) {
                plugin.getLogger().info("config.yml に新しい設定項目を追加しました！");
                plugin.reloadConfig();
                plugin.saveConfig();
            } else {
                plugin.getLogger().info("config.yml は最新の状態です。");
            }

        } catch (IOException e) {
            plugin.getLogger().severe("config.yml の更新中にエラーが発生しました。");
        }

    }

}
