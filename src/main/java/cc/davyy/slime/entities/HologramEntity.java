package cc.davyy.slime.entities;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public final class HologramEntity extends Entity {

    public HologramEntity(@NotNull Component text, @NotNull Instance instance,
                          @NotNull Pos spawn) {

        super(EntityType.TEXT_DISPLAY);

        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setLineWidth(100); // Increase line width to display longer text
        meta.setText(text); // The actual text to display
        meta.setHasNoGravity(true); // Make it float
        meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER); // Faces player
        meta.setTextOpacity((byte) 200); // Semi-transparent text
        meta.setShadow(true); // Adds a shadow for better visibility
        meta.setBackgroundColor(0x000000); // Black background to stand out (optional)
        meta.setUseDefaultBackground(false); // Disable default background
        meta.setSeeThrough(false); // Disable see-through to make it fully solid
        meta.setAlignLeft(true); // Align the text to the left (optional)

        setInstance(instance, spawn);
    }

}