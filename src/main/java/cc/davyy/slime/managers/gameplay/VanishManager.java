package cc.davyy.slime.managers.gameplay;

import cc.davyy.slime.model.Messages;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.VanishService;
import cc.davyy.slime.utils.GeneralUtils;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.utils.PacketUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
@Singleton
public class VanishManager implements VanishService {

    @Override
    public void vanish(@NotNull SlimePlayer player) {
        if (!player.hasVanish()) {
            player.setVanish();

            player.setAutoViewable(false);

            for (Player players : GeneralUtils.getOnlineSlimePlayers()) {
                if (!players.equals(player)) {
                    players.removeViewer(player);

                    final PlayerInfoRemovePacket removePacket = new PlayerInfoRemovePacket(
                            List.of(player.getUuid()));

                    players.sendPacket(removePacket);
                }
            }

            Component vanishTag = Component.text("[VANISH] ")
                    .color(NamedTextColor.RED) // Red color for the tag
                    .decorate(TextDecoration.BOLD); // Bold styling for the tag
            Component displayName = player.getDisplayName() != null ? player.getDisplayName() : Component.text(player.getUsername());
            Component displayNameWithVanishTag = vanishTag.append(displayName);

            //var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();

            final String textures = player.getSkin().textures();
            final String signature = player.getSkin().signature();

//            if (textures != null && signature != null) {
//                properties.add(new PlayerInfoUpdatePacket.Property("textures", textures, signature));
//            }

            var actions = EnumSet.of(PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                    PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
            var entry = new PlayerInfoUpdatePacket.Entry(
                    player.getUuid(),
                    player.getUsername(),
                    new ArrayList<>(),
                    true,
                    0,
                    GameMode.SURVIVAL,
                    displayNameWithVanishTag,
                    null);

            final PlayerInfoUpdatePacket updatePacket = new PlayerInfoUpdatePacket(
                    actions,
                    Collections.singletonList(entry)
            );

            player.sendPacket(updatePacket);

            player.sendMessage(Messages.VANISH);
            return;
        }

        player.sendMessage(Messages.ALREADY_VANISHED);
    }

    @Override
    public void unvanish(@NotNull SlimePlayer player) {
        if (player.hasVanish()) {
            player.unsetVanish();

            player.setAutoViewable(true);

            for (Player players : GeneralUtils.getOnlineSlimePlayers()) {
                players.addViewer(player);
            }

            //var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();

            final String textures = player.getSkin().textures();
            final String signature = player.getSkin().signature();

//            if (textures != null && signature != null) {
//                properties.add(new PlayerInfoUpdatePacket.Property("textures", textures, signature));
//            }

            var actions = EnumSet.of(PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                    PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                    PlayerInfoUpdatePacket.Action.UPDATE_LISTED);
            var entry = new PlayerInfoUpdatePacket.Entry(
                    player.getUuid(),
                    player.getUsername(),
                    new ArrayList<>(),
                    true,
                    0,
                    GameMode.SURVIVAL,
                    player.getDisplayName(),
                    null);

            final PlayerInfoUpdatePacket updatePacket = new PlayerInfoUpdatePacket(
                    actions,
                    Collections.singletonList(entry)
            );

            PacketUtils.broadcastPlayPacket(updatePacket);

            player.sendMessage(Messages.UNVANISH);
            return;
        }

        player.sendMessage(Messages.ALREADY_UNVANISHED);
    }

}