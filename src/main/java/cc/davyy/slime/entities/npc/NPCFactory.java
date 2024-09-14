package cc.davyy.slime.entities.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public final class NPCFactory {

    public NPCEntity createNPCEntity(@NotNull String name, @NotNull PlayerSkin skin, @NotNull Instance instance, @NotNull Pos pos) {
        return new NPCEntity(name, skin, instance, pos);
    }

}