package cc.davyy.slime.entities;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public interface Hologram {

    void setText(@NotNull Component text);

    Component getText();

    void setPosition(@NotNull Pos position);

    Pos getPosition();

    void hide();

    void destroy();

    void updateText(@NotNull Component newText);

}