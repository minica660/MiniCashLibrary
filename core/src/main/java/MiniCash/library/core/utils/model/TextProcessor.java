package MiniCash.library.core.utils.model;

import net.kyori.adventure.text.Component;

public interface TextProcessor {

    Component process(String value);

    String Restore(Component value);

    Component parseMiniMessage(String input);

    String toMiniMessage(Component component);

}
