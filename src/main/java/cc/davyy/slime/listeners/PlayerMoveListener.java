package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.SpawnManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.TagConstants;
import com.google.inject.Inject;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;

public class PlayerMoveListener {

    private final SpawnManager spawnManager;

    @Inject
    public PlayerMoveListener(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerMoveEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();
            final int deathY = player.getInstance().getTag(TagConstants.DEATH_Y);

            if (player.getPosition().y() < deathY) {
                spawnManager.teleportToSpawn(player);
            }

        });
    }

}