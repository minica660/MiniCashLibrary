package com.example.minicash.library.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigUpdate {

    /**
     * 外部APIを一切使わない Pure Java による設定ファイルの更新
     *
     * @param defaultConfigStream Jar内のデフォルト設定 (plugin.getResource(...)など)
     * @param targetFile          サーバー上の設定ファイル
     * @param ignoredSections     無視するセクションのリスト (例: "database", "messages")
     * @return 変更があった場合は true
     */
    public static boolean update(InputStream defaultConfigStream, File targetFile , List<String> ignoredSections) throws IOException {

        if (defaultConfigStream == null || !targetFile.exists()) {
            return false;
        }

        // 既存ファイルのキーなどを取得
        // database: mysql
        // この場合databaseがキーとなる
        List<String> existingLines = Files.readAllLines(targetFile.toPath(), StandardCharsets.UTF_8);
        Set<String> existingKeys = new HashSet<>();
        List<String> pathTracker = new ArrayList<>();

        for (String line : existingLines) {

            String trimmed = line.trim();

            if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains(":")) {
                continue;
            }

            int indent = line.indexOf(trimmed.charAt(0));
            int depth = indent / 2;
            String key = trimmed.split(":", 2)[0].trim();

            while (pathTracker.size() > depth){

                pathTracker.remove(pathTracker.size() - 1);

            }

            if (pathTracker.size() == depth){
                pathTracker.add(key);
            } else{
                pathTracker.set(depth, key);
            }

            existingKeys.add(String.join(".", pathTracker));
        }

        // デフォルト設定から差を取得

        List<String> linesToAdd = new ArrayList<>();
        pathTracker.clear();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String trimmed = line.trim();

                if (trimmed.startsWith("#") || !trimmed.contains(":")) {
                    continue;
                }

                int indent = line.indexOf(trimmed.charAt(0));
                int depth = indent / 2;
                String key = trimmed.split(":", 2)[0].trim();

                while (pathTracker.size() > depth) pathTracker.remove(pathTracker.size() - 1);
                if (pathTracker.size() == depth) pathTracker.add(key);
                else pathTracker.set(depth, key);

                String fullKey = String.join(".", pathTracker);

                // 無視セクションのチェック
                boolean isIgnored = false;
                if (ignoredSections != null) {
                    for (String ignored : ignoredSections) {
                        if (fullKey.equals(ignored) || fullKey.startsWith(ignored + ".")) {
                            isIgnored = true;
                            break;
                        }
                    }
                }

                // 存在しないキーであれば追記用リストに追加（Set#addで判定も同時に処理）
                if (!isIgnored && existingKeys.add(fullKey)) {
                    linesToAdd.add(line);
                }

            }

        }

        // 差があればファイルの最後にデータの追加
        if (!linesToAdd.isEmpty()) {
            existingLines.addAll(linesToAdd);
            Files.write(targetFile.toPath(), existingLines, StandardCharsets.UTF_8);
            return true;
        }

        return false;

    }
}
