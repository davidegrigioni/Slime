package cc.davyy.slime.module;

import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.managers.entities.nametag.NameTagManager;
import cc.davyy.slime.managers.entities.npc.NPCManager;
import cc.davyy.slime.services.entities.HologramService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.minestom.server.scoreboard.TeamManager;

import java.util.function.Function;

public class EntitiesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HologramManager.class);
        bind(NPCManager.class);
        bind(NameTagManager.class);

        bind(HologramService.class).to(HologramManager.class);

        bind(HologramCommand.class);
        bind(NPCCommand.class);
    }

    @Provides
    @Singleton
    public EventNode<Event> provideEventNode() {
        return EventNode.all("npc-events");
    }

    @Provides
    public Function<Entity, Team> provideEntityToTeamFunction(TeamManager teamManager) {
        Team nameTagTeam = new TeamBuilder("name-tags", teamManager)
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .build();
        return entity -> nameTagTeam;
    }

    @Provides
    public TeamManager provideTeamManager() {
        return MinecraftServer.getTeamManager();
    }

}