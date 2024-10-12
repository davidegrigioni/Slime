package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.VanishService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.MinecraftServer;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamManager;

import java.util.Set;

@RootCommand
@Permission("slime.vanish")
@Singleton
public class VanishCommand {

    private final VanishService vanishService;

    @Inject
    public VanishCommand(VanishService vanishService) {
        this.vanishService = vanishService;
    }

    @Execute(name = "vanish")
    void executeVanish(@Context SlimePlayer player) {
        vanishService.vanish(player);
    }

    @Execute(name = "vanish debug")
    void executeVanishDebug(@Context SlimePlayer player, @Arg String name) {
        TeamManager teamManager = MinecraftServer.getTeamManager();
        if (teamManager.exists(name)) {
            Team team = teamManager.getTeam(name);
            Set<String> teamMembers = team.getMembers();
            player.sendMessage(teamMembers.toString());
        }
    }

    @Execute(name = "unvanish")
    void executeUnvanish(@Context SlimePlayer player) {
        vanishService.unvanish(player);
    }

}