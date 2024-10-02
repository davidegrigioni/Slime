package cc.davyy.slime.managers.entities;

import cc.davyy.slime.database.HologramDatabase;
import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.services.entities.HologramService;
import cc.davyy.slime.factories.HologramFactory;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class HologramManager implements HologramService {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(HologramManager.class);
    private static final AtomicInteger entityIdCounter = new AtomicInteger();

    private final HologramFactory hologramFactory;
    private final HologramDatabase hologramDatabase;
    private final Map<Integer, HologramEntity> hologramEntityMap = new ConcurrentHashMap<>();

    @Inject
    public HologramManager(HologramFactory hologramFactory, HologramDatabase hologramDatabase) {
        this.hologramFactory = hologramFactory;
        this.hologramDatabase = hologramDatabase;
    }

    @Override
    public void createHologram(@NotNull SlimePlayer player, @NotNull Component text) {
        if (text.equals(Component.empty())) {
            player.sendMessage(Messages.MESSAGE_EMPTY.asComponent());
            return;
        }

        final HologramEntity hologramEntity = hologramFactory.createHologramEntity(player, text);
        final int entityID = entityIdCounter.getAndIncrement();

        hologramEntity.setTag(TagConstants.HOLOGRAM_ID_TAG, entityID);
        hologramEntityMap.put(entityID, hologramEntity);
        try {
            hologramDatabase.createHologram(entityID, text);
            player.sendMessage("Saved hologram into db");
        } catch (SQLException ex) {
            LOGGER.error("Error in saving hologram to db", ex);
        }

        player.sendMessage(Messages.HOLOGRAM_CREATED
                .addPlaceholder("id", String.valueOf(entityID))
                .asComponent());
    }

    @Override
    public void moveHologram(int id, @NotNull SlimePlayer player) {
        final HologramEntity hologram = hologramEntityMap.get(id);
        final Pos pos = player.getPosition();

        if (hologram == null) {
            player.sendMessage(Messages.HOLOGRAM_DOES_NOT_EXISTS
                    .addPlaceholder("id", String.valueOf(id))
                    .asComponent());
            return;
        }

        hologram.teleport(pos).thenRun(() -> player.sendMessage(Messages.HOLOGRAM_MOVED
                .addPlaceholder("pos", PosUtils.toString(pos))
                .addPlaceholder("id", String.valueOf(id))
                .asComponent()));
    }

    @Override
    public void deleteHologram(int id, @NotNull SlimePlayer player) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram == null) {
            player.sendMessage(Messages.HOLOGRAM_DOES_NOT_EXISTS
                    .addPlaceholder("id", String.valueOf(id))
                    .asComponent());
            return;
        }

        hologram.remove();
        hologramEntityMap.remove(id);
        try {
            hologramDatabase.deleteHologram(id);
            player.sendMessage("deleted from database");
        } catch (SQLException ex) {
            LOGGER.error("Error in deleting hologram to db", ex);
        }
        player.sendMessage(Messages.HOLOGRAM_DELETED
                .addPlaceholder("id", String.valueOf(id))
                .asComponent());
    }

    @Override
    public @NotNull Optional<HologramEntity> getHologramById(int id) {
        return Optional.ofNullable(hologramEntityMap.get(id));
    }

    @Override
    public @NotNull Map<Integer, HologramEntity> getAllHologramEntities() {
        return new ConcurrentHashMap<>(hologramEntityMap);
    }

    @Override
    public void clearHolograms() {
        hologramEntityMap.values().forEach(HologramEntity::remove);
        hologramEntityMap.clear();
    }

    @Override
    public void addHologramLine(@NotNull SlimePlayer player, int id, @NotNull Component text) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram == null) {
            // Optionally add player feedback here
            return;
        }

        if (text.equals(Component.empty())) {
            // Optionally handle empty text feedback
            return;
        }

        hologram.addLine(text);
        player.sendMessage(Messages.HOLOGRAM_LINE_ADDED
                .addPlaceholder("id", String.valueOf(id))
                .asComponent());
    }

    @Override
    public void insertHologramLine(@NotNull SlimePlayer player, int id, int index, @NotNull Component text) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram == null) {
            // Optionally add player feedback here
            return;
        }

        if (index < 0 || index > hologram.getLines().size()) {
            // Optionally handle index out of bounds feedback
            return;
        }

        hologram.insertLine(index, text);
        player.sendMessage(Messages.HOLOGRAM_LINE_INSERTED
                .addPlaceholder("id", String.valueOf(id))
                .addPlaceholder("index", String.valueOf(index))
                .asComponent());
    }

    @Override
    public void removeHologramLine(@NotNull SlimePlayer player, int id, int index) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram == null) {
            // Optionally add player feedback here
            return;
        }

        if (index < 0 || index >= hologram.getLines().size()) {
            // Optionally handle index out of bounds feedback
            return;
        }

        hologram.removeLine(index);
        player.sendMessage(Messages.HOLOGRAM_LINE_REMOVED
                .addPlaceholder("id", String.valueOf(id))
                .addPlaceholder("index", String.valueOf(index))
                .asComponent());
    }

    @Override
    public void updateHologramLine(@NotNull SlimePlayer player, int id, int index, @NotNull Component newText) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram == null) {
            // Optionally add player feedback here
            return;
        }

        if (index < 0 || index >= hologram.getLines().size()) {
            // Optionally handle index out of bounds feedback
            return;
        }

        if (newText.equals(Component.empty())) {
            // Optionally handle empty text feedback
            return;
        }

        hologram.updateLine(index, newText);
        player.sendMessage(Messages.HOLOGRAM_LINE_UPDATED
                .addPlaceholder("id", String.valueOf(id))
                .addPlaceholder("index", String.valueOf(index))
                .asComponent());
    }

    @Override
    public @Nullable List<Component> getHologramLines(int id) {
        final HologramEntity hologram = hologramEntityMap.get(id);
        return hologram != null ? hologram.getLines() : null;
    }

    @Override
    public void clearHologramLines(int id) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram == null) {
            // Optionally add player feedback here
            return;
        }

        hologram.clearLines();
    }

}