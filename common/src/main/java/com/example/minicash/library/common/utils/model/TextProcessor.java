package com.example.minicash.library.common.utils.model;

import net.kyori.adventure.text.Component;

public interface TextProcessor {

    Component process(String value);

    String Restore(Component value);

    String toPlainText(Component component);

    Component parseMiniMessage(String input);

    String toMiniMessage(Component component);

}
