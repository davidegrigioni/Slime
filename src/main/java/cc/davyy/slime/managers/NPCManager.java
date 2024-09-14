package cc.davyy.slime.managers;

import cc.davyy.slime.entities.NPCEntity;
import cc.davyy.slime.interfaces.INPC;
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
public class NPCManager implements INPC {

    private static final AtomicInteger entityIdCounter = new AtomicInteger(1);
    private final NPCFactory npcFactory;
    private final Map<Integer, NPCEntity> npcEntityMap = new ConcurrentHashMap<>();

    @Inject
    public NPCManager(NPCFactory npcFactory) {
        this.npcFactory = npcFactory;
    }

    @Override
    public void createNPC(@NotNull String name, @NotNull PlayerSkin skin, @NotNull SlimePlayer player) {
        final int npcId = entityIdCounter.getAndIncrement();
        NPCEntity npcEntity = npcFactory.createNPCEntity(name, skin, player.getInstance(), player.getPosition());

        npcEntity.setTag(TagConstants.NPC_ID_TAG, npcId);
        npcEntityMap.put(npcId, npcEntity);

        player.sendMessage(text("NPC created with ID: " + npcId)
                .color(NamedTextColor.GREEN));
    }

    @Override
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

    @Override
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

    @Override
    public NPCEntity getNPCById(int id) {
        return npcEntityMap.get(id);
    }

    @Override
    public Map<Integer, NPCEntity> getAllNPCEntities() {
        return new ConcurrentHashMap<>(npcEntityMap);
    }

    @Override
    public void clearNPCs() {
        npcEntityMap.values().forEach(NPCEntity::remove);
        npcEntityMap.clear();
    }

}