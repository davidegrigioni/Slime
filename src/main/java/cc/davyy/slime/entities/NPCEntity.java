package cc.davyy.slime.entities;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class NPCEntity extends EntityCreature {

    public NPCEntity(@NotNull EntityType entityType) {
        super(EntityType.PLAYER);
    }

}