package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.common.ServerLinksPacket;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;
import static cc.davyy.slime.utils.ColorUtils.of;

public class PlayerSpawnListener {

    private final SidebarManager sidebarManager;

    @Inject
    public PlayerSpawnListener(SidebarManager sidebarManager) {
        this.sidebarManager = sidebarManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerSpawnEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();

            sendHeaderFooter(player);

            createServerLinks(player);

            sidebarManager.showSidebar(player);

            applyJoinKit(player);
        });
        handler.addListener(PlayerDisconnectEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();

            sidebarManager.removeSidebar(player);
        });
    }

    private void createServerLinks(@NotNull SlimePlayer player) {
        final String newsLink = getConfig().getString("news-link");
        final String bugsReportLink = getConfig().getString("bugs-report-link");
        final String announcementLink = getConfig().getString("announcement-link");

        player.sendPacket(new ServerLinksPacket(
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.NEWS, newsLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.ANNOUNCEMENTS, announcementLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.BUG_REPORT, bugsReportLink)
        ));
    }

    private void sendHeaderFooter(@NotNull SlimePlayer player) {
        final String header = getConfig().getString("header");
        final String footer = getConfig().getString("footer");

        player.sendPlayerListHeaderAndFooter(
                of(header).build(),
                of(footer).build()
        );
    }

}