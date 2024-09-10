package cc.davyy.slime.managers;

import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.model.HologramFactory;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.TagConstants;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class HologramManager {

    private final HologramFactory hologramFactory;

    private final Map<Integer, HologramEntity> hologramEntityMap = new ConcurrentHashMap<>();

    @Inject
    public HologramManager(HologramFactory hologramFactory) {
        this.hologramFactory = hologramFactory;
    }

    public void createHologram(@NotNull SlimePlayer player, @NotNull Component text) {
        final HologramEntity hologramEntity = hologramFactory.createHologramEntity(player, text);
        final int entityID = HologramEntity.generateId();

        hologramEntity.setTag(TagConstants.HOLOGRAM_ID_TAG, entityID);
        hologramEntityMap.put(entityID, hologramEntity);
    }

    public HologramEntity getHologramById(int id) {
        return hologramEntityMap.get(id);
    }

    public Map<Integer, HologramEntity> getAllHologramEntities() { return hologramEntityMap; }

}