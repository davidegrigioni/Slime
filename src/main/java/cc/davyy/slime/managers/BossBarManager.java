package cc.davyy.slime.managers;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.services.entities.BossBarService;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

// 29/09 TODO: Configurabile BossBars annunci
@Singleton
public class BossBarManager implements BossBarService {

    @Override
    public void createBossBar(@NotNull SlimePlayer player, @NotNull Component title,
                              float progress, @NotNull BossBar.Color color,
                              @NotNull BossBar.Overlay overlay) {
        final BossBar bossBar = BossBar.bossBar(title, progress, color, overlay);

        if (player.hasTag(TagConstants.BOSS_BAR_TAG)) return;

        player.showBossBar(bossBar);
        player.setTag(TagConstants.BOSS_BAR_TAG, bossBar);
    }

    @Override
    public void updateBossBar(@NotNull SlimePlayer player, @NotNull Component title, float progress) {
        final BossBar bossBar = player.getTag(TagConstants.BOSS_BAR_TAG);
        if (bossBar != null) {
            BossBar updatedBossBar = bossBar.name(title).progress(progress);
            player.showBossBar(updatedBossBar);
        }
    }

    @Override
    public void removeBossBar(@NotNull SlimePlayer player) {
        final BossBar bossBar = player.getTag(TagConstants.BOSS_BAR_TAG);
        if (bossBar != null) {
            player.hideBossBar(bossBar);
            player.removeTag(TagConstants.BOSS_BAR_TAG);
        }
    }

}