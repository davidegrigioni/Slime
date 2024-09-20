package cc.davyy.slime.managers;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.testing.Env;
import cc.davyy.slime.testing.EnvTest;
import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class TeleportManagerTest {

    private TeleportManager teleportManager;

    @BeforeEach
    void setup() {
        teleportManager = new TeleportManager();
    }

    @Test
    void testTeleportPlayerToTarget(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, new Pos(0, 0, 0));
        var target = env.createPlayer(instance, new Pos(100, 50, 100));

        var playerPosition = player.getPosition();
        var targetPosition = target.getPosition();

        teleportManager.teleportPlayerToTarget((SlimePlayer) player, (SlimePlayer) target);

        assertEquals(targetPosition, playerPosition);
    }

}