package cc.davyy.slime.misc;

import cc.davyy.slime.entities.NPCEntity;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class NPCFactory {

    public NPCEntity createNPCEntity(@NotNull String name, @NotNull PlayerSkin skin, @NotNull Instance instance,
                                     @NotNull Pos spawn, @NotNull Consumer<Player> onClick) {
        return new NPCEntity(name, skin, instance, spawn, onClick);
    }

}