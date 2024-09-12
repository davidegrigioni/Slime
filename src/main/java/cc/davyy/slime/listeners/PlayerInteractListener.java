package cc.davyy.slime.listeners;

import cc.davyy.slime.gui.ServerGUI;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.TagConstants;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractListener implements EventListener<PlayerUseItemEvent> {

    @Override
    public @NotNull Class<PlayerUseItemEvent> eventType() {
        return PlayerUseItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerUseItemEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final ItemStack item = event.getItemStack();
        final Player.Hand hand = event.getHand();

        if (hand != Player.Hand.MAIN) return Result.INVALID;

        switch (item.getTag(Tag.String("action"))) {
            case "lobbysl" -> new ServerGUI().open(player);
            default -> {}
        }

        return Result.SUCCESS;
    }

}