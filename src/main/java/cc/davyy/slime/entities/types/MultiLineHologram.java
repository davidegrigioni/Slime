package cc.davyy.slime.entities.types;

import cc.davyy.slime.entities.base.AbstractHologram;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiLineHologram extends AbstractHologram {

    private final List<Component> lines;
    private final double lineSpacing;

    public MultiLineHologram(@NotNull List<Component> lines, @NotNull Instance instance, @NotNull Pos spawn, double lineSpacing) {
        super(Component.text(""), instance, spawn);
        this.lines = lines;
        this.lineSpacing = lineSpacing;
        setupHolograms();
    }

    private void setupHolograms() {
        double yOffset = 0;
        for (Component line : lines) {
            new HologramEntity(line, getInstance(), getPosition().add(0, yOffset, 0));
            yOffset += lineSpacing;
        }
    }

    @Override
    public void updateText(@NotNull Component newText) {}

}