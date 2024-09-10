package cc.davyy.slime.model;

import cc.davyy.slime.entities.HologramEntity;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class HologramFactory {

    public HologramEntity createHologramEntity(@NotNull Player player, @NotNull Component text) {
        var hologramPosition = player.getPosition().withY(player.getPosition().y() + 1.5);
        return new HologramEntity(text, player.getInstance(), hologramPosition);
    }

}