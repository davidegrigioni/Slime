package cc.davyy.slime.managers;

import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.model.HologramFactory;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
        HologramEntity hologramEntity = hologramFactory.createHologramEntity(player, text);
        hologramEntityMap.put(HologramEntity.generateId(), hologramEntity);
    }

    /*public void createHologram(@NotNull SlimePlayer player, @NotNull List<Component> lines) {
        var initialPosition = player.getPosition();

        for (int i = 0; i < lines.size(); i++) {
            var linePosition = initialPosition.add(0, -i * 0.25, 0);

            // Create a hologram for each line of text
            HologramEntity hologramEntity = hologramFactory.createHologramEntity(player, lines.get(i), linePosition);
            hologramEntityMap.put(HologramEntity.generateId(), hologramEntity);
        }
    }*/

}