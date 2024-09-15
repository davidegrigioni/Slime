package cc.davyy.slime.managers;

import cc.davyy.slime.testing.EnvTest;
import org.junit.jupiter.api.BeforeEach;

@EnvTest
class BossBarManagerTest {

    private BossBarManager bossBarManager;

    @BeforeEach
    void setup() {
        bossBarManager = new BossBarManager();
    }

    /*@Test
    void testCreateBossBar(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, new Pos(0, 0, 0));

        Component title = Component.text("Test BossBar");
        float progress = 0.5f;
        BossBar.Color color = BossBar.Color.RED;
        BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;

        bossBarManager.createBossBar(player, title, progress, color, overlay);

        assertNotNull(bossBarManager.getPlayerBossBars().get(player.getUuid()));
    }

    @Test
    void testUpdateBossBar(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, new Pos(0, 0, 0));

        Component title = Component.text("Initial BossBar");
        float initialProgress = 0.5f;
        BossBar.Color color = BossBar.Color.RED;
        BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;

        bossBarManager.createBossBar(player, title, initialProgress, color, overlay);

        Component newTitle = Component.text("Updated BossBar");
        float newProgress = 0.75f;

        bossBarManager.updateBossBar(player, newTitle, newProgress);
        assertNotNull(bossBarManager.getPlayerBossBars().get(player.getUuid()));
    }

    @Test
    void testRemoveBossBar(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, new Pos(0, 0, 0));

        Component title = Component.text("Test BossBar");
        float progress = 0.5f;
        BossBar.Color color = BossBar.Color.RED;
        BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;

        bossBarManager.createBossBar(player, title, progress, color, overlay);

        bossBarManager.removeBossBar(player);

        assertNull(bossBarManager.getPlayerBossBars().get(player.getUuid()));
    }*/

}