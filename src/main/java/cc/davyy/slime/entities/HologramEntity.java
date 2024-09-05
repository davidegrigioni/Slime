package cc.davyy.slime.entities;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class HologramEntity extends Entity {

    public HologramEntity(@NotNull Component text, @NotNull Instance instance,
                          @NotNull Pos spawn) {

        super(EntityType.TEXT_DISPLAY);

        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setNotifyAboutChanges(false);
        meta.setLineWidth(50);
        meta.setText(text);
        meta.setHasNoGravity(true);
        meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        meta.setNotifyAboutChanges(true);

        setInstance(instance, spawn);
    }

}