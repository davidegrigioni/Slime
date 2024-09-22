package cc.davyy.slime.factories;

import cc.davyy.slime.entities.HologramEntity;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class HologramFactory {

    public HologramEntity createHologramEntity(@NotNull SlimePlayer player, @NotNull Component text) {
        final var hologramPosition = player.getPosition().withY(player.getPosition().y() + 1.5);
        return new HologramEntity(text, player.getInstance(), hologramPosition);
    }

}