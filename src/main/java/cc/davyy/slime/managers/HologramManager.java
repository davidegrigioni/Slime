package cc.davyy.slime.managers;

import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.misc.HologramFactory;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.misc.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;

@Singleton
public class HologramManager {

    private static final AtomicInteger entityIdCounter = new AtomicInteger(1);
    private final HologramFactory hologramFactory;
    private final Map<Integer, HologramEntity> hologramEntityMap = new ConcurrentHashMap<>();

    @Inject
    public HologramManager(HologramFactory hologramFactory) {
        this.hologramFactory = hologramFactory;
    }

    /**
     * Creates a hologram at the player's position with the given text.
     *
     * @param player the player creating the hologram
     * @param text   the text to display in the hologram
     */
    public void createHologram(@NotNull SlimePlayer player, @NotNull Component text) {
        final HologramEntity hologramEntity = hologramFactory.createHologramEntity(player, text);
        final int entityID = entityIdCounter.getAndIncrement();

        hologramEntity.setTag(TagConstants.HOLOGRAM_ID_TAG, entityID);
        hologramEntityMap.put(entityID, hologramEntity);

        player.sendMessage(text("Hologram created with ID: " + entityID)
                .color(NamedTextColor.GREEN));
    }

    /**
     * Moves the hologram with the given ID to the player's position.
     *
     * @param id     the ID of the hologram to move
     * @param player the player to move the hologram to
     */
    public void moveHologram(int id, @NotNull SlimePlayer player) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram != null) {
            hologram.teleport(player.getPosition());
            player.sendMessage(text("Hologram " + id + " moved to position: " + player.getPosition())
                    .color(NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(text("Hologram with ID " + id + " does not exist.")
                .color(NamedTextColor.RED));
    }

    /**
     * Deletes the hologram with the given ID.
     *
     * @param id     the ID of the hologram to delete
     * @param player the player to notify of the deletion
     */
    public void deleteHologram(int id, @NotNull SlimePlayer player) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram != null) {
            hologram.remove();
            hologramEntityMap.remove(id);
            player.sendMessage(Component.text("Hologram " + id + " deleted.")
                    .color(NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(text("Hologram with ID " + id + " does not exist.")
                .color(NamedTextColor.RED));
    }

    public HologramEntity getHologramById(int id) {
        return hologramEntityMap.get(id);
    }

    public Map<Integer, HologramEntity> getAllHologramEntities() {
        return new ConcurrentHashMap<>(hologramEntityMap);
    }

    public void clearHolograms() {
        hologramEntityMap.values().forEach(HologramEntity::remove);
        hologramEntityMap.clear();
    }

}