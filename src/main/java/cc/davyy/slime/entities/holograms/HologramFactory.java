package cc.davyy.slime.entities.holograms;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class HologramFactory {

    public HologramEntity createHologramEntity(@NotNull Player player, @NotNull Component text) {
        var hologramPosition = player.getPosition().withY(player.getPosition().y() + 1.5);
        return new HologramEntity(text, player.getInstance(), hologramPosition);
    }

}