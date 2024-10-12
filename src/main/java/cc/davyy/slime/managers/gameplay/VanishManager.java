package cc.davyy.slime.managers.gameplay;

import cc.davyy.slime.model.Messages;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.VanishService;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamManager;
import org.jetbrains.annotations.NotNull;

@Singleton
public class VanishManager implements VanishService {

    @Override
    public void vanish(@NotNull SlimePlayer player) {
        if (!player.hasVanish()) {
            player.setVanish();

            Team vanishTeam = createOrGetTeam();
            vanishTeam.addMember(player.getUsername());

            player.sendMessage(Messages.VANISH);
            return;
        }

        player.sendMessage(Messages.ALREADY_VANISHED);
    }

    @Override
    public void unvanish(@NotNull SlimePlayer player) {
        if (player.hasVanish()) {
            player.unsetVanish();

            Team vanishTeam = createOrGetTeam();
            vanishTeam.removeMember(player.getUsername());

            player.sendMessage(Messages.UNVANISH);
            return;
        }

        player.sendMessage(Messages.ALREADY_UNVANISHED);
    }

    private Team createOrGetTeam() {
        final TeamManager teamManager = MinecraftServer.getTeamManager();
        Team vanishTeam = teamManager.getTeam("vanish");

        if (vanishTeam == null) {
            vanishTeam = teamManager.createTeam("vanish");
            vanishTeam.setNameTagVisibility(TeamsPacket.NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
            vanishTeam.setCollisionRule(TeamsPacket.CollisionRule.NEVER);
            vanishTeam.setTeamColor(NamedTextColor.BLUE);
            vanishTeam.setPrefix(Component.text("STOCAYYO"));
            vanishTeam.createTeamsCreationPacket();
        }

        return vanishTeam;
    }

    /*

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

     */

    /*
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
     */

}