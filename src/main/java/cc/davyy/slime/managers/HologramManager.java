package cc.davyy.slime.managers;

import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.misc.HologramFactory;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.misc.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class HologramManager {

    private static final AtomicInteger entityIdCounter = new AtomicInteger(1);

    private final HologramFactory hologramFactory;

    private final Map<Integer, HologramEntity> hologramEntityMap = new ConcurrentHashMap<>();

    @Inject
    public HologramManager(HologramFactory hologramFactory) {
        this.hologramFactory = hologramFactory;
    }

    public void createHologram(@NotNull SlimePlayer player, @NotNull Component text) {
        final HologramEntity hologramEntity = hologramFactory.createHologramEntity(player, text);
        final int entityID = entityIdCounter.getAndIncrement();

        hologramEntity.setTag(TagConstants.HOLOGRAM_ID_TAG, entityID);
        hologramEntityMap.put(entityID, hologramEntity);

        player.sendMessage(Component.text("Hologram created with ID: " + entityID)
                .color(NamedTextColor.GREEN));
    }

    public void moveHologram(int id, @NotNull Pos newPosition) {
        final HologramEntity hologramEntity = hologramEntityMap.get(id);

        if (hologramEntity != null) {
            hologramEntity.teleport(newPosition);
        }

    }

    public void deleteHologram(int id) {
        final HologramEntity hologramEntity = hologramEntityMap.remove(id);

        if (hologramEntity != null) {
            hologramEntity.remove();
        }

    }

    public HologramEntity getHologramById(int id) {
        return hologramEntityMap.get(id);
    }

    public Map<Integer, HologramEntity> getAllHologramEntities() {
        return new ConcurrentHashMap<>(hologramEntityMap);
    }

    public void clearHolograms() {
        hologramEntityMap.clear();
    }

}