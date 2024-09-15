package cc.davyy.slime.managers;

import cc.davyy.slime.services.BossBarService;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class BossBarManager implements BossBarService {

    private final Map<UUID, BossBar> playerBossBars = new ConcurrentHashMap<>();

    @Override
    public void createBossBar(@NotNull SlimePlayer player, @NotNull Component title,
                              float progress, @NotNull BossBar.Color color,
                              @NotNull BossBar.Overlay overlay) {
        final BossBar bossBar = BossBar.bossBar(title, progress, color, overlay);
        player.showBossBar(bossBar);
        playerBossBars.put(player.getUuid(), bossBar);
    }

    @Override
    public void updateBossBar(@NotNull SlimePlayer player, @NotNull Component title, float progress) {
        final BossBar bossBar = playerBossBars.get(player.getUuid());
        if (bossBar != null) {
            BossBar updatedBossBar = bossBar.name(title).progress(progress);
            player.showBossBar(updatedBossBar);
        }
    }

    @Override
    public void removeBossBar(@NotNull SlimePlayer player) {
        final BossBar bossBar = playerBossBars.remove(player.getUuid());
        if (bossBar != null) {
            player.hideBossBar(bossBar);
        }
    }

    @Override
    public @NotNull Map<UUID, BossBar> getPlayerBossBars() { return playerBossBars; }

}