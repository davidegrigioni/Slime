package cc.davyy.slime.managers;

import cc.davyy.slime.entities.holograms.HologramEntity;
import cc.davyy.slime.interfaces.IHologram;
import cc.davyy.slime.entities.holograms.HologramFactory;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.misc.TagConstants;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class HologramManager implements IHologram {

    private static final AtomicInteger entityIdCounter = new AtomicInteger(1);
    private final HologramFactory hologramFactory;
    private final Map<Integer, HologramEntity> hologramEntityMap = new ConcurrentHashMap<>();

    @Inject
    public HologramManager(HologramFactory hologramFactory) {
        this.hologramFactory = hologramFactory;
    }

    @Override
    public void createHologram(@NotNull SlimePlayer player, @NotNull Component text) {
        final HologramEntity hologramEntity = hologramFactory.createHologramEntity(player, text);
        final int entityID = entityIdCounter.getAndIncrement();

        hologramEntity.setTag(TagConstants.HOLOGRAM_ID_TAG, entityID);
        hologramEntityMap.put(entityID, hologramEntity);

        player.sendMessage(Messages.HOLOGRAM_CREATED
                .addPlaceholder("id", String.valueOf(entityID))
                .asComponent());
    }

    @Override
    public void moveHologram(int id, @NotNull SlimePlayer player) {
        final HologramEntity hologram = hologramEntityMap.get(id);
        final Pos pos = player.getPosition();

        if (hologram != null) {
            hologram.teleport(pos);
            player.sendMessage(Messages.HOLOGRAM_MOVED
                    .addPlaceholder("pos", PosUtils.toString(pos))
                    .addPlaceholder("id", String.valueOf(id))
                    .asComponent());
            return;
        }

        player.sendMessage(Messages.HOLOGRAM_DOES_NOT_EXISTS
                .addPlaceholder("id", String.valueOf(id))
                .asComponent());
    }

    @Override
    public void deleteHologram(int id, @NotNull SlimePlayer player) {
        final HologramEntity hologram = hologramEntityMap.get(id);

        if (hologram != null) {
            hologram.remove();
            hologramEntityMap.remove(id);
            player.sendMessage(Messages.HOLOGRAM_DELETED
                    .addPlaceholder("id", String.valueOf(id))
                    .asComponent());
            return;
        }

        player.sendMessage(Messages.HOLOGRAM_DOES_NOT_EXISTS
                .addPlaceholder("id", String.valueOf(id))
                .asComponent());
    }

    @Override
    public HologramEntity getHologramById(int id) {
        return hologramEntityMap.get(id);
    }

    @Override
    public Map<Integer, HologramEntity> getAllHologramEntities() {
        return new ConcurrentHashMap<>(hologramEntityMap);
    }

    @Override
    public void clearHolograms() {
        hologramEntityMap.values().forEach(HologramEntity::remove);
        hologramEntityMap.clear();
    }

}