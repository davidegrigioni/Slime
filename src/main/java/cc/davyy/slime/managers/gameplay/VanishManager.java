package cc.davyy.slime.managers.gameplay;

import cc.davyy.slime.model.Messages;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.VanishService;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Singleton
public class VanishManager implements VanishService {

    private final Collection<Player> playerCollection = MinecraftServer.getConnectionManager().getOnlinePlayers();

    @Override
    public void vanish(@NotNull SlimePlayer player) {
        if (!isPlayerVanished(player)) {
            player.setAutoViewable(false);

            for (Player players : playerCollection) {
                players.removeViewer(player);
            }

            player.sendMessage(Messages.VANISH
                    .asComponent());
            return;
        }

        player.sendMessage(Messages.ALREADY_VANISHED);
    }

    @Override
    public void unvanish(@NotNull SlimePlayer player) {
        if (isPlayerVanished(player)) {
            player.setAutoViewable(true);

            for (Player players : playerCollection) {
                players.addViewer(player);
            }

            player.sendMessage(Messages.UNVANISH
                    .asComponent());
            return;
        }

        player.sendMessage(Messages.ALREADY_UNVANISHED
                .asComponent());
    }

    @Override
    public boolean isPlayerVanished(@NotNull SlimePlayer player) {
        return !player.isAutoViewable();
    }

}