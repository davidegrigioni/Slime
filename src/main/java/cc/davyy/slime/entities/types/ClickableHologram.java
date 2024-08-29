package cc.davyy.slime.entities.types;

import cc.davyy.slime.entities.base.AbstractHologram;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class ClickableHologram extends AbstractHologram {

    private final Runnable onClickAction;

    public ClickableHologram(@NotNull Component text, @NotNull Instance instance, @NotNull Pos spawn, @NotNull Runnable onClickAction) {
        super(text, instance, spawn);
        this.onClickAction = onClickAction;
        setupClickableHologram();
    }

    private void setupClickableHologram() {
        TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setText(getClickableText(getText()));
    }

    private Component getClickableText(@NotNull Component text) {
        return text.clickEvent(ClickEvent.runCommand("/triggerHologramAction"));
    }

    @Override
    public void updateText(@NotNull Component newText) {
        TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setText(getClickableText(newText));
    }

    public void handleClick() {
        onClickAction.run();
    }

}