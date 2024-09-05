package cc.davyy.slime.managers;

import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class HologramManager {

    private final Map<Integer, HologramEntity> hologramEntityMap = new HashMap<>();

    public void createHologram(@NotNull SlimePlayer player, @NotNull Component text) {
        HologramEntity hologramEntity = new HologramEntity(text, player.getInstance(), player.getPosition());
        hologramEntityMap.put(HologramEntity.generateId(), hologramEntity);
    }

}