package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ColorUtils {

    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    private final List<TagResolver> minimessagePlaceholders = new ArrayList<>();
    private String text;

    private ColorUtils() {}

    public static @NotNull ColorUtils of(String text) { return new ColorUtils().setText(text); }

    public @NotNull Component build() { return MINIMESSAGE.deserialize(getText(), this.minimessagePlaceholders.toArray(new TagResolver[0])); }

    public @NotNull ColorUtils parseMMP(@Subst("test_placeholder") @NotNull String placeholder, @NotNull ComponentLike value) {
        this.minimessagePlaceholders.add(
                Placeholder.component(placeholder, value)
        );
        return this;
    }

    public String toString() { return getText(); }

    private String getText() { return text; }

    private @NotNull ColorUtils setText(String text) {
        this.text = text;

        return this;
    }

}