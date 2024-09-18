package cc.davyy.slime.factories;

import cc.davyy.slime.entities.NPCEntity;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class NPCFactory {

    public NPCEntity createNPCEntity(@NotNull String name, @NotNull PlayerSkin skin, @NotNull Instance instance, @NotNull Pos pos) {
        return new NPCEntity(name, skin, instance, pos);
    }

}