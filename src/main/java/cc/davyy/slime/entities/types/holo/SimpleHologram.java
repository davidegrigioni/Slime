package cc.davyy.slime.entities.types.holo;

import cc.davyy.slime.entities.base.AbstractHologram;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import org.jetbrains.annotations.NotNull;

public class SimpleHologram extends AbstractHologram {

    public SimpleHologram(@NotNull Component text) {
        super(text);
        setupHologram();
    }

    private void setupHologram() {
        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setNotifyAboutChanges(false);
        meta.setLineWidth(50);
        meta.setText(text);
        meta.setHasNoGravity(true);
        meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        meta.setNotifyAboutChanges(true);

        setNoGravity(true);
        teleport(position);
    }

    @Override
    public void updateText(@NotNull Component newText) {
        final TextDisplayMeta meta = (TextDisplayMeta) getEntityMeta();
        meta.setNotifyAboutChanges(false);
        meta.setText(newText);
        meta.setNotifyAboutChanges(true);
    }

}