package com.example.minicash.library.common.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * Minecraft のテキスト装飾・カラーコード変換を行うユーティリティクラス。
 * <p>
 * レガシー形式（{@code §} や {@code &}）および MiniMessage 形式と
 * Adventure {@link Component} との相互変換を提供します。
 * </p>
 */
public class ColorUtil {

    private static final LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * セクション記号（{@code §}）を含む文字列を {@link Component} に変換します。
     *
     * @param input 変換対象の文字列（例: {@code "§aHello §cWorld"}）
     * @return 変換された {@link Component}。引数が null または空文字の場合は {@link Component#empty()}
     */
    public static Component process(String input) {

        if (input == null || input.isEmpty()) {
            return Component.empty();
        }
        return SECTION_SERIALIZER.deserialize(input);
    }

    /**
     * {@link Component} をセクション記号（{@code §}）を含む文字列に変換（逆変換）します。
     *
     * @param component 変換対象の {@link Component}
     * @return {@code §} カラーコード付きの文字列。引数が null の場合は空文字 {@code ""}
     */
    public static String Restore(Component component) {
        if (component == null) {
            return "";
        }
        return SECTION_SERIALIZER.serialize(component);
    }


    /**
     * {@link Component} を文字列に変換（§なし）します
     *
     * @param component 変換対象の {@link Component}
     * @return {@code §} 文字列 引数が null の場合は空文字 {@code ""}
     */
    public static String toPlainText(Component component) {
        if (component == null) {
            return "";
        }
        return PlainTextComponentSerializer.plainText().serialize(component);    }

    /**
     * セクション記号（{@code §}）を含むレガシー文字列を MiniMessage 形式の文字列に変換します。
     *
     * @param input 変換対象のレガシー文字列（例: {@code "§aHello §cWorld"}）
     * @return MiniMessage タグ付きの文字列。引数が null または空文字の場合は空文字 {@code ""}
     */
    public static String sectionToMiniMessage(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        Component component = SECTION_SERIALIZER.deserialize(input);
        return MINI_MESSAGE.serialize(component);
    }

    /**
     * MiniMessage 形式のタグを含む文字列を {@link Component} に変換する用
     *
     * @param input 変換対象の文字列(例: {@code "<green>Hello <red>World"}）
     * @return 変換された {@link Component}。引数が null または空文字の場合は {@link Component#empty()}
     */
    public static Component parseMiniMessage(String input) {
        if (input == null || input.isEmpty()) {
            return Component.empty();
        }
        return MINI_MESSAGE.deserialize(input);
    }

    /**
     * {@link Component} をMiniMessage 形式のタグ文字列に変換するよう
     *
     * @param component 変換対象の {@link Component}
     * @return MiniMessageタグ付きの文字列 引数が null の場合は空文字 {@code ""}
     */
    public static String toMiniMessage(Component component) {
        if (component == null) {
            return "";
        }
        return MINI_MESSAGE.serialize(component);
    }


}
