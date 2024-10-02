package cc.davyy.slime.entities;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HologramEntity extends Entity {

    private static final int LINE_WIDTH = 100;
    private static final byte TEXT_OPACITY = (byte) 200;

    private final List<Component> hologramLines = Collections.synchronizedList(new ArrayList<>());

    public HologramEntity(@NotNull Component text, @NotNull Instance instance, @NotNull Pos spawn) {
        super(EntityType.TEXT_DISPLAY);

        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setNotifyAboutChanges(false);
        meta.setLineWidth(LINE_WIDTH);
        meta.setHasNoGravity(true);
        meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        meta.setTextOpacity(TEXT_OPACITY);
        meta.setShadow(true);
        meta.setBackgroundColor(0x000000);
        meta.setUseDefaultBackground(false);
        meta.setSeeThrough(false);
        meta.setAlignLeft(true);
        meta.setNotifyAboutChanges(true);

        hologramLines.add(text);

        updateDisplay();

        setInstance(instance, spawn);
    }

    public List<Component> getLines() {
        return List.copyOf(hologramLines);
    }

    public void addLine(@NotNull Component text) {
        hologramLines.add(text);
        updateDisplay();
    }

    public void insertLine(int index, @NotNull Component text) {
        if (index >= 0 && index <= hologramLines.size()) {
            hologramLines.add(index, text);
            updateDisplay();
        }
    }

    public void removeLine(int index) {
        if (index >= 0 && index < hologramLines.size()) {
            hologramLines.remove(index);
            updateDisplay();
        }
    }

    public void updateLine(int index, @NotNull Component newText) {
        if (index >= 0 && index < hologramLines.size()) {
            hologramLines.set(index, newText);
            updateDisplay();
        }
    }

    public void clearLines() {
        hologramLines.clear();
        updateDisplay();
        this.remove();
    }

    private void updateDisplay() {
        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();

        synchronized (hologramLines) {
            if (hologramLines.isEmpty()) {
                meta.setText(Component.empty());
                return;
            }

            Component fullText = hologramLines.getFirst();

            for (int i = 1; i < hologramLines.size(); i++) {
                fullText = fullText.append(Component.newline()).append(hologramLines.get(i));
            }

            meta.setText(fullText);
        }
    }

}