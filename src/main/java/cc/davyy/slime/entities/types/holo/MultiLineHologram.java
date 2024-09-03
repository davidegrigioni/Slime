package cc.davyy.slime.entities.types.holo;

import cc.davyy.slime.entities.base.AbstractHologram;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiLineHologram extends AbstractHologram {

    private final List<Component> lines;

    public MultiLineHologram(@NotNull List<Component> lines) {
        super(Component.text(""));
        this.lines = lines;
        setupHolograms();
    }

    private void setupHolograms() {
        for (Component line : lines) {
            new SimpleHologram(line);
        }
    }

    @Override
    public void updateText(@NotNull Component newText) {}

}