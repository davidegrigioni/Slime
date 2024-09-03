package cc.davyy.slime.entities.types.holo;

import cc.davyy.slime.entities.base.AbstractHologram;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import org.jetbrains.annotations.NotNull;

public class ClickableHologram extends AbstractHologram {

    private final ClickCallback<Audience> onClickAction;

    public ClickableHologram(@NotNull Component text, @NotNull ClickCallback<Audience> onClickAction) {
        super(text);
        this.onClickAction = onClickAction;
        setupClickableHologram();
    }

    private void setupClickableHologram() {
        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setText(getClickableText(getText(), onClickAction));
    }

    /**
     * Customize the text to include a click event defined by the provided callback.
     *
     * @param text         The text component to be displayed.
     * @param onClickAction The action to run when the text is clicked.
     * @return The text component with the click event applied.
     */
    private Component getClickableText(@NotNull Component text, @NotNull ClickCallback<Audience> onClickAction) { return text.clickEvent(ClickEvent.callback(onClickAction)); }

    @Override
    public void updateText(@NotNull Component newText) {
        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setText(getClickableText(newText, onClickAction));
    }

}