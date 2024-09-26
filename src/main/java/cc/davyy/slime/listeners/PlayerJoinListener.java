package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.managers.entities.nametag.NameTag;
import cc.davyy.slime.managers.entities.nametag.NameTagManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.common.ServerLinksPacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;

public class PlayerJoinListener implements EventListener<PlayerSpawnEvent> {

    private final ConfigManager configManager;
    private final NameTagManager nameTagManager;
    private final SidebarManager sidebarManager;

    @Inject
    public PlayerJoinListener(ConfigManager configManager, NameTagManager nameTagManager, SidebarManager sidebarManager) {
        this.configManager = configManager;
        this.nameTagManager = nameTagManager;
        this.sidebarManager = sidebarManager;
    }

    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();

        sendHeaderFooter(player);

        setNameTags(player, player.getPrefix()
                .append(Component.text(" "))
                .append(player.getName()));

        sidebarManager.showSidebar(player);

        applyJoinKit(player, configManager);

        createServerLinks(player);

        return Result.SUCCESS;
    }

    @SuppressWarnings("all")
    private void createServerLinks(@NotNull SlimePlayer player) {
        final String newsLink = configManager.getConfig().getString("news-link");
        final String bugsReportLink = configManager.getConfig().getString("bugs-report-link");
        final String announcementLink = configManager.getConfig().getString("announcement-link");

        player.sendPacket(new ServerLinksPacket(
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.NEWS, newsLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.ANNOUNCEMENTS, announcementLink),
                new ServerLinksPacket.Entry(ServerLinksPacket.KnownLinkType.BUG_REPORT, bugsReportLink)
        ));
    }

    private void sendHeaderFooter(@NotNull SlimePlayer player) {
        final List<String> headerList = configManager.getConfig().getStringList("header");
        final List<String> footerList = configManager.getConfig().getStringList("footer");

        final String header = String.join("\n", headerList);
        final String footer = String.join("\n", footerList);

        player.sendPlayerListHeaderAndFooter(
                of(header).build(),
                of(footer).build()
        );
    }

    private void setNameTags(@NotNull SlimePlayer player, @NotNull Component text) {
        final NameTag nameTag = nameTagManager.createNameTag(player);

        nameTag.setText(text);
        nameTag.addViewer(player);
        nameTag.mount();
    }

}