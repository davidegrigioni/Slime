package cc.davyy.slime.entities.base;

import cc.davyy.slime.entities.Hologram;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHologram extends Entity implements Hologram {

    protected Component text;
    protected Pos position;

    public AbstractHologram(@NotNull Component text) {
        super(EntityType.TEXT_DISPLAY);
        this.text = text;
    }

    @Override
    public void setText(@NotNull Component text) {
        this.text = text;
        updateText(text);
    }

    @Override
    public Component getText() {
        return text;
    }

    @Override
    public void setPosition(@NotNull Pos position) {
        this.position = position;
        teleport(position);
    }

    @Override
    public @NotNull Pos getPosition() {
        return position;
    }

    @Override
    public void hide() {
        remove();
    }

    @Override
    public void destroy() {
        remove();
    }

    @Override
    public abstract void updateText(@NotNull Component newText);

}
