package cc.davyy.slime.managers;

import cc.davyy.slime.entities.NPCEntity;
import cc.davyy.slime.misc.NPCFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Singleton
public class NPCManager {

    private final NPCFactory npcFactory;

    private final Map<Integer, NPCEntity> npcEntityMap = new ConcurrentHashMap<>();

    @Inject
    public NPCManager(NPCFactory npcFactory) {
        this.npcFactory = npcFactory;
    }

    public void createNPC(@NotNull String name, @NotNull PlayerSkin skin, @NotNull Instance instance,
                               @NotNull Pos spawn, @Nullable Consumer<Player> onClick) {
        final int id = NPCEntity.generateId();
        final NPCEntity npcEntity = npcFactory.createNPCEntity(name, skin, instance, spawn, onClick);

        npcEntityMap.put(id, npcEntity);
    }

    public void moveNPC(int id, @NotNull Pos newPosition) {
        final NPCEntity npcEntity = npcEntityMap.get(id);

        if (npcEntity != null) {
            npcEntity.teleport(newPosition);
        }

    }

    public void deleteNPC(int id) {
        final NPCEntity npcEntity = npcEntityMap.remove(id);

        if (npcEntity != null) {
            npcEntity.remove();
        }

    }

    public NPCEntity getNPCById(int id) {
        return npcEntityMap.get(id);
    }

    public Map<Integer, NPCEntity> getAllNPCEntities() {
        return new ConcurrentHashMap<>(npcEntityMap);
    }

    public void clearNPCs() {
        npcEntityMap.values().forEach(NPCEntity::remove);
        npcEntityMap.clear();
    }

}