package cc.davyy.slime.listeners;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.gui.LobbyGUI;
import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.FileUtils;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.JoinUtils.createItemFromConfig;

@Singleton
public class PlayerItemUseListener implements EventListener<PlayerUseItemEvent> {

    private static final String LOBBY_SL = "lobbysl";
    private static final String SERVER_SL = "serversl";
    private static final String SHOW = "show";
    private static final String HIDE = "hide";

    @Inject
    private Provider<LobbyGUI> lobbyGUIProvider;
    @Inject
    private Provider<ServerGUI> serverGUIProvider;

    @Override
    public @NotNull Class<PlayerUseItemEvent> eventType() {
        return PlayerUseItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerUseItemEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final ItemStack item = event.getItemStack();

        if (event.getHand() != Player.Hand.MAIN) return Result.INVALID;

        switch (item.getTag(TagConstants.ACTION_TAG)) {
            case LOBBY_SL -> lobbyGUIProvider.get().open(player);
            case SERVER_SL -> serverGUIProvider.get().open(player);
            case SHOW -> {
                final ItemStack hideItem = createItemFromConfig("items.hide-item");
                final int hideSlot = FileUtils.getConfig().getInt("items.hide-item.slot");

                player.getInventory().setItemStack(hideSlot, hideItem);
                player.updateViewerRule(playerVisible -> true);
                player.sendMessage(Messages.SHOW
                        .asComponent());
            }
            case HIDE -> {
                final ItemStack showItem = createItemFromConfig("items.show-item");
                final int showSlot = FileUtils.getConfig().getInt("items.show-item.slot");

                player.getInventory().setItemStack(showSlot, showItem);
                player.updateViewerRule(playerVisible -> false);
                player.sendMessage(Messages.HIDE
                        .asComponent());
            }
            default -> {}
        }

        return Result.SUCCESS;
    }

}