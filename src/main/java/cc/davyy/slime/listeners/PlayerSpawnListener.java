package cc.davyy.slime.listeners;

import cc.davyy.slime.managers.SidebarManager;
import cc.davyy.slime.managers.npc.NameTag;
import cc.davyy.slime.managers.npc.NameTagManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.ColorUtils;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerSpawnEvent;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.JoinUtils.applyJoinKit;

public class PlayerSpawnListener {

    private final NameTagManager nameTagManager;
    private final SidebarManager sidebarManager;

    @Inject
    public PlayerSpawnListener(NameTagManager nameTagManager, SidebarManager sidebarManager) {
        this.nameTagManager = nameTagManager;
        this.sidebarManager = sidebarManager;
    }

    public void init(GlobalEventHandler handler) {
        handler.addListener(PlayerSpawnEvent.class, event -> {
            final SlimePlayer player = (SlimePlayer) event.getPlayer();
            final NameTag playerTag = nameTagManager.createNameTag(player);
            final String header = getConfig().getString("header");
            final String footer = getConfig().getString("footer");

            player.sendPlayerListHeaderAndFooter(
                    ColorUtils.of(header).build(),
                    ColorUtils.of(footer).build()
            );

            sidebarManager.showSidebar(player);

            playerTag.setText(player.getPrefix()
                    .append(Component.text(" "))
                    .append(player.getName()));
            playerTag.addViewer(player);
            playerTag.mount();

            applyJoinKit(player);
        });
    }

}