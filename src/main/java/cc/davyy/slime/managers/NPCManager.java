package cc.davyy.slime.managers;

import cc.davyy.slime.entities.NPCEntity;
import cc.davyy.slime.misc.NPCFactory;
import cc.davyy.slime.misc.TagConstants;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;

@Singleton
public class NPCManager {

    private static final AtomicInteger entityIdCounter = new AtomicInteger(1);
    private final NPCFactory npcFactory;
    private final Map<Integer, NPCEntity> npcEntityMap = new ConcurrentHashMap<>();

    @Inject
    public NPCManager(NPCFactory npcFactory) {
        this.npcFactory = npcFactory;
    }

    /**
     * Creates a new NPC and assigns it a unique ID.
     *
     * @param name   the name of the NPC
     * @param skin   the skin for the NPC
     * @param player the player for reference (for instance and position)
     */
    public void createNPC(@NotNull String name, @NotNull PlayerSkin skin, @NotNull SlimePlayer player) {
        final int npcId = entityIdCounter.getAndIncrement();
        NPCEntity npcEntity = npcFactory.createNPCEntity(name, skin, player.getInstance(), player.getPosition());

        npcEntity.setTag(TagConstants.NPC_ID_TAG, npcId);
        npcEntityMap.put(npcId, npcEntity);

        player.sendMessage(text("NPC created with ID: " + npcId)
                .color(NamedTextColor.GREEN));
    }

    /**
     * Moves an NPC to the player's position if it exists.
     *
     * @param id       the ID of the NPC to move
     * @param player   the player to move the NPC to
     */
    public void moveNPC(int id, @NotNull SlimePlayer player) {
        final NPCEntity npcEntity = npcEntityMap.get(id);

        if (npcEntity != null) {
            npcEntity.teleport(player.getPosition());
            player.sendMessage(text("NPC " + id + " moved to your position.")
                    .color(NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(text("NPC with ID " + id + " does not exist.")
                .color(NamedTextColor.RED));
    }

    /**
     * Deletes an NPC by its ID if it exists.
     *
     * @param id     the ID of the NPC to delete
     * @param player the player to send feedback to
     */
    public void deleteNPC(int id, @NotNull SlimePlayer player) {
        final NPCEntity npcEntity = npcEntityMap.get(id);

        if (npcEntity != null) {
            npcEntity.remove();
            npcEntityMap.remove(id);
            player.sendMessage(text("NPC " + id + " deleted.")
                    .color(NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(text("NPC with ID " + id + " does not exist.")
                .color(NamedTextColor.RED));
    }

    /**
     * Retrieves an NPC by its ID.
     *
     * @param id The NPC's ID.
     * @return The NPC entity, or null if not found.
     */
    public NPCEntity getNPCById(int id) {
        return npcEntityMap.get(id);
    }

    /**
     * Retrieves all NPC entities.
     *
     * @return A map of all NPC entities.
     */
    public Map<Integer, NPCEntity> getAllNPCEntities() {
        return new ConcurrentHashMap<>(npcEntityMap);
    }

    /**
     * Clears all NPCs from the instance and removes them from the map.
     */
    public void clearNPCs() {
        npcEntityMap.values().forEach(NPCEntity::remove);
        npcEntityMap.clear();
    }

}